import GeneratedProjects._

ThisBuild / version := "0.1-SNAPSHOT"

organization := "net.hamnaberg.googleapis"
name := "googleapis-http4s-discovery"

crossScalaVersions := List("2.13.12", "2.12.18", "3.3.1")
scalaVersion := crossScalaVersions.value.head

commands += printDiscoveryProject

lazy val bigquery =
  newProject("bigquery", url("https://bigquery.googleapis.com/discovery/v1/apis/bigquery/v2/rest"))
