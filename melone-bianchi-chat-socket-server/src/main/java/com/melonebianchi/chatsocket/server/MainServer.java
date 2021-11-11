package com.melonebianchi.chatsocket.server;

public class MainServer
{    public static void main(String args[])
    {
        
        int porta = 6778;      //Variabile di input per la porta del Server
        
        ServerAccept server = new ServerAccept(porta);

        server.Start();
    }
}
