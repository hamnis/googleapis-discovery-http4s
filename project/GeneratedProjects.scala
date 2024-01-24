import discovery.DiscoveryPlugin.autoImport._
import discovery.{DiscoveryPlugin, ResolveDiscoveryPlugin}
import discovery.ResolveDiscoveryPlugin.autoImport.{discoveryList, discoveryUri}
import org.scalafmt.interfaces.Scalafmt
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtConfig
import sbt.Keys._
import sbt._
import sbtcrossproject._
import scalajscrossproject._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import xerial.sbt.Sonatype.autoImport._
import org.typelevel.sbt.TypelevelMimaPlugin.autoImport._

object GeneratedProjects {
  import Versions._
  def printDiscoveryProject = Command.args("printDiscoveryProject", "<name>") { (state, args) =>
    val discList = Project.runTask(discoveryList, state)
    val items = discList.flatMap(_._2.toEither.toOption).map(_.items).getOrElse(Vector.empty)
    args.headOption
      .flatMap(nm => items.find(_.name == nm))
      .foreach(item => println(s"""Add the following code within the backticks to build.sbt:
           |
           |```
           |lazy val ${item.name} = newProject("${item.name}", url("${item.discoveryRestUrl.renderString})")
           |```
           |
           Make sure you also run `sbt discoveryFetch` after this has been added to build.sbt
           |""".stripMargin))
    state
  }

  def configureApiProject(project: Project): Project =
    project.settings(
      sonatypeProfileName := "net.hamnaberg",
      libraryDependencies ++= Seq(
        "io.circe" %%% "circe-core" % circeVersion,
        "org.http4s" %%% "http4s-circe" % http4sVersion,
        "org.http4s" %%% "http4s-client" % http4sVersion,
      ),
    )

  def newProject(_name: String, url: URL) =
    CrossProject(_name, file(_name))(JVMPlatform, JSPlatform)
      .crossType(CrossType.Full)
      .enablePlugins(DiscoveryPlugin)
      .enablePlugins(ResolveDiscoveryPlugin)
      .configure(configureApiProject)
      .settings(
        name := s"googleapis-http4s-${_name}",
        discoveryPackage := s"googleapis.${_name}",
        discoveryUri := url,
        Compile / discoveryGenerate := {
          val config = scalafmtConfig.value
          val files = (Compile / discoveryGenerate).value
          val fmt = Scalafmt.create(this.getClass.getClassLoader)
          val session = fmt.createSession(config.toPath)
          files.foreach { file =>
            IO.write(file, session.format(file.toPath, IO.read(file)))
          }
          files
        },
        tlMimaPreviousVersions := Set.empty,
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
}
