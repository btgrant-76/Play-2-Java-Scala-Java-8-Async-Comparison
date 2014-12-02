name := "play_2_java-scala_comparison"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8")

libraryDependencies ++= Seq(
  ws,
  javaWs
)
