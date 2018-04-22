import com.typesafe.sbt.SbtScalariform.ScalariformKeys
import scalariform.formatter.preferences._

name := "pdf-extraction"
organization := "com.leonhardtdavid"
version := "0.1-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val root = project in file(".")

lazy val compileScalastyle = taskKey[Unit]("Run scalastyle before compile")
scalastyleFailOnError := true
compileScalastyle := scalastyle.in(Compile).toTask("").value
compileScalastyle in Compile := (compileScalastyle in Compile).dependsOn(SbtScalariform.autoImport.scalariformFormat in Compile).value
compile in Compile := (compile in Compile).dependsOn(compileScalastyle in Compile).value

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(DoubleIndentConstructorArguments, true)
  .setPreference(DoubleIndentMethodDeclaration, true)
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(SpacesAroundMultiImports, true)

coverageMinimum := 85
coverageFailOnMinimum := true

libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-file" % "0.18",
  "org.apache.pdfbox" % "pdfbox" % "2.0.9",
  "com.typesafe" % "config" % "1.3.3",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "org.mockito" % "mockito-core" % "2.15.0" % Test
)

scalacOptions ++= Seq(
  "-feature",
  "-language:postfixOps",
  "-language:reflectiveCalls"
)

