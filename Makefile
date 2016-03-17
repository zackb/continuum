.PHONY: docs

CORE := continuum-core
REST := continuum-rest
MAIN := continuum

MCORE := $(MAKE) -C $(CORE)
MREST := $(MAKE) -C $(REST)
MMAIN := $(MAKE) -C $(MAIN)

default: all

all:
	$(MCORE) all
	$(MREST) all
	$(MMAIN) all

clean: 
	$(MCORE) clean
	$(MREST) clean
	$(MMAIN) clean

docs:
	$(MCORE) docs
	$(MREST) docs
	cp -r $(CORE)/build/docs/javadoc docs/html/
	cp -r $(CORE)/build/reports/tests docs/html/
	cp -r $(CORE)/build/coverage.html docs/html/
	mkdir -p docs/html/rest
	cp -r $(REST)/media docs/html/rest/
	cp -r $(REST)/README.html docs/html/rest/index.html

deploydocs: docs
	rsync -aurv docs/html/ zackbart@zackbartel.com:~/web/continuum/

sloc:
	cloc continuum*/src
