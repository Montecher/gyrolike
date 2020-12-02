var 'MAIN', 'Main'
var 'NAME', 'Observable'

var 'SOURCES', _.wildcard 'src/**.java'
var 'DATA', _.wildcard 'data/**'
var 'DATA', _.exclude DATA, 'data/.gitkeep'

var 'CLASSES', _.patsubst SOURCES, 'src/%.java', 'build/classes/%.class'
var 'DATA_OUT', _.patsubst DATA, 'data/%', 'build/classes/%'
var 'JARFILE', "build/#{NAME}.jar"
var 'MANIFEST', 'build/Manifest.mf'

var 'JAVA', '/usr/lib/jvm/java-1.8-openjdk/bin/java'
var 'JAVAC', '/usr/lib/jvm/java-1.8-openjdk/bin/javac'
var 'JAR', 'jar'

var 'JAVACFLAGS', '-Xlint:unchecked'

with public target 'info'
	\fn =>
		for var in *({'MAIN', 'NAME', 'SOURCES', 'DATA', 'CLASSES', 'DATA_OUT', 'JARFILE', 'MANIFEST', 'JAVA', 'JAVAC', 'JAR'})
			print "#{var}: #{_G[var]}"
	\sync!

with public default target 'jar'
	\after JARFILE

with public target 'classes'
	\after CLASSES

with public target 'data'
	\after DATA_OUT

with public target 'run'
	\after JARFILE
	\fn => _.cmd JAVA, '-jar', JARFILE

with public target 'clean'
	\fn => _.cmd 'rm', '-rf', CLASSES
	\fn => _.cmd 'rm', '-rf', DATA_OUT
	\fn => _.cmd 'rm', '-f', MANIFEST
	\sync!

with public target 'mrproper'
	\fn => _.cmd 'rm', '-rf', 'build'

with target JARFILE
	\depends CLASSES
	\depends DATA_OUT
	\depends MANIFEST
	\produces '%'
	\fn => _.cmd JAR, 'cvfm', @outfile, MANIFEST, '-C', 'build/classes', '.'
	\mkdirs!

with target MANIFEST
	\depends 'Build.moon'
	\produces '%'
	\fn => _.writefile @outfile, "Manifest-Version: 1.0\nMain-Class: #{MAIN}\n"
	\mkdirs!

with target CLASSES, pattern: 'build/classes/%.class'
	\depends 'src/%.java'
	\depends SOURCES
	\produces 'build/classes/%.class'
	\fn => _.cmd JAVAC, JAVACFLAGS, '-cp', 'src:build/classes', '-d', 'build/classes', @infile
	\mkdirs!

with target DATA_OUT, pattern: 'build/classes/%'
	\depends 'data/%'
	\produces 'build/classes/%'
	\fn => _.cmd 'cp', '-a', @infile, @outfile
	\mkdirs!
