import Versions.*

val baseVersion = "0.7"

ThisBuild / myBaseVersion := baseVersion
organization := "net.hamnaberg.googleapis"
sonaDeploymentName := organization.value
githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))
githubWorkflowTargetTags ++= Seq("v*")
githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")), RefPredicate.Equals(Ref.Branch("main")))
githubWorkflowPublish := Seq(
  WorkflowStep.Run(
    name = Some("Import signing key"),
    commands = List(
      """echo "$PGP_SECRET" | base64 -d -i - > /tmp/signing-key.gpg""",
      """echo "$PGP_PASSPHRASE" | gpg --pinentry-mode loopback --passphrase-fd 0 --import /tmp/signing-key.gpg"""
    ),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}"
    )
  ),
  WorkflowStep.Sbt(
    commands = List("publishSigned", "sonaRelease"),
    name = Some("Publish project"),
    env = Map(
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}",
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}"
    )
  )
)
githubWorkflowBuild := Seq(
  WorkflowStep.Use(
    UseRef.Public("jrouly", "scalafmt-native-action", "v5"),
    params = Map("arguments" -> "--test")),
  WorkflowStep.Sbt(
    commands = List("+test", "sbtPlugin/scripted"),
    name = Some("Build project"),
    env = Map(
      "JAVA_TOOL_OPTIONS" -> "-Xss10M"
    )
  )
)

homepage := Some(uri("https://github.com/hamnis/googleapis-discovery-http4s"))
licenses := List(License.Apache2)
developers := List(
  Developer(
    "hamnis",
    "Erlend Hamnaberg",
    "erlend@hamnaberg.net",
    uri("https://github.com/hamnis")
  )
)
publishTo := {
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if (isSnapshot.value) Some("central-snapshots".at(centralSnapshots))
  else localStaging.value
}


startYear := Some(2024)
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
