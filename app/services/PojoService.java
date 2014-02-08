package services;

import play.Logger;
import play.mvc.Http;

public class PojoService {
    
    public void doSomeServicyThings() {
        Http.Context ctx = Http.Context.current.get();
        if (ctx != null)
            Logger.info(String.format("got '%s' for 'foo'", ctx.args.get("foo")));
        else 
            Logger.info("no context available");
            
            
        
        
//        Logger.info(String.format("got '%s' for 'foo'", Http.Context.current().args.get("foo")));
    }

}
