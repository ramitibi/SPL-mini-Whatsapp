/* 
 * File:   ListenTo Keyboard.h
 * Author: rami
 *
 * Created on February 10, 2015, 6:14 PM
 */

#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <boost/thread.hpp>

#ifndef LISTENTO_KEYBOARD_H
#define	LISTENTO_KEYBOARD_H

#ifdef	__cplusplus
extern "C" {
#endif

using namespace std;
    
class ListenToKeyboard{
    
    private:
            string vertion;
            string cookie;
            
    public:
        virtual ~ListenToKeyboard(); 
        ListenToKeyboard(string cookie);
        void setCookie(string cookie);
        string initRequest();
        string sendURI();
        string loginURI();
        string logoutURI();
        string listURI();
        string create_groupURI();
        string add_userURI();
        string remove_userURI();
        string queueURI();

        
        
    };

#ifdef	__cplusplus
}
#endif

#endif	/* LISTENTO_KEYBOARD_H */

