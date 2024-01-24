resolvers ++= Resolver.sonatypeOssRepos("snapshots")

addSbtPlugin("net.hamnaberg" % "google-discovery-sbt" % "0.5-SNAPSHOT")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")
addSbtPlugin("org.typelevel" % "sbt-typelevel" % "0.6.4")

libraryDependencies += "org.scalameta" %% "scalafmt-dynamic" % "3.7.17"
