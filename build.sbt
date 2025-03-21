import GeneratedProjects.*
import Versions.*

inThisBuild(
  Seq(
    tlBaseVersion := "0.6",
    startYear := Some(2024),
    sonatypeCredentialHost := Sonatype.sonatypeLegacy,
    tlFatalWarnings := false,
    tlCiMimaBinaryIssueCheck := false,
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("21")),
    organization := "net.hamnaberg.googleapis",
    crossScalaVersions := List("2.13.16", "2.12.20", "3.3.4"),
    scalaVersion := crossScalaVersions.value.head,
    sonatypeProfileName := "net.hamnaberg",
  )
)

val root = tlCrossRootProject
  .settings(
    name := "googleapis-http4s-discovery",
    commands += printDiscoveryProject,
    sonatypeProfileName := "net.hamnaberg",
  )
  .aggregate(
    bigquery,
    storage,
    firebase,
  )

lazy val bigquery =
  newProject("bigquery", url("https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest"))

lazy val storage =
  newProject("storage", url("https://storage.googleapis.com/$discovery/rest?version=v1"))

lazy val firebase =
  newProject("firebase", url("https://firebase.googleapis.com/$discovery/rest?version=v1beta1"))
