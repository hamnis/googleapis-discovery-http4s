import Versions.*

//tlBaseVersion := "0.7",
startYear := Some(2024)
//tlFatalWarnings := false,
//tlCiMimaBinaryIssueCheck := false,
//githubWorkflowJavaVersions := Seq(JavaSpec.temurin("21"))
organization := "net.hamnaberg.googleapis"
crossScalaVersions := Versions.scalaVersions
scalaVersion := crossScalaVersions.value.head
LocalRootProject / publish / skip := true
//sonatypeProfileName := "net.hamnaberg"

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
