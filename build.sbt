import GeneratedProjects.*
import Versions.*

inThisBuild(
  Seq(
    tlBaseVersion := "0.1",
    tlSonatypeUseLegacyHost := true,
    tlFatalWarnings := false,
    tlCiMimaBinaryIssueCheck := false,
    githubWorkflowJavaVersions := Seq(JavaSpec.temurin("21")),
    organization := "net.hamnaberg.googleapis",
    crossScalaVersions := List("2.13.12", "2.12.18", "3.3.1"),
    scalaVersion := crossScalaVersions.value.head,
    sonatypeProfileName := "net.hamnaberg",
  )
)

val root = tlCrossRootProject
  .settings(name := "googleapis-http4s-discovery", commands += printDiscoveryProject)
  .aggregate(
    bigquery
  )

lazy val bigquery =
  newProject("bigquery", url("https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest"))
