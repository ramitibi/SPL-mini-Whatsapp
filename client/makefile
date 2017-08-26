CFLAGS:=-c  -g
LDFLAGS:=-lboost_system -lboost_locale -lboost_thread

all: WhatsappClient
	g++ -o bin/client bin/main.o bin/ConnectionHandler.o bin/ListenToKeyboard.o $(LDFLAGS) 

WhatsappClient: bin/main.o bin/ConnectionHandler.o bin/ListenToKeyboard.o

bin/main.o: main.cpp
	g++ $(CFLAGS) -o bin/main.o main.cpp

bin/ConnectionHandler.o: ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o ConnectionHandler.cpp

bin/ListenToKeyboard.o: ListenToKeyboard.cpp
	g++ $(CFLAGS) -o bin/ListenToKeyboard.o ListenToKeyboard.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
