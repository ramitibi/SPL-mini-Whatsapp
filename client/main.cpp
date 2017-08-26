/* 
 * File:   main.cpp
 * Author: rami
 *
 * Created on February 10, 2015, 3:03 PM
 */

#include <cstdlib>
#include <iostream>
#include "ListenTo Keyboard.h"
#include <string>
#include <sstream>
#include <boost/thread.hpp>
#include "ConnectionHandler.h"


using namespace std;


class Task {
      private:
	ConnectionHandler& _handler;
	bool& _open;
	ListenToKeyboard& _user;
	string _s;
      public:
	Task (ConnectionHandler& connectionHandler, bool& open, ListenToKeyboard& user ,string s) : _handler(connectionHandler), _open(open), _user(user) ,_s(s) {}
 
	void run(){
	  while(_open)
	  {
	    string msg =  _user.queueURI();
	    _handler.sendLine(_s);
	    string answer="";
	    _handler.getLine(answer);
	    boost::this_thread::sleep(boost::posix_time::seconds(3));
	    if(answer != "\n$" && answer != "\n\n\n$" )
	      std::cout << answer << endl;
	  }
	}
    };

/*
 * 
 */
int main(int argc, char** argv) {
    
    
  
    string host= argv[1] ;
    string cookie="";
    short port = atoi(argv[2]);
    int len;
    bool open=false;
    string queue="";
    queue.operator += ("GET /queue.jsp HTTP/1.1");
	queue.operator += ("\n");
    ListenToKeyboard input = ListenToKeyboard(cookie);
    ConnectionHandler connectionHandler(host , port);
    if(!connectionHandler.connect()){
        std::cerr << "cannot connect to " << host << ":" << port << endl;
        return 1;
    }
    
    while(1){
        string in="";
	
        in=input.initRequest();
	
	
	
	
	std::cout << in << endl;
	//std::cout.flush();
	
        connectionHandler.sendLine(in);
        
        string temp="";
        string answer="";
        
        if(connectionHandler.getLine(answer)){
            std::cout << "step here" << endl;
        }
        else{
	  std::cerr << "Disconnected ..\n" << endl;
            break;
	}
        
        std::cout << answer << endl;
	
	if(answer.find("Set-Cookie") != -1){
            std::cout << "find cookie" << endl; 
	    int index = answer.find("=") + 1;
            char c;
            c = answer.at(index);
            while(c != '\n'){
                cookie.operator +=(c);
                index++;
                c=answer.at(index);
            }
            std::cout << cookie << endl;
	    input.setCookie(cookie);
	    queue.operator += ("Cookie: user_auth=");
	    queue.operator += (cookie);
	    queue.operator += ("\n");
	    queue.operator += ("$");
        }
        if(answer.find("Get_Cookie:") != -1){
	  std::cout << "find cookie" << endl; 
	    int index = answer.find("=") + 1;
            char c;
            c = answer.at(index);
            while(c != '\n'){
                cookie.operator +=(c);
                index++;
                c=answer.at(index);
            }
            std::cout << cookie << endl;
	    input.setCookie(cookie);
	    queue.operator += ("Cookie: user_auth=");
	    queue.operator += (cookie);
	    queue.operator += ("\n");
	    queue.operator += ("$");
	}
        
        if(answer.find("logedout") !=-1)
	  input.setCookie("");
        if(answer.find("bye")!= -1)
	  break;
	if(cookie != ""){
	//std::cout << "queue req: " << queue << endl;
	//if(false){
	  open=true;
	  Task t1(connectionHandler , open , input,queue);
	  boost::thread th(&Task::run , &t1);
	}
        
    }
    
    return 0;
}

