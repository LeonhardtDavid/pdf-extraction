// The Typesafe repository
resolvers += Resolver.typesafeRepo("releases")

// Eclipse sbt plugin
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

// Deploy plugins
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.6")

// Check style
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")

// Coverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")


