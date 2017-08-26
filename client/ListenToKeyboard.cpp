#include "ListenTo Keyboard.h"

ListenToKeyboard::ListenToKeyboard(string cookie){
    this->cookie=cookie;
}

ListenToKeyboard::~ListenToKeyboard() {

}

void ListenToKeyboard::setCookie(string cookie) {
    this->cookie=cookie;
}


string ListenToKeyboard::initRequest() {
    
    this->vertion = " HTTP/1.1" ;
    string type="" ;
    string uri="" ;
    string input="";
    string c="";
    string request="";
    string uriReq="";
    
    std::cout << "chose command number" << endl;
    std::cout << "1) login" << endl;
    std::cout << "2) logout" << endl;
    std::cout << "3) add" << endl;
    std::cout << "4) list" << endl;
    std::cout << "5) create" << endl;
    std::cout << "6) send" << endl;
    std::cout << "7) remove" << endl;
    std::cout << "8) queue" << endl;
    std::cout << "9) exit "<< endl;  
    
    std::cin >> input;
    
    if(input.compare("1")==0){
        uri=" /login.jsp";
        type="POST";
        uriReq=ListenToKeyboard::loginURI();
    }
    if(input.compare("2")==0){
        uri=" /logout.jsp";
        type="GET";
    }
    if(input.compare("3")==0){
        uri=" /add_user.jsp";
        type="POST";
        uriReq=ListenToKeyboard::add_userURI();
    }
    if(input.compare("4")==0){
        uri=" /list.jsp";
        type="POST";
        uriReq=ListenToKeyboard::listURI();
    }
    if(input.compare("5")==0){
        uri=" /create_group.jsp";
        type="POST";
        uriReq=ListenToKeyboard::create_groupURI();
    }
    if(input.compare("7")==0){
        uri=" /remove_user.jsp";
        type="POST";
        uriReq=ListenToKeyboard::remove_userURI();
    }
    if(input.compare("6")==0){
        uri=" /send.jsp";
        type="POST";
        uriReq=ListenToKeyboard::sendURI();
    }
    if(input.compare("8")==0){
        uri=" /queue.jsp";
        type="GET";
    }
    
    if(input.compare("9")==0){
      uri=" /close.jsp";
      type="GET";
    }
    request.append(type);
    request.append(uri);
    request.append(vertion);
    request.append("\n");
    
    //set cookie
    
    if(this->cookie != ""){
        c.append("Cookie: user_auth=");
        c.append(this->cookie);
        request.append(c);
        request.append("\n");
    }
        
    request.append("\n");
    request.append(uriReq);
    request.append("\n");
    request.append("$");
    
    return request;
    
}

string ListenToKeyboard::loginURI() {
    string str="";
    string user="";
    string phonenumber="";
    
    std::cout << "enter user name" << endl;
    std::cin >> user;
    
    std::cout << "enter phone number" << endl;
    std::cin >> phonenumber;
    
    str.operator +=("UserName=");
    str.operator +=(user);
    str.operator +=("&Phone=");
    str.operator +=(phonenumber);
    
    return str;
    
}

string ListenToKeyboard::add_userURI() {
    
    string str="";
    string target="";
    string user="";
    
    std::cout << "inter Group name" << endl;
    std::cin >> target;
    std::cout << "enter User name" << endl;
    std::cin >> user;
    
    str.operator +=("Target=");
    str.operator +=(target);
    str.operator +=("&User=");
    str.operator +=(user);
    
    return str;
    
    
}

string ListenToKeyboard::listURI() {
    
    string str="";
    string type="";
    
    std::cout << "Groups/Users" << endl;
    std::cin >> type;
    
    str.operator +=("List=");
    str.operator +=(type);
    
    return str;
    
}

string ListenToKeyboard::create_groupURI() {
    
    string str="";
    string group="";
    string user="";
    int i=0;
    
    std::cout << "enter group name" << endl;
    std::cin >> group;
    
    str.operator +=("GroupName=");
    str.operator +=(group);
    str.operator +=("&Users=");
    
    std::cout << "enter users.. end with '0' " << endl;
    std::cin >> user;
    if(user != "")
        str.operator +=(user);
    
    while(user != "0" ){
        std::cin >> user;
        if(user != "0"){
            str.operator +=(',');
            str.operator +=(user);
        }
    }
    
    return str;
    
}

string ListenToKeyboard::sendURI() {
    
    string str="";
    string type="";
    string target="";
    string content="";
    char buf[1024];
  // Gets cin from user
  
    
    std:cout << "Group or Direct" << endl;
    std::cin >> type;
    
    std::cout << "message target" << endl;
    std::cin >> target;
    
    std::cout << "message body" << endl;
    std::cin >> content;
      std::cin.getline(buf , 1024);
     std::string line(buf);
    content.operator +=(" ");
    content.operator +=(line);
    
    str.operator +=("Type=");
    str.operator +=(type);
    str.operator +=("&Target=");
    str.operator +=(target);
    str.operator +=("&Content=");
    str.operator +=(content);
    
    return str;
}

string ListenToKeyboard::remove_userURI() {
    
    string str="";
    string target="";
    string user="";
    
    std::cout << "inter Group name" << endl;
    std::cin >> target;
    std::cout << "enter User name" << endl;
    std::cin >> user;
    
    str.operator +=("Target=");
    str.operator +=(target);
    str.operator +=("&User=");
    str.operator +=(user);
    
    return str;
    
}

string ListenToKeyboard::queueURI() {
    
    string str="";
    str.append("GET /queue.jsp HTTP/1.1");
    str.append("\n");
    str.append("\n");
    str.append("$");
    
    return str;
}


