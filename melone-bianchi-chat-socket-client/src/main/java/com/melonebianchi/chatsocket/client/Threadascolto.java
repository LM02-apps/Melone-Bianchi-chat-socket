package com.melonebianchi.chatsocket.client;

import java.io.DataOutputStream;
import java.io.*;
import java.net.*;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;

/**
 * Questa classe che è un Thread, permette l'ascolto dei messaggi che il Server invia.
 * Quando il Client vuole disconnettersi il Server invia un ACK di conferma prima di chiudera la connessione.
 * ACK = END --> Quando il Client riceve questo messaggio dal Server, significa che può chiudere la connessione.
 */

public class Threadascolto extends Thread
{
    private BufferedReader inServer;    //Stream di input per inviare al Server
    private String rispostaServer;      //Variabile per contenere la risposta del Server
    DataOutputStream outServer;         //Stream per la ricezzione dei messaggi da parte del Server
    Socket socketClient;                //Socket
    JTextArea chatMessaggio = new JTextArea();  //Elemento gragfico per poter stampare a video i messaggi che arrivano.
    DefaultListModel<String> model = new DefaultListModel<String>();

    public Threadascolto(BufferedReader inServer, DataOutputStream outServer, Socket socketClient, JTextArea chatMessaggio, JList<String> listaClient, DefaultListModel<String> model)
    {
        this.inServer = inServer;
        this.outServer = outServer;
        this.socketClient = socketClient;
        this.chatMessaggio = chatMessaggio;
        this.model = model;
    }

    public void run()
    {
        try
        {
            for(;;)
            {
               
                rispostaServer = inServer.readLine();   //Attendo un messaggio dal Client

                //rispostaServer.split(arg0);
                if (rispostaServer.equals("END"))       //Controllo la conferma di potermi disconnettere da parte del Server
                {
                    chatMessaggio.append("Chiusura connessione..." + "\n");

                    inServer.close();       //Chiusura dello Stream
                    outServer.close();      //Chiusura dello Stream
                    socketClient.close();   //Chiusura del Socket

                    chatMessaggio.append("Connessione Terminata" + "\n");

                    break;
                }

                if(rispostaServer.contains("Utenti Connessi:$$"))//Questo if serve per controllare se da parte del Server arriva la nuova lista Client
                {
                    model.removeAllElements();      //Rimuovo tutti i nomi dalla lista
                    model.addElement("Globale");    
                    
                    rispostaServer = rispostaServer.replace("Utenti Connessi:$$", ""); //Elimino quello che non mi serve

                    String[] utenti;            //Array contenitore dove andranno tutti i nomi dei Client
                    String nomeUtente = "";     //nome uternte
                    
                    
                        utenti = rispostaServer.split(";");     //Riempo l'Array

                       for(int i = 0; i < utenti.length; i++)   //For per inizzializare la lista
                       {
                           if(utenti[i].compareTo(Thread.currentThread().getName()) != 0)
                           {    
                                nomeUtente = utenti[i];   
                                model.addElement(nomeUtente);       //Aggiungo i nomi alla lista
                           }
                       }

                       rispostaServer = "";
                }
                
                rispostaServer = rispostaServer.replace("AWT-EventQueue-0:", "Server:");
                chatMessaggio.append(rispostaServer + "\n");

            }
            
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    
}
