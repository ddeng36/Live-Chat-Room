package team.service;
import java.io.*;
import java.net.Socket;

import team.shared.Message;
import team.view.Server;

import static team.view.Server.*;

public class ServerConnectionThread extends Thread{
    private Socket socket;
    private String uid;

    public ServerConnectionThread(Socket socket, String uid) {
        this.socket = socket;
        this.uid = uid;
    }

    public Socket getSocket() {
        return socket;
    }

    public void run() {
        while (true) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();
                //Get the request message from client.
                String messageType = message.getType();
                switch (messageType) {
                    case "getList" :
                    {
                        //Update server GUI.
                        Server.getServerVIew().getTextArea().append(new java.util.Date().toString() + "\n");
                        Server.getServerVIew().getTextArea().append("User " + message.getSender() + " retrieves online user list.\n\n");

                        String msg = new String();
                        for (String s : getOnlineClients().keySet()) {
                            msg = msg + s + " ";
                        }
                        //Get online user list.

                        Message message1 = new Message();
                        message1.setReceiver(message.getSender());
                        message1.setType("returnList");
                        message1.setContent(msg);
                        message1.setSentTime(new java.util.Date().toString());
                        //Initialize the return message.
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(message1);
                        //Return online user list.
                        break;
                    }

                    case "getUserList": {
                        //Update server GUI.
                        Server.getServerVIew().getTextArea().append(new java.util.Date().toString() + "\n");
                        Server.getServerVIew().getTextArea().append("User " + message.getSender() + " retrieves user list.\n\n");
                        String msg = new String();
                        //Get user list.
                        for (String s : getUsersDatabase().keySet()) {
                            msg = msg + s + " ";
                        }
                        //Initialize the return message.
                        Message message1 = new Message();
                        message1.setReceiver(message.getSender());
                        message1.setType("returnUserList");
                        message1.setContent(msg);
                        message1.setSentTime(new java.util.Date().toString());
                        //Return user list.
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject(message1);

                        break;
                    }
                    case "userToOne": {
                        if (getUsersDatabase().containsKey(message.getReceiver())) {
                            if (getOnlineClients().containsKey(message.getReceiver())) {
                                Server.getServerVIew().getTextArea().append(new java.util.Date().toString() + "\n");
                                Server.getServerVIew().getTextArea().append("User " + message.getSender() + " sends " + message.getContent() + " to " + message.getReceiver() + ".\n\n");
                                //Update server GUI.
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(getOnlineClients(message.getReceiver()).getSocket().getOutputStream());
                                objectOutputStream.writeObject(message);
                                //Forward message.
                            } else {
                                //Store the messages to database.
                                Server.getServerVIew().getTextArea().append(new java.util.Date().toString() + "\n");
                                Server.getServerVIew().getTextArea().append("User " + message.getSender() + " sends " + message.getContent() + " to " + message.getReceiver() + ".\n\n");
                                //Update server GUI.
                                Server.storeMessage(message.getReceiver(), message);
                            }
                        } else {
                            Server.getServerVIew().getTextArea().append(new java.util.Date().toString() + "\n");
                            Server.getServerVIew().getTextArea().append("User " + message.getSender() + " sends " + message.getContent() + " to " + message.getReceiver() + ".\n");
                            Server.getServerVIew().getTextArea().append(message.getReceiver() + " is not existed.\n\n");
                            Message message1 = new Message();
                            message1.setReceiver(message.getSender());
                            message1.setType("InvalidReceiver");
                            message1.setContent("Failed, " +message.getReceiver() + " is not existed\n\n");
                            message1.setSentTime(new java.util.Date().toString());
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(message1);
                        }
                        break;
                    }
                    case "userToAll":
                    {
                        Server.getServerVIew().getTextArea().append(new java.util.Date().toString()+"\n");
                        Server.getServerVIew().getTextArea().append("User "+ message.getSender() + " says " + message.getContent() + " to all users.\n\n");
                        //Update server GUI.
                        for (String user : Server.getUsersDatabase().keySet()){
                            if (getOnlineClients().containsKey(user) ){
                                if (!user.equals(message.getSender())){
                                    //Forward the message to all except sender.
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(getOnlineClients().get(user).getSocket().getOutputStream());
                                    objectOutputStream.writeObject(message);
                                }
                            } else {
                                //Store message to database.
                                Server.storeMessage(user,message);
                            }
                        }
                        break;
                    }

                    case "file":
                    {
                        if (getUsersDatabase().containsKey(message.getReceiver())) {
                            Server.getServerVIew().getTextArea().append(new java.util.Date().toString()+"\n");
                            Server.getServerVIew().getTextArea().append("User "+ message.getSender() + " sends a file to " +message.getReceiver() +".\n\n");
                            //Update server GUI.
                            if(getOnlineClients().containsKey(message.getReceiver())) {
                                //Forward file.
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(getOnlineClients(message.getReceiver()).getSocket().getOutputStream());
                                objectOutputStream.writeObject(message);
                            }else {
                                //Store message to database.
                                Server.storeMessage(message.getReceiver(),message);
                            }
                        } else {
                            Server.getServerVIew().getTextArea().append(new java.util.Date().toString() + "\n");
                            Server.getServerVIew().getTextArea().append("User " + message.getSender() + "wants to sends file to " + message.getReceiver() + ".\n");
                            Server.getServerVIew().getTextArea().append(message.getReceiver() + " is not existed.\n\n");
                            Message message1 = new Message();
                            message1.setReceiver(message.getSender());
                            message1.setType("InvalidReceiver");
                            message1.setContent("Failed, " +message.getReceiver() + " is not existed\n\n");
                            message1.setSentTime(new java.util.Date().toString());
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            objectOutputStream.writeObject(message1);
                        }

                        break;
                    }

                    case "logOut":
                    {
                        Server.getServerVIew().getTextArea().append(new java.util.Date().toString()+"\n");
                        Server.getServerVIew().getTextArea().append("User "+ message.getSender() + " logs out.\n\n");
                        //Update server GUI.
                        deleteOnlineClients(uid);
                        socket.close();
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
