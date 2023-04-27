package chatclient;

import java.net.*;
import java.io.*;

public class ChatClient {
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    
    public ChatClient() {
    }
    
    public void run() {
        
        try {
            socket = new Socket("localhost", 1234); //server Ip, server port number
//            System.out.println("Connected to server " + "localhost" + ": " + 1234);           
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            
            // Create a thread to receive messages from the server
            Thread receiveThread = new Thread(new ReceiveThread());
            receiveThread.start();
            
            // Create a thread to send messages to the server
            Thread sendThread = new Thread(new SendThread());
            sendThread.start();
            
        }
        catch(UnknownHostException e) {
//            System.out.println("Error connecting to server: " + e.getMessage());
        }
        catch(IOException e) {
//            System.out.println("Error connecting to server: " + e.getMessage());
        }
    }
    
    public void sendMessage(String message) {
        output.println(message);
    }
    
    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.run();
    }
    
    class ReceiveThread implements Runnable {
        
        public void run() {
            try {
                while(true) {
                    String message = input.readLine();
                    
                    if(message == null) {
                        break;
                    }
                    
                    System.out.println(message);
                }
            }
            catch(IOException e) {
//                System.out.println("Error receiving message: " + e.getMessage());
            }
        }
    }
    
    class SendThread implements Runnable {
        
        public void run() {
            try {
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                
                while(true) {
                    String message = consoleInput.readLine();
                    
//                    if(message.equals("/disconnect")) {
//                        break;
//                    }
                    
                    sendMessage(message);
                }
            }
            catch(IOException e) {
//                System.out.println("Error sending message: " + e.getMessage());
            }
            finally {
                try {
                    socket.close();
                }
                catch(IOException e) {
//                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }
    }
}
