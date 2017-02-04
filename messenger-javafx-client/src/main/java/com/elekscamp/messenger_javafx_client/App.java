package com.elekscamp.messenger_javafx_client;

import java.io.IOException;

import com.elekscamp.messenger_javafx_client.Entities.*;
import com.elekscamp.messenger_javafx_client.Exceptions.HttpErrorCodeException;
import com.elekscamp.messenger_javafx_client.DAL.*;

public class App 
{
/*	public static void main( String[] args ) {
		
    	Credential cred = new Credential("Ananas", "1234");
    	RequestManager.setRequestApi("http://localhost:55059/api");
    	ContentProvider provider = new ContentProvider();
    	// authorization
    	try {
			User currentUser = RequestManager.authenticateUser(cred);
			System.out.println("Current user: " + currentUser);
    	} catch (IOException e) {
    		System.out.println(e.getMessage());
       	} catch (HttpErrorCodeException e){
    		System.out.println(e.getMessage());
       	}
    	// creating message, conversation; updating personalInfo
 /*    	try {
//     		System.out.println("message: " + provider.getMessageProvider().add(new Message(1, 1, "text")));
//			System.out.println("conversation: " + provider.getConversationProvider().add(new Conversation("SuperKarp")));
     		provider.getPersonalInfoProvider().update(1, new PersonalInfo(null, null, null, "5555", null));
     	} catch (IOException e) {
    		System.out.println(e.getMessage());
       	} catch (HttpErrorCodeException e){
    		System.out.println(e.getMessage());
       	}
  */
     	// registration new user
/*    	try {
    		System.out.println("registration: " + new RegistrationProvider().register(new User("amazsi2ngdsa", "pas2sdsaa", "amazing@amazing.com")));
    	} catch (HttpErrorCodeException e) {
    		System.out.println(e.getMessage());
    	} catch (IOException e){
    		System.out.println(e.getMessage());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
*/    	
    	// get-methods
  /*  	try {	
    /*		Conversation conversation1 = new Conversation("Superkarp");
    		conversation1.setCreationDate(new Date().getTime());
    		System.out.println(provider.getConversationProvider().add(conversation1));
    		for(Conversation conversation : provider.getConversationProvider().getAll()){
    			System.out.println(conversation);
    		}*/
    
	/*		for(User user : provider.getUserProvider().getAll()) {
				System.out.println(user);
			}
		
    	//	Message message = new Message(2, 1, "sdadsa");
    	//	System.out.println((new Date().getTime()));
    	//	System.out.println(provider.getMessageProvider().add(message));
		//	for (Message message1 : provider.getMessageProvider().getAll()){
		//		System.out.println(message1);
		//	}
		/*
			for (Conversation conversation : provider.getConversationProvider().getAll()){
				System.out.println(conversation);
			}
			*/
	/*		for (PersonalInfo personalInfo : provider.getPersonalInfoProvider().getAll()){
				System.out.println(personalInfo);
			}
	/*	*/
		//	System.out.println("First PersonalInfo: " + provider.getPersonalInfoProvider().getById(1));
		/*	provider.getUserConversationProvider().removeUser(2, 1);
    		System.out.println("user added to conversation: " + provider.getUserConversationProvider().addUser(2, 1));
    		for (UserConversation u : provider.getUserConversationProvider().getAll()) {
    			System.out.println(u);
    		}*/
  /*  	} catch (HttpErrorCodeException e) {
    		System.out.println(e.getMessage());
    	} catch (IOException e){
    		System.out.println(e.getMessage());
    	} 
    }*/
}
