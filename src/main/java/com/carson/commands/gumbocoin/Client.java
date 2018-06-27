package com.carson.commands.gumbocoin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

	public static String ip = "localhost";
	public static final int PORT = 9211;
	/**
	 * prints the client header message, mainly for debug
	 */
	public Client() {
		System.out.println("===================client");
	}
	
	
	/**
	 * runs sendMessageUnclean but handles (and prints) errors for better debug and code
	 * @param message
	 * @return the server's response
	 */
	public static String sendMessage(String message){
		try {
			return sendMessageUnclean(message);
		}catch(UnknownHostException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			System.err.println("eof exception thrown");
			return "server connection was terminated by the server";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "FAILED"; 
		//this is what you get if it through an error.
		//should have built-in safeguards on the client side at least, 
		//however should just be avoided in general
	}
	
	/**
	 * 
	 * @param message the message to send
	 * @return the response from the server
	 * @throws UnknownHostException
	 * @throws EOFException
	 * @throws IOException
	 */
	public static String sendMessageUnclean(String message) throws UnknownHostException, EOFException,IOException{
		Socket client = new Socket(ip,Client.PORT); //starts the conversation
		DataOutputStream out = new DataOutputStream(client.getOutputStream()); //gets streams
        DataInputStream in = new DataInputStream(client.getInputStream()); 
        out.writeUTF(message);//sends the message
        String response  = in.readUTF();//waits for response and then returns it.
        client.close();
        return response;
	}
	
}
