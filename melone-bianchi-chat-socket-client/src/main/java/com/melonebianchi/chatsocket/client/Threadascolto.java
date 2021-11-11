package com.melonebianchi.chatsocket.client;

import java.io.DataOutputStream;


import java.io.*;
import java.net.*;

public class Threadascolto extends Thread
{
    private BufferedReader inserver;
    private String rispostaserver;
    DataOutputStream outserver;
    Socket socketclient;
    public Threadascolto(BufferedReader inserver, DataOutputStream outserver, Socket socketclient) {
        this.inserver = inserver;
        this.outserver=outserver;
        this.socketclient=socketclient;
    }

    public void run()
    {
        
        try
        {
            for(;;)
            {
               
                rispostaserver=inserver.readLine();

                if (rispostaserver.contains("END"))
                {
                    System.out.println("Chiusura connessione...")
                    inserver.close();
                    outserver.close();
                    socketclient.close();
                    break;
                }
                
                System.out.println(rispostaserver);

            }
            
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    
}
