import java.net.*;
import java.io.*;
import static java.lang.System.exit;
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
    public String username, username_check, password, password_check;
    Hashtable<String, String> user_pass;
    Boolean ok, ok1;
    
    public ClientThread(Socket clientSocket, ChatServer server) {
        this.user_pass = new Hashtable<>();
        
        try {
            this.clientSocket = clientSocket;
            this.server = server;
            
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            
            output.println("Please enter a username: ");
            username = input.readLine(); //set nickanme
            output.println("Please enter a password(must contain at least one special character,(e.g. !@#_): ");
            password = input.readLine(); //set password
            ok = false;
            ok1 = true;
            while(ok == false){
            for(int i = 0; i< password.length(); i++){ //advanced feature: password policy check
                if(Character.isLetterOrDigit(password.charAt(i)) == false){ 
                    ok1 = false;
                }
            }
            if(ok1){
                output.println("Password doesn't meet requirements. Try again.");
                output.println("Please enter a username: ");
                username = input.readLine(); //set nickanme
                output.println("Please enter a password(must contain at least one special character,(e.g. !@#_): ");
                password = input.readLine(); //set password
            }
            else{
                user_pass.put(username, password);
                ok = true;
            }
            }
            output.println("Registration Successful");
            output.println("Please enter username to authentificate: ");
            username_check = input.readLine();
            output.println("Please enter password to authentificate: ");
            password_check = input.readLine();
            while(true){
                if(user_pass.containsKey(username_check) && user_pass.containsValue(password_check)){
                    output.println("You are authenticated, Welcome " + username);
                    break;
                    
           }
           else{
               output.println("Please enter correct username and/or password");
               output.println("Please enter username to authentificate: ");
               username_check = input.readLine();
               output.println("Please enter password to authentificate: ");
               password_check = input.readLine();
           }
           }
        }
        catch(IOException e) {
        }
    }
    
}

