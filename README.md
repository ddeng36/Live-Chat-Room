
# Chat Application
I create this chat app by my self.This repository contains a chat application implemented in Java. The application allows multiple clients to connect to a server and exchange messages in real-time. Below you will find instructions on how to run the application and a brief overview of the file structure.
# Features
  - ### Client Features
    1. Log in with username and password: Users can log in to the app by entering their username and password.

    2. Invalid login handling: If the entered username and password are invalid, a pop-up window will display an error message, prompting the user to re-enter the credentials.

    3. Display online user list: The app provides a list of currently online users, allowing clients to see who is available for communication.
       
    4. Broadcast message to all users: Clients can send a message that will be broadcasted to all online users.
       
    5. Validate receiver when sending a message: When a client sends a message to another client, the app validates the receiver's identity to ensure the message is delivered to the correct recipient.
       
    6. Send a message to one user: Clients can send private messages to a specific user.
       
    7. Display messages on recipient's window: If the recipient is online, the message will be displayed on their GUI window.
       
    8. Offline message handling: If the recipient is offline, the message will be stored on the server, and the recipient will receive it upon their next login.
       
    9. Send files to one user: Clients can send files to specific users.
    
    10. Display registered users: The app provides a list of all registered users.
        
    11. Log out: Clients can log out from the app by closing the window, which triggers a disconnection message sent to the server.
        
    12. Display chat history: The client's GUI window displays the chat history, allowing users to view past conversations.

  - ### Server Features
    1. Username and password validation: The server maintains a database of username and password data and validates the credentials provided by clients during login.
     
    2. Thread pool management: The server manages a thread pool and creates a new thread for each client that connects to the server.
    
    3. Single-server Multi-client: The server can handle multiple client connections simultaneously.
    
    4. Message forwarding: If the recipient is online, the server forwards the message to the intended receiver.
    
    5. Offline message storage: If the recipient is offline, the server stores the message and delivers it upon the recipient's next login.
    
    6. Display journal file: The server's GUI window displays a journal file that contains relevant information and logs.
    
    7. These features collectively enable communication and interaction between clients through the server while maintaining data integrity and providing a seamless user experience.
## Instructions
- Videos instructions are in the Videos folder.
- Detailed text report is in the root folder
- To run the chat application, please follow these steps:
  1. Run the ChatServer/team/view/Server class first to start the server. This will act as a single-server for handling client connections.
  2. Run the ChatClient/team/view/Start class to start the client application. You can run multiple instances of the client in different IDE windows to simulate multiple clients connecting to the server.
     - User and Password are in ChatServer/team/view/Server.java
  3. Then the detailed tutorials for the server and client will be displayed separately on the graphical user interface (GUI).

## File Descriptions
- Server
  - ChatServer/team/service/ServerConnectionThread.java: This class enables the server to handle multiple client connections concurrently.

  - ChatServer/team/view/ServerView.java: This class represents the GUI of the server.

  - ChatServer/team/view/Server.java: This class is responsible for broadcasting messages, managing user database, and handling online user management.

- Client

  - ChatClient/team/shared/User.java and ChatServer/team/shared/User.java: These classes define the user model used by both the client and the server.
  
  - ChatClient/team/shared/Message.java and ChatServer/team/shared/Message.java: These classes define the message model used by both the client and the server.
  
  - ChatClient/team/service/Service.java: This class provides functionality for logging in, logging out, sending/receiving messages and files to/from the server.
  
  - ChatClient/team/view/ChatView.java: This class represents the GUI of the client.
  
  - ChatClient/team/view/Start.java: This class is used to start the client application.

## License
  - This project is licensed under the MIT License. Feel free to use and modify the code as per the terms of the license.

## Acknowledgements
  - The implementation of this chat application was inspired by various online resources and tutorials. We would like to acknowledge the contribution of these resources in developing this project.