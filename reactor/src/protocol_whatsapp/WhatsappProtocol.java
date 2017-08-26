package protocol_whatsapp;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import protocol.ServerProtocol;
import struct.Group;
import struct.User;
import tokenizer.http.HttpMessage;

public class WhatsappProtocol implements ServerProtocol<HttpMessage> {
	
	private ArrayList<Group> groups2;
	private ArrayList<User> users2;
	private Hashtable<String , User> cookies; 
 	
	public WhatsappProtocol() {
		this.groups2 = new ArrayList<Group>();
		this.users2 = new ArrayList<User>();
		this.cookies = new Hashtable<String, User>();
	}
	
	@Override
	public HttpMessage processMessage(HttpMessage msg) {
		
		if(msg.getUri().compareTo("/list.jsp") == 0)
			return this.list(msg);
		if(msg.getUri().compareTo("/login.jsp") == 0){
			return this.login(msg);
		}
		if(msg.getUri().compareTo("/create_group.jsp") == 0)
			return this.create_group(msg);
		if(msg.getUri().compareTo("/send.jsp") == 0)
			return this.send(msg);
		if(msg.getUri().compareTo("/add_user.jsp") == 0)
			return this.add_user(msg);
		if(msg.getUri().compareTo("/remove_user.jsp") == 0)
			return this.remove_user(msg);
		if(msg.getUri().compareTo("/logout.jsp") == 0)
			return this.logout(msg);
		if(msg.getUri().compareTo("/queue.jsp") == 0)
			return this.gueue(msg);
		if(msg.getUri().compareTo("/close.jsp") == 0)
			return this.close(msg);
		
		return null;
	}
	
	//********************************************
	public HttpMessage send(HttpMessage msg){
		int startIndex= -1 , endIndex= -1 ;
		String temp=msg.getMessage();
		String target = new String();
		String type = new String();
		String content = new String();
		String[] lines = temp.split("\n");
		User sender = new User();
		
		
		if( !(temp.contains("Type=")) || !(temp.contains("Target=")) || !(temp.contains("Content=")) )
			return new HttpMessage("Error 711: Cannot send, missing parameters" + "\n");
		
		// check permition
		startIndex = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
		endIndex = lines[1].length();
		String cookie = lines[1].substring(startIndex, endIndex);
		System.out.println("cokie : " + cookie);
		if(!this.cookies.containsKey(cookie))
			return new HttpMessage("Error 555: have no permition" + "\n");
		if(!this.cookies.get(cookie).isLoged())
			return new HttpMessage("Error: have no permition" + "\n");
		
		sender = this.cookies.get(cookie);
		
		//init the type
		startIndex = temp.indexOf("Type=") + "Type=".length();
		endIndex = temp.indexOf("&Target=");
		type = temp.substring(startIndex, endIndex);
		if( !(type.compareTo("Group")==0 || type.compareTo("Direct")==0) ){
			return new HttpMessage("ERROR 836: Invalid Type" + "\n");
		}
		//System.out.println("type: " + type);
		
		//init the Content
		 
				startIndex = temp.indexOf("Content=") + "Content=".length() ;
				endIndex = temp.length()-1 ;
				content = (sender.getUserName() + " sends:" + "\n      " + temp.substring(startIndex, endIndex));
				
				//System.out.println("content: " + content);
		
		//init the Target
		
		startIndex = temp.indexOf("Target=") + "Target=".length() ;
		endIndex = temp.indexOf("&Content=") ;
		target = temp.substring(startIndex, endIndex);
		
		System.out.println("getter " + target + "\n");
		
		if(type.compareTo("Group")==0){
			int j=0;
			for(j=0 ; j < this.groups2.size() ; j++){
				if(this.groups2.get(j).getGroupName().compareTo(target)==0){
					if(this.groups2.get(j).find2(this.cookies.get(cookie))){
						this.groups2.get(j).sendMessageToUsers(content);
						return new HttpMessage("message sent" + "\n");
					}
					else return new HttpMessage("your are not in the group" + "\n");
				}
			}
			return new HttpMessage("ERROR 771: Target Does not Exist" + "\n");
		}
		
		
		if(type.compareTo("Direct")==0){
			int j=0;
			for(j=0 ; j < this.users2.size() ; j++){
				if(this.users2.get(j).getPhoneNumber().compareTo(target)==0){
					this.users2.get(j).addMessage(content);
					return new HttpMessage("message sent" + "\n");
				}
			}
			return new HttpMessage("ERROR 771: Target Does not Exist" + "\n");
		}
		
		
		return new HttpMessage("Missage Sended" + "\n");
	}
	
