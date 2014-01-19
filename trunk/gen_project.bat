cd dist
set classpath=
set classpath=%classpath%;kefirUtil.jar
set classpath=%classpath%;kefirWeb.jar
set classpath=%classpath%;kefirGen.jar
set classpath=%classpath%;./../lib/log4j.jar
set classpath=%classpath%;./../lib/javaee.jar
set classpath=%classpath%;./../kefirGen/lib/commons-lang-2.3.jar

echo CLASSPATH = %classpath%
java -classpath %classpath% su.opencode.kefir.gen.project.ProjectGeneratorRunner props="D:\Projects\kefir\source\kefirGen\project.properties"