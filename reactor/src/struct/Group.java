package struct;

import java.util.ArrayList;
import java.util.Hashtable;

public class Group {
	
	private String groupNmae;
	private Hashtable<String, User> users;
	private ArrayList<User> users2;
	
	
	public Group(){}
	
	public Group(String groupName){
		this.groupNmae=groupName;
		this.users=new Hashtable<String, User>();
		this.users2=new ArrayList<User>();
	}
	
	public String getGroupName(){
		return this.groupNmae;
	}
	
	
	public void addUser(User user){
		
		int index=this.find(user);
		
		if(index == -1){
			this.users2.add(user);
		}
	}
	
	
	public int find(User user){
		int i=-1;
		
		for(i=0 ; i < this.users2.size() ; i++){
			if(this.users2.get(i).equals(user))
				return i;
		}
		
		return -1;
		
	}
	
	public boolean find2(User user){
		int i=-1;
		
		for(i=0 ; i < this.users2.size() ; i++){
			if(this.users2.get(i).equals(user))
				return true;
		}
		
		return false;
		
	}
	
	
	public void deleteUser(User user){
		
		int index=this.find(user);
		
		if(index != -1)
			this.users2.remove(index);
		
	}
	
	
	public String listUsers(){
		
		StringBuilder str = new StringBuilder();
		
		
		int i=0;
		for(i=0 ; i< this.users2.size(); i++){
			str.append(this.users2.get(i).toString());
			str.append('\n');
		}
		
		return str.toString();
	}
	
	/*******************************/
	public void sendMessageToUsers(String str){
		int i=0;
		System.out.println("size: " + this.users.size());
		for( i=0 ; i < this.users2.size() ; i++){
			this.users2.get(i).addMessage(str);
		}
	}
	
	
	
	
	

}
