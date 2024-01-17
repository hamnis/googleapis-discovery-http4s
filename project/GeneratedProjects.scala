import discovery.DiscoveryPlugin.autoImport.{discoveryGenerate, discoveryPackage}
import discovery.{DiscoveryPlugin, ResolveDiscoveryPlugin}
import discovery.ResolveDiscoveryPlugin.autoImport.{discoveryList, discoveryUri}
import org.scalafmt.interfaces.Scalafmt
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtConfig
import sbt.Keys.*
import sbt.*

object GeneratedProjects {
  val circeVersion = "0.14.6"
  val http4sVersion = "0.23.25"

  def configureApiProject(project: Project): Project =
    project.settings(
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-core" % circeVersion,
        "org.http4s" %% "http4s-circe" % http4sVersion,
        "org.http4s" %% "http4s-client" % http4sVersion,
      )
    )

  def printDiscoveryProject = Command.args("printDiscoveryProject", "<name>") { (state, args) =>
    val discList = Project.runTask(discoveryList, state)
    val items = discList.flatMap(_._2.toEither.toOption).map(_.items).getOrElse(Vector.empty)
    args.headOption
      .flatMap(nm => items.find(_.name == nm))
      .foreach(item => println(s"""Add the following code within the backticks to build.sbt:
           |
           |```
           |lazy val ${item.name} = newProject("${item.name}", url("${item.discoveryRestUrl.renderString}")
           |```
           |
           Make sure you also run `sbt discoveryFetch` after this has been added to build.sbt
           |""".stripMargin))
    state
  }

  def newProject(_name: String, url: URL) =
    Project(_name, file(_name))
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
        version := {
          val mainVersion = (ThisBuild / version).value
          val discovery = DiscoveryPlugin.parseDiscovery(baseDirectory.value)
          val isSnapshot = mainVersion.endsWith("SNAPSHOT")
          val googleVersion = discovery.version + "-" + discovery.revision
          if (isSnapshot) {
            mainVersion.stripSuffix("-SNAPSHOT") + "-" + googleVersion + "-SNAPSHOT"
          } else {
            mainVersion + "-" + googleVersion
          }
        },
      )
}
