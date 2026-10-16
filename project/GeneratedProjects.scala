import discovery.DiscoveryPlugin.autoImport._
import discovery.{DiscoveryPlugin, ResolveDiscoveryPlugin}
import discovery.ResolveDiscoveryPlugin.autoImport.{discoveryList, discoveryUri}
import sbt.Keys._
import sbt._
//import org.typelevel.sbt.TypelevelMimaPlugin.autoImport._

object GeneratedProjects {
  import Versions._
  /*def printDiscoveryProject = Command.args("printDiscoveryProject", "<name>") { (state, args) =>
    val discList = Project.runTask(discoveryList, state)
    val items = discList.flatMap(_._2.toEither.toOption).map(_.items).getOrElse(Vector.empty)
    args.headOption
      .flatMap(nm => items.find(_.name == nm))
      .foreach(item =>
        println(s"""Add the following code within the backticks to build.sbt:
           |
           |```
           |lazy val ${item.name} = newProject("${item.name}", uri("${item.discoveryRestUrl.renderString}"))
           |```
           |
           Make sure you also run `sbt discoveryFetch` after this has been added to build.sbt
           |""".stripMargin)
      )
    state
  }*/

  def newProject(id: String, uri: URI) =
    ProjectMatrix(id, file(id), getClass.getClassLoader)
      .jvmPlatform(scalaVersions = Versions.scalaVersions)
      .jsPlatform(scalaVersions = Versions.scalaVersions)
      .enablePlugins(DiscoveryPlugin)
      .enablePlugins(ResolveDiscoveryPlugin)
      .configure(p => configureApiProject(p, id, uri))

  def configureApiProject(project: Project, id: String, uri: URI): Project =
    project.settings(
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-core" % circeVersion,
        "org.http4s" %% "http4s-circe" % http4sVersion,
        "org.http4s" %% "http4s-client" % http4sVersion,
      ),
      name := s"googleapis-http4s-${id}",
      discoveryPackage := s"googleapis.${id}",
      Compile / discoveryFile :=
        file(id) / "shared" / "src" / "main" / "discovery" / "discovery.json",
      discoveryUri := uri,
      // Compile / discoveryGenerate := {
      //        val config = scalafmtConfig.value
      //  val files = (Compile / discoveryGenerate).value
      //      val fmt = Scalafmt.create(this.getClass.getClassLoader)
      //    val session = fmt.createSession(config.toPath)
      //  files.foreach { file =>
      //  IO.write(file, session.format(file.toPath, IO.read(file)))
      // }
      // files
      // },
      // tlMimaPreviousVersions := Set.empty,
      version := {
        val mainVersion = (ThisBuild / version).value
        val file = (Compile / discoveryFile).value
        if (file.exists) {
          val discovery =
            DiscoveryPlugin.parseDiscovery(file)
          val isSnapshot = mainVersion.endsWith("SNAPSHOT")
          val googleVersion = discovery.version + "-" + discovery.revision
          if (isSnapshot) {
            mainVersion.stripSuffix("-SNAPSHOT") + "-" + googleVersion + "-SNAPSHOT"
          } else {
            mainVersion + "-" + googleVersion
          }
        } else mainVersion
      },
    )

  /*def newProject(_name: String, uri: URI) =
    ProjectMatrix(_name, file(_name), getClass.getClassLoader)
      .jvmPlatform(scalaVersions = Versions.scalaVersions)
      .jsPlatform(scalaVersions = Versions.scalaVersions)
      .enablePlugins(sbtmatrixsources.ProjectMatrixSourcesPlugin)
      .enablePlugins(DiscoveryPlugin)
      .enablePlugins(ResolveDiscoveryPlugin)
      .configure(configureApiProject)*/
}
