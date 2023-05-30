package team.service;

import team.view.ChatView;
import team.shared.Message;
import team.shared.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class Service {
    private User user ;
    private Socket socket1;

    private static HashSet<String> validUsers;
    private boolean receivedValidUsers = false;
    public boolean isReceivedValidUsers(){
        return receivedValidUsers;
    }
    public void setReceivedValidUsers(boolean res) {
        this.receivedValidUsers = res;
    }
    public HashSet getvalidUsers() {
        return this.validUsers;
    }
    public void setValidUsers(String[] validUsersList){
        this.validUsers = new HashSet<>();
        for (String s: validUsersList) {
            this.validUsers.add(s);
        }
    }
    public User getUser() {
        return user;
    }

    public Service() {
        user = new User();
    }
    public void writeObj(Message message) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket1.getOutputStream());
        objectOutputStream.writeObject(message);
    }
    public void sendFileToOne(String src, String dest,String receiverID) throws IOException {
        if (receiverID.equals("") || receiverID.equals("textField1")) {
            ChatView.getTextArea().append(new java.util.Date().toString()+"\n");
            ChatView.getTextArea().append("Please enter right receiver ID in textField1\n\n");
        } else { //Send file to one user.

            //Create a message and initialize it.
            Message message = new Message();
            message.setType("file");
            message.setSender(user.getUserID());
            message.setReceiver(receiverID);
            message.setSource(src);
            message.setDest(dest);
            message.setSentTime(new java.util.Date().toString());
            byte[] bytes = new byte[(int) new File(src).length()];
            message.setFileBytes(bytes);

            //Read the data to FileInputStream.
            FileInputStream fileInputStream = new FileInputStream(src);
            fileInputStream.read(bytes);

            //Send message.
            writeObj(message);

            //Update GUI.
            ChatView.getTextArea().append(message.getSentTime()+"\n");
            ChatView.getTextArea().append(user.getUserID() + " sends file to " + receiverID + "\n\n");
        }

    }
    public void sendMsgToOne(String content,String receiver) throws IOException {
        if (receiver.equals("")|| receiver.equals("textField1")) {
            ChatView.getTextArea().append(new java.util.Date().toString()+"\n");
            ChatView.getTextArea().append("Please enter right receiver ID in textField1\n\n");
        } else {
            //Send message to one user.
            Message message = new Message();
            message.setSentTime(new java.util.Date().toString());
            message.setContent(content);
            message.setType("userToOne");
            message.setReceiver(receiver);
            message.setSender(user.getUserID());
            //Initialize the message.

            writeObj(message);
            //Send the message

            ChatView.getTextArea().append(message.getSentTime()+"\n");
            ChatView.getTextArea().append(user.getUserID() + " says "+ content +" to "+ receiver +"\n\n");
            //Update GUI.
        }
    }
    public void sendMsgToAll(String content) throws IOException {
        //Send message to all users.
        Message message = new Message();
        message.setContent(content);
        message.setType("userToAll");
        message.setSentTime(new java.util.Date().toString());
        message.setSender(user.getUserID());
        //Initialize the message.

        writeObj(message);
        //Write message to stream.

        ChatView.getTextArea().append(message.getSentTime()+"\n");
        ChatView.getTextArea().append(user.getUserID() + " says "+content+" to All\n\n");
        //Update GUI.
    }
    public void logout() throws IOException {
        //Log out, and update the Server's Thread Pool of Clients.

        Message message = new Message();
        message.setType("logOut");
        message.setSender(user.getUserID());
        message.setSentTime(new java.util.Date().toString());
        //Initialize the Message.

        writeObj(message);
        //Write message into stream.

        ChatView.getTextArea().append(message.getSentTime()+"\n");
        ChatView.getTextArea().append("User "+user.getUserID() +" exits.\n\n");
        //Update the GUI.

        System.exit(0);
        //Kill all threads.
    }

    //Get online user list from Server.
    public void getOnlineList() throws IOException {
        //Initialize the message.
        Message message = new Message();
        message.setType("getList");
        message.setSender(user.getUserID());
        //Write message into stream._
        writeObj(message);
    }

    //Get valid user List from server.
    public void getUserList() throws IOException {
        //Initialize the message
        Message message = new Message();
        message.setType("getUserList");
        message.setSender(user.getUserID());
        // Write message into stream
        writeObj(message);
    }

    //Validate userID and password.
    public boolean validateUser(String userName, String password) throws IOException, ClassNotFoundException {

        //Initialize User object.
        user.setUserID(userName);
        user.setUserPassword(password);
        //Create a socket, using port 8888 and address localhost
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);
        //Send user obj to server.
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(user);

        //Get user obj from server, and check whether logging in is successful.
        ObjectInputStream objectInputStream1 = new ObjectInputStream(socket.getInputStream());
        Message message1 = (Message) objectInputStream1.readObject();

        if(message1.getType().equals("LogIn")){
            socket1 = socket;
            //Display the tutorial of Chat Room.
            ChatView.getTextArea().append("-------------------------------------------------------1. Display online user list.-------------------------------------------------------\n");
            ChatView.getTextArea().append("option -> 1 , textField1 -> null , textField2 -> null, textField3 -> null\n");
            ChatView.getTextArea().append("\n----------------------------------------------------2. Forward message to all users.-------------------------------------------------\n");
            ChatView.getTextArea().append("option -> 2 , textField1 -> null , textField2 -> null, textField3 -> null\n");
            ChatView.getTextArea().append("\n-----------------------------------------------------3. Send message to one user.----------------------------------------------------\n");
            ChatView.getTextArea().append("option -> 3 , textField1 -> receiverID , textField2 -> null, textField3 -> null\n");
            ChatView.getTextArea().append("\n--------------------------------------------------------------4. Send files.-----------------------------------------------------------------\n");
            ChatView.getTextArea().append("option -> 4 , textField1 -> receiverID, textField2 -> source path , textField3 -> destination path\n");
            ChatView.getTextArea().append("\n-------------------------------------------------------5. Display user list.------------------------------------------------------------\n");
            ChatView.getTextArea().append("option -> 5 , textField1 -> null , textField2 -> null, textField3 -> null\n\n");
            while(true) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Message message = (Message) objectInputStream.readObject();
                    switch (message.getType()) {
                        case "returnList" -> {
                            String[] msg = message.getContent().split(", ");
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            for (String s : msg) {
                                ChatView.getTextArea().append("Online User: " + s + "\n");
                            }
                            ChatView.getTextArea().append("\n");
                            break;
                        }
                        case "returnUserList" -> {
                            String[] users = message.getContent().split(" ");
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            for (String s : users) {
                                ChatView.getTextArea().append("Valid User: " + s + "\n");
                            }
                            setValidUsers(users);
                            ChatView.getTextArea().append("\n");
                            setReceivedValidUsers(true);
                            break;
                        }
                        case "InvalidReceiver" -> {
                            ChatView.getTextArea().append(new java.util.Date().toString()+"\n");
                            ChatView.getTextArea().append(message.getContent());
                            break;
                        }
                        case "userToOne" -> {
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            ChatView.getTextArea().append(message.getSender() + " sends " + message.getContent() + " to " + message.getReceiver() + "\n\n");
                            break;
                        }

                        case "userToAll" -> {
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            ChatView.getTextArea().append(message.getSender() + " sends " + message.getContent() + " to all\n\n");
                            break;
                        }
                        case "serverToAll" -> {
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            ChatView.getTextArea().append(message.getSender() + " broadcasts " + message.getContent() + " to all\n\n");
                            break;
                        }
                        case "file" -> {
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            ChatView.getTextArea().append(message.getSender() + " sends file to " + message.getReceiver() + "\n");
                            FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                            fileOutputStream.write(message.getFileBytes());
                            fileOutputStream.close();
                            ChatView.getTextArea().append("File saved\n\n");
                            break;
                        }
                        default -> {
                            ChatView.getTextArea().append(message.getSentTime() + "\n");
                            ChatView.getTextArea().append("Unknown message type\n\n");
                            break;
                        }

                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        else {
            return false;
        }
    }

    //validate receiver userID
}
