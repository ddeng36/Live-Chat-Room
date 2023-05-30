package team.view;

import team.service.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ChatView extends JFrame {
    private static JTextArea textArea;
    //We may update textArea in another class, so we made it static.
    private JTextArea inputArea;
    //Area to hold the input.

    private JScrollPane scrollPane;
    private JButton sendBtn;

    private JTextField option;
    //Option decides the message type.

    private JTextField textField1;

    private JTextField textField2;

    private JTextField textField3;

    private JLabel userInfo;

    private Service service;
    //Services for user.

    public static void setTextArea(JTextArea textArea) {
        ChatView.textArea = textArea;
    }

    public JTextArea getInputArea() {
        return this.inputArea;
    }

    public void setInputArea(JTextArea inputArea) {
        this.inputArea = inputArea;
    }

    public JScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public JButton getSendBtn() {
        return this.sendBtn;
    }

    public void setSendBtn(JButton sendBtn) {
        this.sendBtn = sendBtn;
    }

    public JTextField getOption() {
        return this.option;
    }

    public void setOption(JTextField option) {
        this.option = option;
    }

    public JTextField getTextField1() {
        return this.textField1;
    }

    public void setTextField1(JTextField textField1) {
        this.textField1 = textField1;
    }

    public JTextField getTextField2() {
        return this.textField2;
    }

    public void setTextField2(JTextField textField2) {
        this.textField2 = textField2;
    }

    public JTextField getTextField3() {
        return this.textField3;
    }

    public void setTextField3(JTextField textField3) {
        this.textField3 = textField3;
    }

    public JLabel getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(JLabel userInfo) {
        this.userInfo = userInfo;
    }

    public Service getService() {
        return this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }



    public static JTextArea getTextArea() {
        //This is for updating textArea.
        return textArea;
    }
    protected void processWindowEvent(WindowEvent e) {
        //Log out when closing the window.
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            try {
                service.logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //Initialize the GUI structure.
    public void initialGUI(){

        //Set basic window.
        setTitle("ChatGUI");
        setSize(600, 600);
        setResizable(true);

        //Generate a text area.
        textArea = new JTextArea();
        textArea.setBackground(new Color(232, 229, 229, 255));
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //Displays current user ID.
        userInfo = new JLabel("Please enter userID and password", SwingConstants.CENTER);

        //Container, which holds above 4 JTextFields
        inputArea = new JTextArea(5, 25);
        option = new JTextField("option",5);
        textField1 = new JTextField("textField1",10);
        textField2 = new JTextField("textField2",10);
        textField3 = new JTextField("textField3",10);
        sendBtn = new JButton("Send message");
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        bottomPanel.add(option);
        bottomPanel.add(textField1);
        bottomPanel.add(textField2);
        bottomPanel.add(textField3);
        bottomPanel.add(sendBtn); //A send button.
        //Container, which holds JPanel bottomPanel.
        JPanel bottomArea = new JPanel(new BorderLayout());
        //Generate a bottom area.
        bottomArea.add(new JScrollPane(inputArea), BorderLayout.CENTER);
        bottomArea.add(bottomPanel, BorderLayout.SOUTH);

        //Link text area, user info and bottom area to the base window.
        add(userInfo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomArea, BorderLayout.SOUTH);

        //Display the window.
        setVisible(true);
    }

    public void setBtnListener(){
        //Create a event listener to send button.
        sendBtn.addActionListener(e -> {
            String opt = option.getText();
            switch (opt) {
                case "1": {
                    //-------------------------------------------------------1. Display online user list.-------------------------------------------------------
                    //option -> 1 , textField1 -> null , textField2 -> null, textField3 -> null);
                    try {
                        service.getOnlineList();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
                case "2":
                {
                    //------------------------------------------------------2. Forward message to all users.----------------------------------------------------
                    //option -> 2 , textField1 -> null , textField2 -> null, textField3 -> null);
                    try {
                        service.sendMsgToAll(inputArea.getText());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
                case "3":
                {
                    //-------------------------------------------------------3. Send message to one user.-------------------------------------------------------
                    //option -> 3 , textField1 -> receiverID , textField2 -> null, textField3 -> null);
                    String inputId = textField1.getText();
                    String inputMsg = inputArea.getText();
                    try {
                        service.sendMsgToOne(inputMsg, inputId);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
                case "4": {
                    //--------------------------------------------------------------4. Send files.--------------------------------------------------------------
                    //option -> 4 , textField1 -> receiverID, textField2 -> source path , textField3 -> destination path);
                    String receiverId = textField1.getText();
                    String inputSrc = textField2.getText();
                    String inputDst = textField3.getText();

                    try {
                        service.sendFileToOne(inputSrc, inputDst, receiverId);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
                case "5": {
                    //-------------------------------------------------------5. Display user list.-------------------------------------------------------
                    //option -> 5 , textField1 -> null , textField2 -> null, textField3 -> null);
                    try {
                        service.getUserList();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
                default:
                {
                    ChatView.getTextArea().append("Wrong option, pleas check again.\n\n");
                    ChatView.getTextArea().setCaretPosition(ChatView.getTextArea().getText().length());
                    break;
                }
            }
        });
    }

    public void initialService() throws IOException, ClassNotFoundException {
        while (true){
            service = new Service();
            String userName = JOptionPane.showInputDialog(this,"Please enter user ID", "Log in (1/2)", JOptionPane.QUESTION_MESSAGE);
            String password = JOptionPane.showInputDialog(this,"Please enter pass words", "Log in (2/2)", JOptionPane.QUESTION_MESSAGE);
            //Initial a User and it's service class.
            if (!service.validateUser(userName, password)) {
                //Validate whether user log in successfully.
                JOptionPane.showMessageDialog(this, "UserID or Password is false!");
            }
            else {
                userInfo.setText("Current User : "+userName);
                break;
            }
        }
    }

    public ChatView() throws IOException, ClassNotFoundException {
        //Constructor of ChatView.
        initialGUI();
        setBtnListener();
        initialService();
    }
}
