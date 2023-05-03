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
        }
        catch(IOException e) {
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
            }
        }
    }
    
    class SendThread implements Runnable {
        
        public void run() {
            try {
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
                
                while(true) {
                    String message = consoleInput.readLine();
                    
                    sendMessage(message);
                }
            }
            catch(IOException e) {
            }
            finally {
                try {
                    socket.close();
                }
                catch(IOException e) {
                }
            }
        }
    }
}
