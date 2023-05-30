package team.view;

import team.shared.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

import static team.view.Server.getOnlineClients;


public class ServerView extends JFrame{
    private static JTextArea textArea;
    //We may update textArea in another class, so we made it static.

    private JTextArea inputArea;
    //Area to hold the input.

    private JScrollPane scrollPane;
    //Enable input Area to scroll when there are too many massages.

    private JButton sendBtn;

    private JLabel serverInfo;

    public static JTextArea getTextArea() {
        //This is for updating textArea.
        return textArea;
    }

    protected void processWindowEvent(WindowEvent e) {
        //Log out when closing the window.
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    public void initialGUI() {
        //Set basic window.
        setTitle("ServerGUI");
        setSize(600, 600);
        setResizable(true);

        //Generate a text area.
        textArea = new JTextArea();
        textArea.setBackground(new Color(238, 207, 207, 255));
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Displays current server.
        serverInfo = new JLabel("Server", SwingConstants.CENTER);

        //Container, which holds send btn.
        inputArea = new JTextArea(5, 25);
        sendBtn = new JButton("Send message");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        //A send button.
        bottomPanel.add(sendBtn);

        //Generate a bottom area.
        JPanel bottomArea = new JPanel(new BorderLayout());
        bottomArea.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        bottomArea.add(bottomPanel, BorderLayout.SOUTH);

        //Link text area, user info and bottom area to the base window.
        add(serverInfo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomArea, BorderLayout.SOUTH);

        //Display the window.
        setVisible(true);
    }
    public ServerView(){
        initialGUI();
        setBtnListener();
        ServerView.getTextArea().append("---------------------------------------------------You can send message to all users.---------------------------------------------------\n\n");
    }

    public  void setBtnListener(){
        //Create a event listener to send button.
        sendBtn.addActionListener(e -> {
            Message message = new Message();
            message.setSender("server");
            message.setType("serverToAll");
            message.setSentTime(new java.util.Date().toString());
            message.setSentTime(new Date().toString());
            message.setContent(inputArea.getText());
            //Create a new message

            textArea.append(new java.util.Date().toString()+"\n");
            textArea.append("Server says "+ inputArea.getText() + " to All.\n\n");
            inputArea.setText("");
            //Read input area and clear it.

            for(String user : Server.getUsersDatabase().keySet()){
                //Broadcast message to all users.
                if(getOnlineClients().containsKey(user) ){
                    if(!user.equals(message.getSender())){
                        ObjectOutputStream objectOutputStream = null;
                        try {
                            objectOutputStream = new ObjectOutputStream(getOnlineClients().get(user).getSocket().getOutputStream());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        try {
                            objectOutputStream.writeObject(message);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }else {
                    Server.storeMessage(user,message);
                }
            }
        });
    }


}
