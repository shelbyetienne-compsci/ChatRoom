/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shelbyetienne
 */
public class ChatRoom {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        ServerSocket server;//The server socket
        Map<char[],Socket> test;//where the clients will be stored
        
        try
        {
            server=new ServerSocket(1234);
            test=new HashMap<>();
            int i=0;
            
            while(true)
            {
                i++;
                Socket client=server.accept();//waiting for client
                test.put(("name"+i).toCharArray(), client);//when client connects add into map
                new ServerThread1 (client, test).start();
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}

/**
 * ServerThread is the server that can accept connections from multiple clients
 * @author se88375
 */
class ServerThread1 extends Thread
{
    private Socket client;
    private PrintWriter output;
    private Scanner input;
    private Map<char[], Socket> name1;
   
    private Map<Socket , String> name2;
    String [] bag;
    int size;
    
    /**
     * Explicit Constructor
     * @param c the client that is connecting to the Server
     * @param m the Sockets of all the clients connected
     * @throws Exception 
     */
    public ServerThread1(Socket c, Map<char[], Socket> m) throws Exception
    {
        client = c;
        output = new PrintWriter(client.getOutputStream(), true);
        input = new Scanner(client.getInputStream());
        name1=m; 
        
        name2=new HashMap<>();
        bag=new String[1000];
        size=0;
    }
    
    public void run()
    {
        
        Map<char[], Socket> Names=new HashMap<>();
        String s="null",m=" ",u=" ";
        char [] user, message, ucopy, mcopy;
        
        while(true)
        {
            //waits for first message to be sent from client
            s=input.nextLine();
            
            //will find the name only when a '/' is in the string
            if(s.contains("/")) 
            {
                //set lengths of user and message so the text can fit in array
                user=new char[s.length()];
                message=new char[s.length()];
                
                int i=0, uu=0, mm=0;
                for(;s.charAt(i) != '/';i++,uu++)
                    user[i]=s.charAt(i);
                
                i++;//increment i in order to skip '/'
                
                for(int j=0;(i)<s.length();i++,j++,mm++)
                    message[j]=s.charAt(i);

                ucopy=new char[uu];
                
                for(int i1=0;i1<uu;i1++)
                {
                    ucopy[i1]=user[i1];
                }
                
                mcopy=new char[mm];
                
                for(int i2=0;i2<mm;i2++)
                {
                    mcopy[i2]=message[i2];
                }
                
                u=new String(ucopy);//stores the first name
                m=new String(mcopy);//stores the message sent
                
                try 
                {
                    //iterates through all the client sockets
                    for(Socket c : name1.values())
                    {
                        if(c!=client)
                        {
                            PrintWriter cout=new PrintWriter(c.getOutputStream(),true);
                            //prints text after client connects and send first message
                            cout.println(u + " has just entered the chat!!!"+m);
                        }
                    }
                } 
                catch (Exception e) 
                {
                    System.out.println(e.getMessage());
                }
            }
            //Sends regular messages that do not contain '/' format
            else
            {
                try 
                {
                    for(Socket c : name1.values())
                    {
                        if(c!=client)
                        {
                            PrintWriter cout=new PrintWriter(c.getOutputStream(),true);
                            //prints text message to all other client
                            cout.println(u+ ": " + s);
                        }
                    }
                } 
                catch (Exception e) 
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
}

