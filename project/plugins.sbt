resolvers ++= Resolver.sonatypeOssRepos("snapshots")

addSbtPlugin("net.hamnaberg" % "google-discovery-sbt" % "0.6.2")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
addSbtPlugin("org.typelevel" % "sbt-typelevel" % "0.7.4")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.17.0")

libraryDependencies += "org.scalameta" %% "scalafmt-dynamic" % "3.8.3"
