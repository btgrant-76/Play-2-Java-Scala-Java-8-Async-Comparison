import java.util.concurrent.TimeUnit;

import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import services.PojoService;


public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        Akka.system()
            .scheduler()
            .scheduleOnce(
            Duration.create(15, TimeUnit.SECONDS), 
            new Runnable() {
                public void run() {
                    final PojoService s = new PojoService();
                    s.doSomeServicyThings();
                }
            },
            Akka.system().dispatcher());
    }

}
