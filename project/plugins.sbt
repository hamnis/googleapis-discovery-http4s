resolvers ++= Resolver.sonatypeOssRepos("snapshots")

addSbtPlugin("net.hamnaberg" % "google-discovery-sbt" % "0.3.1-SNAPSHOT")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.2")

libraryDependencies += "org.scalameta" %% "scalafmt-dynamic" % "3.7.17"
