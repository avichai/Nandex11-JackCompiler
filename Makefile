###############################################################################
#
# Students:
# Roee Cates, ID 308298827, roee.cates@huji.mail.ac.il
# Avichai BenDavid, ID 203455126, avichai.bendavid@huji.mail.ac.il
#
###############################################################################

JAVAC=javac
JAVACFLAGS=

SRCS=*.java
EXEC=JackCompiler

TAR=tar
TARFLAGS=cvf
TARNAME=project11.tar
TARSRCS=$(SRCS) $(EXEC) README Makefile

all: compile

compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

tar:
	$(TAR) $(TARFLAGS) $(TARNAME) $(TARSRCS)

clean:
	rm -f *.class *~ project11.tar
