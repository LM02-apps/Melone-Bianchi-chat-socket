package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ThreadClient extends Thread
{
    String stringaRicevuta;
    String nomeClient;
    Socket client = null; 
    ListaClient lista;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;    
    boolean ciclo = true;
    final static String messaggioAbbandono = "Ha Abbandonato La Chat";

    public ThreadClient(Socket socket, ListaClient lista, BufferedReader inDalClient, String nomeClient, DataOutputStream outVersoClient) //Costruttore 
    {
        this.client = socket;
        this.lista = lista;
        this.inDalClient = inDalClient;
        this.nomeClient = nomeClient;
        this.outVersoClient = outVersoClient;
    }

    public void run()
    {
        try
        {
            currentThread().setName(nomeClient);
            Comunica(); //Chiamata del metodo
        }
        catch(Exception e)  
        {
           System.out.println("Errore Comunica() Thread");
           try
            {
                ChiudiConnessione();
            }
            catch (IOException e1) 
            {
              e1.printStackTrace();
            }
        }
    }

    public void Comunica() throws IOException
    {

        lista.ListaUtenti();

        for(;ciclo;)
        {
            stringaRicevuta = inDalClient.readLine();
            System.out.println("Il Server Ha Ricevuto Da " + Thread.currentThread().getName() + ": " + stringaRicevuta);  
            
            if(lista.getSize() >= 2 || stringaRicevuta.equals("/exit"))
            {
                Comando();
            }
            else
            {
                outVersoClient.writeBytes("Impossibile Inviare Messagi Se Soli Connessi" + "\n");
            }
        }
    }

    public void Comando() throws IOException
    {
        if(stringaRicevuta.contains("/all "))
           {
                PulisciStringa();
                lista.InvioTotale(Thread.currentThread().getName(), stringaRicevuta);
           }
           else if (stringaRicevuta.contains("/private "))
           {
                String nomeutente="";

                PulisciStringa();

                for (int i = 0; i < stringaRicevuta.length(); i++)
                {
                    if (stringaRicevuta.charAt(i) != ' ')
                    {
                        nomeutente += stringaRicevuta.charAt(i);
                    }
                    else
                    {
                        stringaRicevuta = stringaRicevuta.replace(nomeutente, "");
                        break;
                    }
                }
                
                lista.InvioPrivato(stringaRicevuta, nomeutente,Thread.currentThread().getName());
           }
           else if(stringaRicevuta.contains("/exit"))
           {
             ChiudiConnessione();
           }
    }

    public void ChiudiConnessione() throws IOException
    {
        lista.RimuoviClient(Thread.currentThread().getName());
        lista.InvioTotale(Thread.currentThread().getName(), " " + messaggioAbbandono);
        lista.ListaUtenti();
        outVersoClient.writeBytes("END");
        outVersoClient.close();
        inDalClient.close();
        client.close();
        ciclo = false;
    }

    public void InviaClient(String messaggio) throws IOException
    {
        outVersoClient.writeBytes(Thread.currentThread().getName() + ": " + messaggio + "\n");
    }

    public void InviaLista(String lista) throws IOException
    {
        outVersoClient.writeBytes("Utenti Connessi:" + lista + "\n");
    }

    public void PulisciStringa()
    {

          stringaRicevuta = stringaRicevuta.replace("/all ", "");  
          stringaRicevuta = stringaRicevuta.replace("/private ", "");
          stringaRicevuta = stringaRicevuta.replace("/exit", "");

    }
}
