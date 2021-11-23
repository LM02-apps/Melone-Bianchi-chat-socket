package com.melonebianchi.chatsocket.server;

public class MainServer
{    public static void main(String args[])
    {
       //Inizializzazione server = new Inizializzazione();
       ServerAccept server = new ServerAccept(6778);

       server.Start();
       
    }
}
