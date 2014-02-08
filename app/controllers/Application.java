package controllers;

import java.util.concurrent.Callable;

import play.Logger;
import play.libs.F;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import services.PojoService;
import views.html.index;
import static play.libs.Akka.future;
import play.libs.F.*;

public class Application extends Controller {
  
    public static Result index() {
        Logger.info("adding 'foo:bar' to Context.args");
        ctx().args.put("foo", "bar");
        
        final PojoService ps = new PojoService();
        ps.doSomeServicyThings();
        
        return async(future(new Callable<Integer>() {
            public Integer call() {
                final PojoService pjs = new PojoService(); 
                pjs.doSomeServicyThings();
                return 64;
            }
        }).map(new Function<Integer, Result>() {
            public Result apply(Integer i) {
                return ok(index.render("Your new application is ready."));
            }
        })
    );
    }
    
    
    public static Result jaxb() {
      WS.url("http://www.giantbomb.com/api/character/3005-175/?api_key=06cfd9fb987868517f54832da87b86477b8a53ad")
        .get();
     return TODO; 
    }
  
}
