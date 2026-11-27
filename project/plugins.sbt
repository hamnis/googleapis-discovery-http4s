resolvers += "Central Snapshots".at("https://central.sonatype.com/repository/maven-snapshots/")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.3.2")
addSbtPlugin("com.github.sbt" % "sbt-git" % "2.2.0")

addSbtPlugin("net.hamnaberg" % "google-discovery-sbt" % "0.7-SNAPSHOT")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.6.2")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.22.0")
libraryDependencies += "org.scalameta" %% "scalafmt-dynamic" % "3.11.5"
