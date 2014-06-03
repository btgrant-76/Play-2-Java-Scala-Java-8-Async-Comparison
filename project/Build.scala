import _root_.sbt.Keys._
import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "async_demo"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies)
    .settings(
      // Add your own project settings here      
      javaOptions in (Test) += "-ea",
      javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8")
  )

}
