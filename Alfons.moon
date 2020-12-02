moonbuild = require 'moonbuild'

tasks:
	build: => moonbuild j: true
	run: => moonbuild 'run', j: true
	clean: => moonbuild 'clean'
	mrproper: => moonbuild 'mrproper'
