package com.melonebianchi.chatsocket.client;

import java.io.*;
import java.net.*;


public class Client 
{
    static String nomeserver = "192.168.1.25";
    int portaserver = 6778;
    Socket msocket;
    BufferedReader tastiera;
    String stringautente;
    String nomehost;
    String stringarispostaserver;
    DataOutputStream outversoserver;
    BufferedReader indalserver;
    Threadascolto mthreadascolto;
    boolean controllo=false;
    String messaggio;

    // main client
    public static void main(String[] args) 
    {
        Client mclient = new Client();
        

        mclient.connetti();
        mclient.comunica();
    }

    public Socket connetti() 
    {

        System.out.println("Client in esecuzione");
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Inserire nome client:");
            nomehost = tastiera.readLine();
            msocket = new Socket(nomeserver, portaserver);


            outversoserver = new DataOutputStream(msocket.getOutputStream());
            indalserver = new BufferedReader(new InputStreamReader(msocket.getInputStream()));
            
            do
            {
                
                outversoserver.writeBytes(nomehost + '\n');
                stringarispostaserver=indalserver.readLine();
                if (stringarispostaserver.equals("OK"))
                {
                    controllo=true;
                }
                else
                {
                    System.out.println(stringarispostaserver);
                    System.out.println("Reinserisci:");
                    nomehost = tastiera.readLine();
                }

            }while(!controllo);
                
            

            mthreadascolto=new Threadascolto(indalserver,outversoserver,msocket); //Guardare per bene
            mthreadascolto.start();

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore connessione");
            System.exit(1);
        }
        return msocket;
    }

    public void comunica() 
    {

        for (;;) 
        {
            try 
            {
                boolean uscita = true;

                do
                {
                    System.out.println("Inserisci la stringa" + '\n');
                    stringautente = tastiera.readLine();


                    String nomeutente = "";

                    if (stringautente.contains("/private "))
                    {
                        for (int i = 9; i < stringautente.length(); i++)
                        {
                            
                            if (stringautente.charAt(i) != ' ')
                            {
                                nomeutente += stringautente.charAt(i);
                            }

                            else
                            {
                                uscita = false;
                                break;
                            }
                        }

                        for (int i = 9+nomeutente.length(); i < stringautente.length(); i++)
                        {
                            messaggio += stringautente.charAt(i);
                        }
                        
                        if(messaggio.isEmpty()) 
                        {
                            System.out.println("Messaggio vuoto,riprovare"); 
                            uscita=true;
                        }
                        
                        else
                        {
                            uscita=false;
                        }
                    }


                    else if (stringautente.contains("/all ")) 
                    {
                        for (int i = 4; i < stringautente.length(); i++)
                        {
                            messaggio += stringautente.charAt(i);
                        }
                        
                        if(messaggio.isEmpty()) 
                        {
                            System.out.println("Messaggio vuoto,riprovare"); 
                            uscita=true;
                        }
                        
                        else
                        {
                            uscita=false;
                        } 
                    }

                    else if (stringautente.contains("/exit"))
                    {
                        uscita=false;
                    }


                    else
                    {
                        System.out.println("Errore input,riprovare"); 
                        uscita = true;
                    }


                }
                while(uscita);
                

                if (stringautente.contains("/exit"))
                {
                    break;
                }

                System.out.println("invio stringa");
                outversoserver.writeBytes(stringautente + '\n');

                
            
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

    }
}
