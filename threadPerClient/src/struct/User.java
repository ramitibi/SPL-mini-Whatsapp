package struct;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * @author rami
 *
 */
public class User {
	
	
	private String phoneNumber;
	private String userName;
	private boolean isLoggedIn;
	private ArrayList<Group> groups;
	private PriorityQueue<String> lastMessages;
	
	
	public User(){}
	
	public User(String phoneNumber , String userName ){
		
		this.phoneNumber=phoneNumber;
		this.userName=userName;
		this.isLoggedIn=true;
		this.groups=new ArrayList<Group>();
		this.lastMessages=new PriorityQueue<String>();
		
	}
	
	
	/**
	 * add message to the queue
	 * @param m
	 */
	public void addMessage(String m){
		this.lastMessages.add(m);
	}
	
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	public void changeToLogedIn(){
		this.isLoggedIn=true;
	}
	
	public void changeToLogedOut(){
		this.isLoggedIn=false;
	}
	
	public boolean isLoged(){
		return this.isLoggedIn;
	}
	
	
	public void addGroup(Group group){
		if( find(group.getGroupName()) == -1 ){
			this.groups.add(group);
		}
	}
	
	public void removeGroup(Group group){
		int index = find ( group.getGroupName() );
		if(index != -1){
			this.groups.remove(index);
		}
	}
	
	
	public int find(String goupName){
		int i=0 ;
		for( i=0 ; i < this.groups.size() ; i++ ){
			if ( this.groups.get(i).getGroupName().compareTo(goupName) == 0 ){
				return i;
			}
		}
		return -1;
	}
	
	
	public String getLastMessages(){
		
		StringBuilder str=new StringBuilder();
		String s=new String();
		
		while(!this.lastMessages.isEmpty()){
			s=this.lastMessages.poll();
			str.append(s);
			str.append('\n');
		}
		
		return str.toString();
		
	}
	
	
	public String toString(){
		
		StringBuilder str = new StringBuilder();
		str.append("User name: ");
		str.append(this.userName);
		str.append("   Phone Number: ");
		str.append(this.phoneNumber);
		
		return str.toString();
	}
	
	

}
