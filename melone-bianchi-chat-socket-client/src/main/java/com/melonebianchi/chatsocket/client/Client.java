package com.melonebianchi.chatsocket.client;

import java.io.*;
import java.net.*;


public class Client 
{
    static String nomeServer = "192.168.1.182";
    int portaServer = 6778;
    Socket msocket;
    BufferedReader tastiera;
    BufferedReader indalServer;
    DataOutputStream outVersoServer;
    boolean controllo=false;
    boolean ciclo = true;
    boolean uscita = true;
    String messaggio;
    String stringaUtente;
    String nomehost;
    String stringaRispostaServer;
    Threadascolto threadAscolto;

    // main client
    public static void main(String[] args) 
    {
        Client client = new Client();
        

        client.Connetti();
        client.Comunica();
    }

    public Socket Connetti() 
    {

        System.out.println("Client in esecuzione");

        try 
        {
            tastiera = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Inserire nome client:");

            nomehost = tastiera.readLine();
            msocket = new Socket(nomeServer, portaServer);


            outVersoServer = new DataOutputStream(msocket.getOutputStream());
            indalServer = new BufferedReader(new InputStreamReader(msocket.getInputStream()));
            
            do
            {
                
                outVersoServer.writeBytes(nomehost + '\n');

                stringaRispostaServer = indalServer.readLine();

                if (stringaRispostaServer.equals("OK"))
                {
                    controllo = true;
                }
                else
                {
                    System.out.println(stringaRispostaServer);

                    System.out.println("Reinserire Nuovo Nome:");
                    nomehost = tastiera.readLine();
                }

            }while(!controllo);
                
            

            threadAscolto = new Threadascolto(indalServer, outVersoServer, msocket); //Guardare per bene
            threadAscolto.start();

        } 
        catch (UnknownHostException e)
        {
            System.err.println("Host sconosciuto");
        }
         catch (Exception e) 
        {
            System.out.println("Errore connessione");
            System.exit(1);
        }

        return msocket;
    }

    public void Comunica() 
    {

        for(;ciclo;) 
        {
            try 
            {
                do
                {
                    System.out.println("Inserisci la stringa" + '\n');
                    stringaUtente = tastiera.readLine();


                    String nomeUtente = "";

                    if(stringaUtente.contains("/private "))
                    {
                        for(int i = 9; i < stringaUtente.length(); i++)
                        {
                            
                            if(stringaUtente.charAt(i) != ' ')
                            {
                                nomeUtente += stringaUtente.charAt(i);
                            }
                            else
                            {
                                uscita = false;
                                break;
                            }
                        }

                        for(int i = 9 + nomeUtente.length(); i < stringaUtente.length(); i++)
                        {
                            messaggio += stringaUtente.charAt(i);
                        }
                        
                        isEmpty();
                    }
                    else if(stringaUtente.contains("/all ")) 
                    {
                        for (int i = 4; i < stringaUtente.length(); i++)
                        {
                            messaggio += stringaUtente.charAt(i);
                        }
                        
                        isEmpty();
                    }
                    else if(stringaUtente.contains("/exit"))
                    {
                        uscita = false;
                        ciclo = false;
                    }
                    else
                    {
                        System.out.println("Errore input, riprovare"); 
                        uscita = true;
                    }
                }
                while(uscita);
                
                System.out.println("invio stringa");
                outVersoServer.writeBytes(stringaUtente + '\n');               
            
            } 
            catch (Exception e) 
            {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

    }

    public void isEmpty()
    {
        if(messaggio.isEmpty()) 
        {
             System.out.println("Messaggio vuoto,riprovare"); 
             uscita = true;
        }                
        else
        {
            uscita = false;
        }
    }
}
