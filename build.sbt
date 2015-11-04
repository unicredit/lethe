name := "ORAM"

val zeromq = "org.zeromq" % "jeromq" % "0.3.5"

lazy val oram = crossProject.in(file("."))
  .settings(
    scalaVersion := "2.11.7",
    organization := "unicredit",
    version := "0.1.0",
    scalacOptions ++= Seq(
        "-deprecation",
        "-feature",
        "-language:postfixOps"
    ),
    libraryDependencies ++= Seq(
      "me.chrons" %%% "boopickle" % "1.1.0"
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      zeromq
    )
  )
  .jsSettings(
    persistLauncher in Compile := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.0",
      "be.doeraene" %%% "scalajs-jquery" % "0.8.1"
    )
  )


lazy val oramJVM = oram.jvm
lazy val oramJS = oram.js

lazy val oramServer = project.in(file("server"))
  .settings(
    scalaVersion := "2.11.7",
    organization := "unicredit",
    version := "0.1.0",
    libraryDependencies ++= Seq(
      zeromq
    )
  )
  .dependsOn(oram.jvm)

lazy val root = project.in(file(".")).aggregate(oramJVM, oramJS, oramServer)