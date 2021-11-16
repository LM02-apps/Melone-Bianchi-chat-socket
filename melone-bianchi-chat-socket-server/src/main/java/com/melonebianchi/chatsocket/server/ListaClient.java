package com.melonebianchi.chatsocket.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * QUesta classe è la classe  contenitore di tutti i Client che si connettono e contiene i metodi per Aggiungere, Rimuovere, Inviare ai Client i messaggi
 */

public class ListaClient 
{
    HashMap<String, ThreadClient> clientRunning = new HashMap<String, ThreadClient>(); // Contenitore Dei Client connessi (HashMap)
                                                                                       

    /**
     * 
     * Metodo che aggiunge i CLient al Server
     */
    public void AggiungiClient(String nome, ThreadClient x) 
    {
        clientRunning.put(nome, x);
    }

    /**
     * Rimuove il Client
     */
    public void RimuoviClient(String nome) 
    {
        clientRunning.remove(nome);
    }


    /**
     * Il Metodo ritorna vero o falso se all'interno dell'HashMap è già presente il nome che l'utente inserisce per identificarsi
     * @param nomeCliet
     * @return
     */
    public boolean Input(String nomeCliet) 
    {
        return clientRunning.containsKey(nomeCliet);
    }

    /**
     * Invia a tutti i Client i messaggi di ogni Client che vuole comunicare un Qualcosa
     * @param nomeClient
     * @param messaggio
     * @throws IOException
     */

    public void InvioTotale(String nomeClient, String messaggio) throws IOException 
    {

        for (Map.Entry<String, ThreadClient> e : clientRunning.entrySet()) 
        {
            if (nomeClient != e.getValue().getName()) 
            {
                clientRunning.get(e.getKey()).InviaClient(messaggio);
            }
        }
    }

    /**
     * Il Metodo Invia al Client interessato il messaggio di un Client che vuole comunicare 
     * @param messaggio
     * @param nomeClientPrivato
     * @param clientChiamata
     * @throws IOException
     */

    public void InvioPrivato(String messaggio, String nomeClientPrivato, String clientChiamata) throws IOException {
        if (clientRunning.containsKey(nomeClientPrivato)) {
            clientRunning.get(nomeClientPrivato).InviaClient("(Privato) " + messaggio);
        } else {
            messaggio = "Nome client inesistente";
            clientRunning.get(clientChiamata).InviaClient(messaggio);
        }
    }

    public boolean isEmpty() // Controlla se l'array è vuoto
    {
        return clientRunning.isEmpty();
    }

    public int getSize() // Ritorna la grandezza
    {
        return clientRunning.size();
    }

    /**
     * Il Metodo Crea la lista degli utenti connessi.
     * @throws IOException
     */

    public void ListaUtenti() throws IOException 
    {
        String listaClient = "";

        for (Map.Entry<String, ThreadClient> e : clientRunning.entrySet()) {
            listaClient += "\n" + clientRunning.get(e.getKey()).getName();
        }

        for (Map.Entry<String, ThreadClient> e : clientRunning.entrySet()) {
            clientRunning.get(e.getKey()).InviaLista(listaClient);
        }
    }

    /**
     * Crea La lista degli Utenti connessi per la consoleServer
     * @return
     */
    public String ListaUtentiServer() 
    {
        String listaClient = "";

        for (Map.Entry<String, ThreadClient> e : clientRunning.entrySet()) {
            listaClient += "\n" + clientRunning.get(e.getKey()).getName();
        }

        return listaClient;
    }
}
