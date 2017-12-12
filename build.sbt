name := "Team4_final"

version := "0.1"

scalaVersion := "2.11.7"

javaOptions += "-Xms128m"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

resolvers += "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"


libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.specs2" %% "specs2-core" % "3.6.6" % "test"
)

val sprayGroup = "io.spray"
val sprayJsonVersion = "1.3.2"
libraryDependencies ++= List("spray-json") map { c => sprayGroup %% c % sprayJsonVersion }

//spark
lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.7"
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "team4_spark",
    libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.2"
  )