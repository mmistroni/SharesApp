// Set the project name to the string 'My Project'
name := "SharesApp"

version := "1.0"

scalaVersion := "2.11.5"
scalacOptions += "-target:jvm-1.7"


assembleArtifact in packageScala := true
assembleArtifact in packageDependency := true
assemblyJarName in assembly := "sharesapp.jar"

mainClass in assembly :=   Some("EdgarActorRunner")

assemblyMergeStrategy in assembly := {
  case PathList("javax", "mail", xs @ _*)         => MergeStrategy.first
  case PathList("com", "sun", xs @ _*)         => MergeStrategy.first
  case PathList("org", "slf4j", xs @ _*)         => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

// Add a single dependency
libraryDependencies += "junit" % "junit" % "4.8" % "test"
libraryDependencies += "commons-io" % "commons-io" % "2.4"
libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.5"
libraryDependencies += "commons-net" % "commons-net" % "3.4"
libraryDependencies += "org.scalamock" % "scalamock-core_2.11" % "3.2.2" % "test"
libraryDependencies += "org.scalamock" % "scalamock-scalatest-support_2.11" % "3.2.2" % "test"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.3.3"
libraryDependencies += "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.3" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.3.3"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13"
libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5" % "test"
libraryDependencies ++= Seq("org.slf4j" % "slf4j-api" % "1.7.5",
                            "org.slf4j" % "slf4j-simple" % "1.7.5",
                            "org.clapper" %% "grizzled-slf4j" % "1.0.2")
libraryDependencies += "org.powermock" % "powermock-mockito-release-full" % "1.5.4" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "com.h2database" % "h2" % "1.3.168"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.37"
libraryDependencies += "org.sorm-framework" % "sorm" % "0.3.21"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.3"
libraryDependencies +=  "org.squeryl" %% "squeryl" % "0.9.8"
libraryDependencies +=	"mysql" % "mysql-connector-java" % "5.1.10"
  
unmanagedJars in Compile ++= {
  val libs = baseDirectory.value / "lib"
  ((libs) ** "*.jar").classpath
}

resolvers += "softprops-maven" at "http://dl.bintray.com/content/softprops/maven"
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases"


