default: all
install:package
all:
	./gradlew clean jar test
tgz:
	./gradlew clean jar test installDist
package:
	./gradlew clean jar test installDist debian
docs:

clean:
	./gradlew clean
