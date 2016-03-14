default: all
all:
	$(MAKE) -C continuum-core
	$(MAKE) -C continuum-rest
	$(MAKE) -C continuum
