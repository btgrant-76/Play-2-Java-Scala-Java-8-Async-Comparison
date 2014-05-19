package controllers;

import play.Logger;
import play.libs.F;
import play.libs.F.Function;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.Arrays.asList;

public class JavaController extends Controller {

  static final List<String> names = asList("Jim", "Dean", "Kunal");

  static final Function<String, String> lower = new Function<String, String>() {
    @Override
    public String apply(String arg0) {
      return arg0.toLowerCase();
    }
  };

  static final Function<String, Integer> strlen = new Function<String, Integer>() {
    @Override
    public Integer apply(String str) {
      return str.length();
    }
  };

  static final Function<String, List<Character>> explode = new Function<String, List<Character>>() {
    @Override
    public List<Character> apply(String arg0) {
      final List<Character> result = new ArrayList<>();
      for (char c : arg0.toCharArray()) {
        result.add(c);
      }
      return result;
    }
  };

  static <R, A> Collection<R> map(Function<A, R> f, Collection<A> c) throws Throwable {
    final Collection<R> result = new ArrayList<>();
    for (A a : c) {
      result.add(f.apply(a));
    }
    return result;
  }

  static <R, A, C extends Collection<R>> Collection<R> flatMap(Function<A, C> f, Collection<A> c) throws Throwable {
    final Collection<R> result = new ArrayList<>();
    for (A a : c) {
      for (R r : f.apply(a)) {
        result.add(r);
      }
    }
    return result;
  }

  static <R, A> R map(Function<A, R> f, A c) throws Throwable {
    return f.apply(c);
  }

  public static Result index() throws Throwable {
    final Collection<String> loweredNames = map(lower, names);
    out.println(format("%s.map(lower) yields %s", names, loweredNames));

    final Collection<Integer> lengthsOfNames = map(strlen, names);
    out.println(format("%s.map(strlen) yields %s", names, lengthsOfNames));

    final Collection<List<Character>> explodedNames = map(explode, names);
    out.println(format("%s.map(explode) yields %s", names, explodedNames));

    final Collection<Character> flattenedExplodedNames = flatMap(explode, names);
    out.println(format("%s.map(explodeToStream) yields %s", names, flattenedExplodedNames));

    return ok(map(lower, "Hello World"));
  }

  public static F.Promise<Result> proxy() {
    final F.Promise<WS.Response> responsePromise = WS.url("http://example.com").get();

    Logger.info("Before map");
    final F.Promise<Result> resultPromise = responsePromise.map(
        new Function<WS.Response, Result>() {
          @Override
          public Result apply(WS.Response wsResponse) {
            Logger.info("Within map");
            response().setContentType(wsResponse.getHeader("Content-Type"));
            return ok(wsResponse.getBody());
          }
        }
    );

    Logger.info("After map");
    return resultPromise;
  }

  public static F.Promise<Result> parallel() {
    final long start = System.currentTimeMillis();
    final Function<WS.Response, Long> getLatency = new Function<WS.Response, Long>() {
      @Override
      public Long apply(WS.Response response) {
        return System.currentTimeMillis() - start;
      }
    };

    F.Promise<Long> googleLatency = WS.url("http://google.com").get().map(getLatency);
    F.Promise<Long> yahooLatency = WS.url("http://yahoo.com").get().map(getLatency);

    return googleLatency.flatMap(new Function<Long, F.Promise<Result>>() {
      @Override
      public F.Promise<Result> apply(Long googleResponseTime) {
        return yahooLatency.map(new Function<Long, Result>() {
          @Override
          public Result apply(Long yahooResponseTime) {
            return ok(format("Google response time:  %d; Yahoo response time:  %d",
                             googleResponseTime, yahooResponseTime));
          }
        });
      }
    });
  }

  private static final String paramsFromFoo(Object anything) {
    return "?bar=baz";
  }

  public static F.Promise<Result> sequential() {
    final F.Promise<WS.Response> foo = WS.url("http://www.foo.com").get();

    return foo.flatMap(new Function<WS.Response, F.Promise<Result>>() {
      @Override
      public F.Promise<Result> apply(WS.Response fooResponse) {
        // Use data in fooResponse to build the second request
        final F.Promise<WS.Response> bar = WS.url("http://www.bar.com/" + paramsFromFoo(fooResponse))
            .get();

        return bar.map(new Function<WS.Response, Result>() {
          @Override
          public Result apply(WS.Response barResponse) {
            // Now you can use barResponse and fooResponse to build a Result
            return ok(format("response from foo.com is %s & from bar.com is %s",
                fooResponse.getStatusText(), barResponse.getStatusText()));
          }
        });
      }
    });
  }

  // Handle Exceptions in Futures by logging them and returning a fallback value
  private static <T> F.Promise<T> withErrorHandling(F.Promise<T> promise, T fallback) {
    return promise.recover(new Function<Throwable, T>() {
      @Override
      public T apply(Throwable throwable) {
        Logger.error("Something went wrong!", throwable);
        return fallback;
      }
    });
  }

  public static F.Promise<Result> checkHostName(String hostName) {
    // try using "thisdomaindoesnotexist"
    final F.Promise<String> myPromise = WS.url(format("http://www.%s.com", hostName)).get()
        .map(new Function<WS.Response, String>() {
          @Override
          public String apply(WS.Response response) {
            return response.getStatusText();
          }
        });

    final F.Promise<String> myPromiseWithFallback = withErrorHandling(myPromise, "fallback value");

    // str either contains the result of myFuture's async I/O or
    // "fallback value" if any Exception was thrown
    return myPromiseWithFallback.map(new Function<String, Result>() {
      @Override
      public Result apply(String s) {
        return ok(s);
      }
    });
  }

}
