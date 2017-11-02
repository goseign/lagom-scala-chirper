organization in ThisBuild := "com.lightbend.lagom.sample.chirper"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"

lazy val `lagom-scala-chirper` = (project in file("."))
  .aggregate(
    `friend-api`, `friend-impl`
  )

lazy val `friend-api` = (project in file("friend-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `friend-impl` = (project in file("friend-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`friend-api`)
