package controllers;

import play.Logger;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;

import static java.lang.System.out;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkedInJava8Controller extends Controller {

    private static final List<String> names = Arrays.asList("Jim", "Dean", "Kunal");
    private static final Function<String, String> lower = str -> str.toLowerCase();
    private static final Function<String, Integer> strlen = str -> str.length();
    private static final Function<String, List<Character>> explode = str -> {
        final List<Character> chars = new ArrayList<>();
        for (char c : str.toCharArray()) {
            chars.add(Character.valueOf(c));
        }
        return chars;
    };

    private static final Function<String, Stream<Character>> explodeToStream = str -> explode.apply(str).stream();

    private static F.Function3<String, Integer, Character, List<String>> dur = (str, i, ch) -> Collections.emptyList();


    public static Result index() {
        final List<String> loweredNames = names.stream().map(lower).collect(Collectors.toList());
        out.println(format("%s.map(lower) yields %s", names, loweredNames));

        final List<Integer> lengthsOfNames = names.stream().map(strlen).collect(Collectors.toList());
        out.println(format("%s.map(strlen) yields %s", names, lengthsOfNames));

        final List<List<Character>> explodedNames = names.stream().map(explode).collect(Collectors.toList());
        out.println(format("%s.map(explode) yields %s", names, explodedNames));

        final List<Character> flattenedExplodedNames = names.stream().flatMap(explodeToStream).collect(Collectors.toList());
        out.println(format("%s.map(explodeToStream) yields %s", names, flattenedExplodedNames));

        return ok();
    }

    public static F.Promise<Result> proxy() {
        final F.Promise<WS.Response> responsePromise = WS.url("http://example.com").get();

        Logger.info("Before map");
        final F.Promise<Result> resultPromise = responsePromise.map((wsResponse) -> {
            Logger.info("Within map");
            response().setContentType(wsResponse.getHeader("Content-Type"));
            return ok(wsResponse.getBody());
        });

        Logger.info("After map");
        return resultPromise;
    }

    public static F.Promise<Result> parallel() {
        final long start = System.currentTimeMillis();

        final F.Function<WS.Response, Long> getLatency = resp -> System.currentTimeMillis() - start;

        F.Promise<Long> googleLatency = WS.url("http://google.com").get().map(getLatency);
        F.Promise<Long> yahooLatency = WS.url("http://yahoo.com").get().map(getLatency);

        return googleLatency.flatMap(googleResponseTime ->
            yahooLatency.map(yahooResponseTime ->
                ok(format("Google response time:  %d; Yahoo response time:  %d", googleResponseTime, yahooResponseTime)))
        );
    }
}
