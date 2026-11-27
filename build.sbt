import Versions.*

val baseVersion = "0.7"

inThisBuild(
  Seq(
    crossScalaVersions := Versions.scalaVersions,
    scalaVersion := crossScalaVersions.value.head,
    myBaseVersion := baseVersion,
    sonaDeploymentName := "net.hamnaberg",
    homepage := Some(uri("https://github.com/hamnis/google-discovery-scala")),
    licenses := List(License.Apache2),
    developers := List(
      Developer(
        "hamnis",
        "Erlend Hamnaberg",
        "erlend@hamnaberg.net",
        uri("https://github.com/hamnis"),
      )
    ),
    publishTo := {
      val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
      if ((ThisBuild / isSnapshot).value) Some("central-snapshots".at(centralSnapshots))
      else localStaging.value
    },
    commands += Command.command("pushRelease") { state =>
      if (state.getSetting(isSnapshot).getOrElse(false))
        state // a snapshot is good-to-go
      else // a non-snapshot releases as a bundle
        Command.process("sonaRelease", state, _ => ())
    },
  )
)

startYear := Some(2024)
LocalRootProject / publish / skip := true

lazy val bigquery = GeneratedProjects.newProject(
  id = "bigquery",
  uri = uri("https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest"),
)
lazy val storage = GeneratedProjects.newProject(
  id = "storage",
  uri = uri("https://storage.googleapis.com/$discovery/rest?version=v1"),
)

lazy val firebase = GeneratedProjects.newProject(
  id = "firebase",
  uri = uri("https://firebase.googleapis.com/$discovery/rest?version=v1beta1"),
)

lazy val root = project
  .in(file("."))
  .aggregate((bigquery.projectRefs ++ storage.projectRefs ++ firebase.projectRefs) *)
  .settings(
    name := "googleapis-http4s-discovery"
  )
