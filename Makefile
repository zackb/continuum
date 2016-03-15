default: all
clean: 
	$(MAKE) -C continuum-core clean
	$(MAKE) -C continuum-rest clean
	$(MAKE) -C continuum clean
all:
	$(MAKE) -C continuum-core
	$(MAKE) -C continuum-rest
	$(MAKE) -C continuum
