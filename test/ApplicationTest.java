import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Content;
import play.test.WithApplication;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest extends WithApplication {

    @Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }

    @Test
    public void fileConfig() {
      final Map<String, Object> conf = new HashMap<>();
      conf.put("file.config", "farfle");
      start(fakeApplication(conf));
      assertEquals("farfle", Play.application().configuration().getString("file.config"));
    }
    
    @Test
    public void mshxml() throws InterruptedException {
      Promise<Response> pr = WS.url("http://msuxml.morningstar.com/IndexTS")
        .setQueryParameter("username", "MoMo12095")
        .setQueryParameter("password", "Z87ZxH")
        .setQueryParameter("instrument", "29.10.@CCO")
        .setQueryParameter("interval", "5")
        .setQueryParameter("type", "bar")
        .setQueryParameter("tradingdays", "1")
        .setFollowRedirects(true)
//        .setContentType(arg0)
        .get();
      
      while (!pr.wrapped().isCompleted()) {
        Thread.sleep(1_000);
      }
      
      System.out.println(pr.get().getUri().toString());
      System.out.println(pr.get().getBody());
    }
   
}
