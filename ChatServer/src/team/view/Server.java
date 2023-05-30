package team.view;

import team.service.ServerConnectionThread;
import team.shared.Message;
import team.shared.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
public class Server {
    private static HashMap<String, User> usersDatabase = new HashMap<>();
    //here is the default username and password!!!!
    static {
        usersDatabase.put("1",new User("1","1"));
        usersDatabase.put("2",new User("2","2"));
        usersDatabase.put("3",new User("3","3"));
        usersDatabase.put("4",new User("4","4"));
    }
    //DataBase
    public static HashMap<String, User> getUsersDatabase() {
        return usersDatabase;
    }

    private static ServerView serverVIew = new ServerView();
    public static ServerView getServerVIew() {
        return serverVIew;
    }

    //create a hash map, stores the offline message.
    private static HashMap<String, ArrayList<Message>> offlineMessages = new HashMap<>();

    public static void storeMessage(String receiver, Message message){
        if (offlineMessages.containsKey(receiver)) {
            offlineMessages.get(receiver).add(message);
        } else {
            ArrayList<Message> messages = new ArrayList<>();
            messages.add(message);
            offlineMessages.put(receiver,messages);
        }
    }

    private static HashMap<String,ServerConnectionThread> onlineClients = new HashMap<>();
    public static void addOnlineClients(String uid, ServerConnectionThread serverConnectionThread){
        onlineClients.put(uid, serverConnectionThread);
    }
    public static ServerConnectionThread getOnlineClients(String uid){

        return onlineClients.get(uid);
    }
    public static void deleteOnlineClients(String uid){
        onlineClients.remove(uid);
    }

    public static HashMap<String,ServerConnectionThread> getOnlineClients(){
        return onlineClients;
    }

    public Server() throws IOException, ClassNotFoundException {
        //create a server socket.
        ServerSocket serverSocket = new ServerSocket(8888);

        while (true){
            //Keep listening to clients.
            Socket socket = serverSocket.accept();
            //Accept sockets request from client.
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            User user = (User)objectInputStream.readObject();
            //Receive a user class, and validate it.
            Date now= new Date();
            boolean userIDExist = usersDatabase.containsKey(user.getUserID());
            boolean login = false;
            if (userIDExist) {
                String storedPassword = usersDatabase.get(user.getUserID()).getUserPassword();
                String inputPassword = user.getUserPassword();
                if (storedPassword.equals(inputPassword)) {
                    login = true;
                }
            }
            if (userIDExist && login) {
                //update serverView
                serverVIew.getTextArea().append(new java.util.Date().toString()+"\n");
                serverVIew.getTextArea().append("user " + user.getUserID() + " logs in.\n\n");

                //create user login message.
                Message message = new Message();
                message.setType("LogIn");
                message.setSentTime(now.toString());
                objectOutputStream.writeObject(message);

                //create a thread and keep connection with socket
                //Function: Connection
                ServerConnectionThread serverConnectionThread = new ServerConnectionThread(socket, user.getUserID());
                serverConnectionThread.start();
                addOnlineClients(user.getUserID(), serverConnectionThread);
                //Put this client thread into thread pool.

                //Send offline message to current user.
                if(offlineMessages.containsKey(user.getUserID())){
                    for(Message msg : offlineMessages.get(user.getUserID())){
                        ObjectOutputStream objectOutputStream1 = new ObjectOutputStream(getOnlineClients(user.getUserID()).getSocket().getOutputStream());
                        objectOutputStream1.writeObject(msg);
                    }
                    offlineMessages.remove(user.getUserID());
                }
            } else if (!userIDExist) {
                //create user login fail message.
                Message message = new Message();
                message.setType("user is not existed");
                message.setSentTime(now.toString());
                objectOutputStream.writeObject(message);

                serverVIew.getTextArea().append(now.toString() + "\n");
                serverVIew.getTextArea().append("Input user id is:" + user.getUserID() + ".\n");
                serverVIew.getTextArea().append("This user id is not exist.\n");
                socket.close();
            } else { // userID exists, wrong password
                //create user login fail message.
                Message message = new Message();
                message.setType("wrong password");
                message.setSentTime(now.toString());
                objectOutputStream.writeObject(message);

                serverVIew.getTextArea().append(now.toString() + "\n");
                serverVIew.getTextArea().append("Input user id is:" + user.getUserID() + ".\n");
                serverVIew.getTextArea().append("Wrong password!.\n");
                socket.close();
            }
        }

    }
    public static void main(String[] args) {
        try {
            Server server = new Server();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
