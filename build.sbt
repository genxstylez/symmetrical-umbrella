name := """url-shortener"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


// ebean
lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

resolvers += "dl-john-ky" at "http://dl.john-ky.io/maven/releases"

libraryDependencies += "io.john-ky" %% "hashids-scala" % "1.1.1-7d841a8"