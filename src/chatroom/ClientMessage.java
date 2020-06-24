/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatroom;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author shelbyetienne
 */
public class ClientMessage 
{
    public static void main(String [] args) throws Exception
    {
        Socket server=null;
        Scanner input=null;
        PrintWriter output=null;
        Scanner keyBoard=null;
        
        try
        {
            server=new Socket("LocalHost", 1234);
            input=new Scanner(server.getInputStream());
            
            output=new PrintWriter(server.getOutputStream(), true);
            keyBoard=new Scanner(System.in);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        
        new OutputThread(output, keyBoard, server).start();
        new InputThread(input).start();
    }
}


class OutputThread extends Thread
{
    private PrintWriter output;
    private Scanner keyBoard;
    private Scanner input;
    private Socket client;
    
    /**
     * Explicit Constructor
     * @param out sends out messages
     * @param kb will scan for text typed by keyboard
     * @param c the clients socket
     */
    public OutputThread(PrintWriter out, Scanner kb, Socket c)
    {
        output=out;
        keyBoard=kb;
        client=c;
    }
    
    public void run()
    {
        try
        {
            while(true)
            {
                String s= keyBoard.nextLine();
                output.println(s);
                
                if(s.equals("bye!!"))
                {
                    output.close();
                    client.close();
                    break;
                }
            }
        }catch(Exception e)
        {
            return;
        }
    }
}

class InputThread extends Thread
{
    private Scanner input;
    
    /**
     * Explicit Constructor
     * @param in scans for text from client
     */
    public InputThread(Scanner in){input=in;}
    
    public void run()
    {
        try
        {
            while(true)
            {
                String s="null";
                s=input.nextLine();
                System.out.println(s);
            }
        }
        catch(Exception e)
        {
            return;
        }
    }
}