	public HttpMessage login(HttpMessage msg){
		//System.out.println("in log");
		int startIndex = -1 , endIndex = -1 ;
		String temp = msg.getMessage();
		String cookie = new String();
		
		if( !(temp.contains("UserName=")) || !(temp.contains("Phone=")) ){
			return new HttpMessage("ERROR 765: Cannot login, missing parameters" + "\n");
		}
		
		startIndex = temp.indexOf("UserName=") + "UserName=".length();
		endIndex = temp.indexOf("&Phone=");
		String userName = temp.substring(startIndex, endIndex);
		
		startIndex = temp.indexOf("Phone=") + "Phone=".length() ;
		endIndex = temp.length()-1;
		String phoneNumber = temp.substring(startIndex, endIndex);
		
		cookie = String.valueOf(new HttpCookie(phoneNumber, userName).hashCode());
		if(!this.cookies.isEmpty())
			if(this.cookies.containsKey(cookie)){
			this.cookies.get(cookie).changeToLogedIn();
			String ans=("WELLCOME AGAIN" + userName + "@" + phoneNumber + "\n" + "Get_Cookie: euser_auth=" + cookie);
			//System.out.println("ans =  " + ans);
			return new HttpMessage(ans+ "\n");
		}
		 
			User New = new User(phoneNumber, userName);
			this.users2.add(New);
			this.cookies.put(cookie, New);
			return new HttpMessage("WELLCOME " + userName + "@" + phoneNumber + "\n" +"Set-Cookie: user_auth=" +  cookie + "\n");
		
	}
	
	public HttpMessage logout(HttpMessage msg){
		
		String temp = msg.getMessage();
		String[] lines = temp.split("\n");
		int start = -1 , end = -1 ;
		
		start = lines[1].indexOf("=") + 1;
		end = lines[1].length();
		String cookie = lines[1].substring(start, end);
		
		if(this.cookies.containsKey(cookie)){
			this.cookies.get(cookie).changeToLogedOut();
			return new HttpMessage("logedout... bye" + "\n");
		}
		return new HttpMessage("error" + "\n");
	}
	
	public HttpMessage list(HttpMessage msg){
		
		int start = -1 ,end = -1 ;
		String temp = msg.getMessage();
		String[] lines = temp.split("\n");
		
		
		if(!temp.contains("List=") || !temp.contains("user_auth="))
			return new HttpMessage("ERROR 273: Missing Parameters1" + "\n");	
		
		// check permeation
		start = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
		end = lines[1].length();
		String cookie = lines[1].substring(start, end);
		if(!this.cookies.containsKey(cookie)){
			System.out.println("cookie:-  " + cookie);
			return new HttpMessage("ERROR 273: Missing Parameters2" + "\n");
		}
		if(!this.cookies.get(cookie).isLoged()){
			return new HttpMessage("Error: have no permition" + "\n");
		}
			
		start = temp.indexOf("List=") + "List=".length();
		end = temp.length() - 1;
		
		String type = temp.substring(start, end);
		if(type.compareTo("Users") == 0 ){
			return  new HttpMessage(this.getAllUsers() + "\n");
		}
		
		if(type.compareTo("Groups") == 0)
			return new HttpMessage(this.getAllGroups() + "\n");
		
		return new HttpMessage("ERROR 273: Missing Parameters3" + "\n");
	}
	
