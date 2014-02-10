import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

  val appName         = "play2testbed"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies)
    .settings(Play2WarPlugin.play2WarSettings:_*)
    .settings(
      // Add your own project settings here      
      javaOptions in (Test) += "-ea",
      // javaOptions in (Test) += "-javaagent:/Users/bgrant/projects/InvestMe_service/lib/jmockit.jar"
      Play2WarKeys.servletVersion := "2.5"
  )

}
