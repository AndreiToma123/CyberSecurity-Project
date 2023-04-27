package chatserver;

import java.net.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

public class ChatServer {
    ServerSocket serverSocket;
    ClientThread[] clients = new ClientThread[100];
    int clientCount = 0;
    
    public ChatServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while(true) {
                
                Socket clientSocket = serverSocket.accept(); //waits for a connection and accepts it
                ClientThread clientThread = new ClientThread(clientSocket, this);
                clients[clientCount] = clientThread;
                clientCount++;
                clientThread.start();
            }
        }
        catch(Exception e) {
//            System.out.println("Error starting server on port " + port + ": " + e.getMessage());
//              System.out.println("Maximum number of users reached.");
        }
    }
    
//    public synchronized void broadcast(String message) {
//        
//        for(int i = 0; i < clientCount; i++) {
//            clients[i].sendMessage(message);
//        }
//    }
    
    public synchronized void removeClient(ClientThread clientThread) {
        int index = -1;
        for(int i = 0; i < clientCount; i++) {
            if(clients[i] == clientThread) {
                index = i;
                break;
            }
        }
        if(index != -1) {
            for(int i = index; i < clientCount - 1; i++) {
                clients[i] = clients[i+1];
            }
            clientCount--;
        }
    }
    
    public static void main(String[] args) {
        ChatServer server = new ChatServer(1234);
    }
}

class ClientThread extends Thread {   //handles communication between a client and the server
    Socket clientSocket;  
    BufferedReader input;
    PrintWriter output;   
    ChatServer server;   
    String username, username_check, password, password_check;
    Hashtable<String, String> user_pass;
    
    public ClientThread(Socket clientSocket, ChatServer server) {
        this.user_pass = new Hashtable<>();
        
        try {
            this.clientSocket = clientSocket;
            this.server = server;
            
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            
            output.println("Please enter a username: ");
            username = input.readLine(); //set nickanme
            output.println("Please enter a password: ");
            password = input.readLine(); //set password
            user_pass.put(username, password);
            output.println("‘Registration Successful");
            output.println("Please enter username to authentificate: ");
            username_check = input.readLine();
            output.println("Please enter password to authentificate: ");
            password_check = input.readLine();
           if(user_pass.containsKey(username_check) && user_pass.containsValue(password_check)){
               output.println("You are authenticated, Welcome " + username);
           }
           else{
               output.println("Please enter correct username password");
               cleanup();
           }
        }
        catch(IOException e) {
//            System.out.println("Error creating client thread: " + e.getMessage());
        }
    }
    
//    public void sendMessage(String message) {
//        output.println(message);
//    }
    
//    public void run() {
//        
//        String message;
//        
//        try {
//        while ((message = input.readLine()) != null) {
//           if (message.startsWith("/msg ")) { //send private message
//                handlePrivateMessage(message);
//            } else {
//                server.broadcast("[" + nickname + "]: " + message);
//            }
//        }
//    } catch (IOException e) {
////        System.out.println("Error reading from client: " + e.getMessage());
//    } finally {
//        cleanup();
//    }
//}
//    public void handlePrivateMessage(String message) {
//        String[] parts = message.substring(5).split(" ", 2);
//        boolean foundRecipient = false;
//        String recipient = parts[0];
//        String msg = parts[1];
//
//        if (parts.length != 2) {
//            output.println("Invalid message format");
//        }


//        for (ClientThread i : server.clients) {
//            if (i != null && i.nickname.equals(recipient)) {
//                i.sendMessage("[Private message from " + nickname + "]: " + msg);
//                foundRecipient = true;
//                break;
//            }
//        }

//        if (foundRecipient == false) {
//            output.println("Recipient not found");
//        }
    public void cleanup() {
            server.removeClient(this);
    //        server.broadcast(nickname + " left the chat");

            try {
                clientSocket.close();
            } catch (IOException e) {
    //            System.out.println("Error closing client socket: " + e.getMessage());
            }
    }

}
    
    //    }