	public HttpMessage create_group(HttpMessage msg){
		
		String temp = msg.getMessage();
		String tempUser = new String();
		String[] lines = temp.split("\n");
		int start = -1 , end = -1 ;
		ArrayList<String> allUsers = new ArrayList<String>();
		
		if(!(temp.contains("user_auth="))){
			return new HttpMessage("Error: have no permition" + "\n");
		}
		
		if( !(temp.contains("GroupName=")) || !(temp.contains("Users=")) )
			return new HttpMessage("ERROR 675: Cannot create group, missing parameters" + "\n");
		
		// check permeation
				start = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
				end = lines[1].length() ;
				String cookie = lines[1].substring(start, end);
				if(!this.cookies.containsKey(cookie))
					return new HttpMessage("ERROR 675: Cannot create group, missing parameters" + "\n");
				if(!this.cookies.get(cookie).isLoged())
					return new HttpMessage("Error: have no permition" + "\n");
		
		
		start = temp.indexOf("GroupName=") + "GroupName=".length();
		end = temp.indexOf("&Users=")  ;
		String group = temp.substring(start, end);
		
		if(this.checkGroup(group))
			return new HttpMessage("ERROR 511: Group Name Already Taken" + "\n");
		
		start = lines[3].indexOf("Users=") + "Users=".length();
		System.out.println("length of line: " + lines[3].length() + "  start: " + start);
		String line2 = lines[3].substring(start, lines[3].length());
		
		StringTokenizer st = new StringTokenizer(line2, ",");
		while(st.hasMoreTokens()){
			tempUser = st.nextToken();
			if(!this.checkUser(tempUser))
				return new HttpMessage("ERROR 511: Unknown User " + tempUser + "\n");
			allUsers.add(tempUser);
		}
		
		Group g = new Group(group);
		int j=0;
		for(int i=0 ; i < allUsers.size() ; i++){
			for(j = 0 ; j < this.users2.size() ; j++){
				if(this.users2.get(j).getPhoneNumber().compareTo(allUsers.get(i))==0)
					g.addUser(this.users2.get(j));
			}
		}
		this.groups2.add(g);
		//System.out.println("groups num: " + this.groups2.size());
		return new HttpMessage("Group: " + group + " added" + "\n");
	}
	
	public HttpMessage remove_user(HttpMessage msg){
		
		String temp = msg.getMessage();
		String[] lines = temp.split("\n");
		int start = -1 , end = -1 ;
		
		if( !(temp.contains("Target=")) || !(temp.contains("User=")) || !temp.contains("user_auth=") )
			return new HttpMessage("ERROR 336: Cannot remove, missing parameters" + "\n");
		
		// check permeation
		start = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
		end = lines[1].length() ;
		String cookie = lines[1].substring(start, end);
		if(!this.cookies.containsKey(cookie))
			return new HttpMessage("ERROR 336: Cannot remove, missing parameters" + "\n");
		if(!this.cookies.get(cookie).isLoged())
			return new HttpMessage("Error 669: permission denied" + "\n");
		
		
		start = temp.indexOf("Target=") + "Target=".length();
		end = temp.indexOf("&User")  ;
		String target = temp.substring(start, end);
		
		if(!this.checkGroup(target))
			return new HttpMessage("ERROR 769: Target does not Exist" + "\n");
		
		start = temp.indexOf("User=") + "User=".length();
		end = temp.length() - 1;
		String user = temp.substring(start, end);
		
		if(!this.checkUser(user))
			return new HttpMessage("ERROR 336: Cannot remove, missing parameters" + "\n");
		
		if(!this.getGroup(target).find2(this.getUser(user)))
			return new HttpMessage("ERROR 336: Cannot remove, missing parameters" + "\n");
		
		this.getGroup(target).deleteUser(this.getUser(user));
		
		return new HttpMessage(user + " Deleted from " + target + "\n");
		
	}
	
	public HttpMessage add_user(HttpMessage msg){
		
		String temp = msg.getMessage();
		String[] lines = temp.split("\n");
		int start = -1 , end = -1 ;
		
		if( !(temp.contains("Target=")) || !(temp.contains("User=")) || !temp.contains("user_auth=") )
			return new HttpMessage("ERROR 242: Cannot add user, missing parameters" + "\n");
		
		// check permeation
				start = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
				end = lines[1].length() ;
				String cookie = lines[1].substring(start, end);
				if(!this.cookies.containsKey(cookie))
					return new HttpMessage("ERROR 242: Cannot add user, missing parameters" + "\n");
				if(!this.cookies.get(cookie).isLoged())
					return new HttpMessage("Error 668: permission denied" + "\n");
		
		
		start = temp.indexOf("Target=") + "Target=".length();
		end = temp.indexOf("&User") ;
		String target = temp.substring(start, end);
		
		if(!this.checkGroup(target))
			return new HttpMessage("ERROR 770: Target does not Exist" + "\n");
		
		start = temp.indexOf("User=") + "User=".length();
		end = temp.length() - 1;
		String user = temp.substring(start, end);
		
		if(!this.checkUser(user))
			return new HttpMessage("ERROR 242: Cannot add user, missing parameters" + "\n");
		
		if(this.getGroup(target).find2(this.getUser(user)))
			return new HttpMessage("ERROR 142: Cannot add user, user already in group" + "\n");
		
		this.getGroup(target).addUser(this.getUser(user));
		
		return new HttpMessage(user + " Added to " + target + "\n");
	}

