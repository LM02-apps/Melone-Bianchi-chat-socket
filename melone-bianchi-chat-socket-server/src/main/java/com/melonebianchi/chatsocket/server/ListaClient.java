package com.melonebianchi.chatsocket.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ListaClient 
{
    HashMap<String, ThreadClient> clientRunning = new HashMap<String, ThreadClient>();  //Contenitore Dei Client connessi

    public void AggiungiClient(String nome, ThreadClient x)
    {
        clientRunning.put(nome, x);
    }
    
    public void RimuoviClient(String nome)
    {
        clientRunning.remove(nome);
    }

    public boolean Input(String nomeCliet)
    {
        return clientRunning.containsKey(nomeCliet);
    }

    public void InvioTotale(String nomeClient, String messaggio) throws IOException
    {

        for(Map.Entry<String, ThreadClient> e : clientRunning.entrySet())
        {
            if(nomeClient != e.getValue().getName())
            {
               clientRunning.get(e.getKey()).InviaClient(messaggio);
            }
        }
    }

    public void InvioPrivato(String messaggio, String nomeClientPrivato, String clientChiamata) throws IOException
    {   
        if(clientRunning.containsKey(nomeClientPrivato))
        {
            clientRunning.get(nomeClientPrivato).InviaClient(messaggio);
        }
        else
        {
            messaggio = "Nome client inesistente";
            clientRunning.get(clientChiamata).InviaClient(messaggio);
        }
    }

    public boolean isEmpty()    //Controlla se l'array è vuoto
    {
        return clientRunning.isEmpty();
    }
    
    public int getSize()        //Ritorna la grandezza
    {
        return clientRunning.size();
    }

    public void ListaUtenti() throws IOException
    {
        String listaClient = "";

        for(Map.Entry<String, ThreadClient> e : clientRunning.entrySet())
        {
               listaClient += "\n" + clientRunning.get(e.getKey()).getName();
        }

        for(Map.Entry<String, ThreadClient> e : clientRunning.entrySet())
        {
                clientRunning.get(e.getKey()).InviaLista(listaClient);
        }
    }

    public String ListaUtentiServer()
    {
        String listaClient = "";

        for(Map.Entry<String, ThreadClient> e : clientRunning.entrySet())
        {
            listaClient += "\n" + clientRunning.get(e.getKey()).getName();
        }

        return listaClient;
    }
}
