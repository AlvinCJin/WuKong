import MergeStrategy._

val excludedFiles = Seq("pom.xml", "pom.properties", "manifest.mf", "package-info.class")

assemblyMergeStrategy in assembly := {
  case PathList("javax", "xml", xs @ _*) => MergeStrategy.first
  case f if excludedFiles.exists(f.endsWith(_)) => discard
  case "org/apache/spark/unused/UnusedStubClass.class" | "plugin.xml" | "META-INF/aop.xml" => first
  case f if f.startsWith("com/google/common/base/") => first
  case f =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(f)
}

//Disable test in assembly phase, should run "sbt test-only" before assembly in CI.
test in assembly := {}
    