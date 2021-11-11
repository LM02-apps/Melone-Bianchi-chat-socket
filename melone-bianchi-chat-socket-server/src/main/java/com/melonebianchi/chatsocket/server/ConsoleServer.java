package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class ConsoleServer extends Thread
{
    ServerSocket server;
    ListaClient lista;
    BufferedReader tastiera;
    String comando;
    String messaggio;
    boolean stop = true;

    public ConsoleServer(ServerSocket server, ListaClient lista)
    {
        this.server = server;
        this.lista = lista;
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        Thread.currentThread().setName("Server");
    }

    public void run()
    {
        try
         {
             for(;;)
             {
                 System.out.print("Server:$");
                 comando = tastiera.readLine();
              
                if(comando.equals("/list"))
                {
                    System.out.println("La Lista E' La Seguente: " + lista.ListaUtentiServer());
                }
                else if(comando.equals("/all"))
                {
                    System.out.println("Inserire Messaggio Da Inviare Ai Client");
                    messaggio = tastiera.readLine();
                    lista.InvioTotale("Server", messaggio);
                }
                else if(comando.equals("/clear"))
                {
                    for(int i = 0; i < 30; i++)
                        System.out.println("\n");
                }
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void setSTOP(boolean stop)
    {
        this.stop = stop;
    }

    public boolean isSTOP()
    {
        return stop;
    }
}
