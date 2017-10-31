organization in ThisBuild := "com.lightbend.lagom.sample.chirper"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test

lazy val `lagom-scala-chirper` = (project in file("."))
  .aggregate(`lagom-scala-chirper-api`, `lagom-scala-chirper-impl`, `lagom-scala-chirper-stream-api`, `lagom-scala-chirper-stream-impl`)

lazy val `lagom-scala-chirper-api` = (project in file("lagom-scala-chirper-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-scala-chirper-impl` = (project in file("lagom-scala-chirper-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`lagom-scala-chirper-api`)

lazy val `lagom-scala-chirper-stream-api` = (project in file("lagom-scala-chirper-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagom-scala-chirper-stream-impl` = (project in file("lagom-scala-chirper-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagom-scala-chirper-stream-api`, `lagom-scala-chirper-api`)
