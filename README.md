
# Chat Application
I create this chat app by my self.This repository contains a chat application implemented in Java. The application allows multiple clients to connect to a server and exchange messages in real-time. Below you will find instructions on how to run the application and a brief overview of the file structure.

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

- License
  - This project is licensed under the MIT License. Feel free to use and modify the code as per the terms of the license.

- Acknowledgements
  - The implementation of this chat application was inspired by various online resources and tutorials. We would like to acknowledge the contribution of these resources in developing this project.