	public HttpMessage gueue(HttpMessage msg){
		int startIndex = -1 , endIndex = -1 ;
		String temp = msg.getMessage();
		String[] lines = temp.split("\n");
		
		if(!( temp.contains("user_auth=") ))
			return new HttpMessage("Error: have no permition**" + "\n");
		// check permition
				startIndex = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
				endIndex = lines[1].length() ;
				String cookie = lines[1].substring(startIndex, endIndex);
				System.out.println("cookie" + cookie + "\n");
				if(!this.cookies.containsKey(cookie))
					return new HttpMessage("" + "\n");
				if(!this.cookies.get(cookie).isLoged())
					return new HttpMessage("Error: have no permition" + "\n");
				
		User user = this.cookies.get(cookie);
		String messages = user.getLastMessages();
		
		
		return new HttpMessage(messages + "\n");
	}
	
	public HttpMessage close(HttpMessage msg){
		int startIndex = -1 , endIndex = -1 ;
		String temp = msg.getMessage();
		String[] lines = temp.split("\n");
		
		if(!( temp.contains("user_auth=") ))
			return new HttpMessage("bye" + "\n");
		// check permition
				startIndex = lines[1].indexOf("user_auth=") + "user_auth=".length() ;
				endIndex = lines[1].length() ;
				String cookie = lines[1].substring(startIndex, endIndex);
				if(!this.cookies.containsKey(cookie))
					return new HttpMessage("bye" + "\n");
				if(!this.cookies.get(cookie).isLoged())
					return new HttpMessage("bye" + "\n");
				
				this.cookies.get(cookie).changeToLogedOut();
				return new HttpMessage("bye" + "\n");
		
		
	}
	//***********************************************
	
 	public String getAllUsers(){
		String ans = new String();
		StringBuilder sb = new StringBuilder();
		int i=0;
		
		for( i = 0 ; i < this.users2.size() ; i++){
			sb.append("UserName: ");
			sb.append(this.users2.get(i).getUserName());
			sb.append("  PhoneNumber: ");
			sb.append(this.users2.get(i).getPhoneNumber());
			sb.append('\n');
		}
		ans= sb.toString();
		return ans;
	}

	public String getAllGroups(){
		
		String ans = new String();
		StringBuilder sb = new StringBuilder();
		int i=0;
		
		for(i=0 ; i < this.groups2.size() ; i++){
			sb.append("GroupName: ");
			sb.append(this.groups2.get(i).getGroupName());
			sb.append("\n");
			sb.append("Users Of Group");
			sb.append('\n');
			sb.append(this.groups2.get(i).listUsers());
			sb.append('\n');
		}
		ans= sb.toString();
		return ans;
	}
	
	public boolean checkGroup(String str){
		int i=0;
		for( i=0 ; i < this.groups2.size() ; i++){
			if(this.groups2.get(i).getGroupName().compareTo(str)==0)
				return true;
		}
		return false;
	}
	
	public  Group getGroup(String str){
		int i=0;
		for( i=0 ; i < this.groups2.size() ; i++){
			if(this.groups2.get(i).getGroupName().compareTo(str)==0)
				return this.groups2.get(i);
		}
		return null;
		
	}
	
	public boolean checkUser(String str){
		int i=0;
		for(i=0 ; i < this.users2.size() ; i++)
			if(this.users2.get(i).getPhoneNumber().compareTo(str)==0)
				return true;
		return false;
	}
	
	public User getUser(String str){
		int i=0;
		for(i=0 ; i < this.users2.size() ; i++)
			if(this.users2.get(i).getPhoneNumber().compareTo(str)==0)
				return this.users2.get(i);
		return null;
	}
	
	@Override
	public boolean isEnd(HttpMessage msg) {
		if(msg.getMessage().indexOf("bye")>-1)
			return true;
		return false;
	}

}
