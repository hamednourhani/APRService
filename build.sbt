lazy val root = (project in file("."))
  .settings(
    organization := "ir.h4n",
    name         := "APRService",
    version      := "0.0.2-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.http4s"     %% "http4s-blaze-server"  % Http4sVersion,
      "org.http4s"     %% "http4s-circe"         % Http4sVersion,
      "org.http4s"     %% "http4s-dsl"           % Http4sVersion,
      "io.circe"       %% "circe-generic"        % CirceVersion,
      "io.circe"       %% "circe-generic-extras" % CirceVersion,
      "io.circe"       %% "circe-parser"         % CirceVersion,
      "org.specs2"     %% "specs2-core"          % Specs2Version % Test,
      "ch.qos.logback" % "logback-classic"       % LogbackVersion,
      "org.scalatest"  %% "scalatest"            % scalaTestVersion % Test,
      "org.scalamock"  %% "scalamock"            % scalaMockVersion % Test,
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0")
  )
val Http4sVersion    = "0.20.8"
val CirceVersion     = "0.11.1"
val Specs2Version    = "4.1.0"
val LogbackVersion   = "1.2.3"
val scalaTestVersion = "3.0.8"
val scalaMockVersion = "4.1.0"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)
