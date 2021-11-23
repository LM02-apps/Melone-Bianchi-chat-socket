package com.melonebianchi.chatsocket.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;

/**
 * Questa Classe Gestisce la console del Server, un interfaccia per poter interagire con i Client.
 */

public class ConsoleServer extends JFrame implements ActionListener 
{
    ServerSocket server;
    ListaClient lista;
    String comando;
    String messaggio;

    Container c = new Container();
    JPanel pannelloPrincipale = new JPanel();               // Pannello principale dove vengono aggiunti i vari componenti della Grafica
    JPanel pannelloChat = new JPanel();                     // Pannnello per la visualizzazione dei Messaggi
    JLabel indicazione = new JLabel("Inserire Messaggio");  // Indicazione di Input per l'utente
    JButton bottoneInput = new JButton("Esegui");           // Bottone di input per inviare i messaggi
    JTextField input = new JTextField(50);                  // Elemento grafico per l'inserimento delle Stringhe
    JTextArea chatMessaggio;                                // Un'area di Testo per visualizzare la ricezzione e l'invio dei messaggi

    public ConsoleServer(ServerSocket server, ListaClient lista, JTextArea chatMessaggio) 
    {
        this.chatMessaggio = chatMessaggio;
        this.server = server;
        this.lista = lista;
        Thread.currentThread().setName("Server");

        Grafica(); // Richiamo della Grafica

    }

    public void Comando(String comando) throws IOException 
    {
        messaggio = "";

        if (comando.contains("/list")) //Comando per Visualizzare la lista dei CLient Connessi
        {
            chatMessaggio.append("La Lista E' La Seguente: " + lista.ListaUtentiServer() + "\n");
        } 
        else if (comando.contains("/all")) //Invio dei Messaggi 
        {
            for (int i = 4; i < comando.length(); i++)  //Prendo il messaggio da inviare
                messaggio += comando.charAt(i);

            if (!isEmpty())                             //Controllo il messaggio se è vuoto
                lista.InvioTotale("Server", messaggio);

            chatMessaggio.append(comando);              //stampo il messaggio nel Terminale
        } 
        else if (comando.contains("/clear"))        //Pulisco il Terminale
        {
            for (int i = 0; i < 30; i++) 
            {
                chatMessaggio.append("\n");
            }
        }
    }

    public boolean isEmpty() //Controllo se il Messaggio è Vuoto
    {
        if (messaggio.isEmpty())
            chatMessaggio.append("Il Messaggio E' Vuoto");

        return messaggio.isEmpty();
    }

    public void Grafica() 
    {
        c = this.getContentPane();

        Font f = new Font("Verdana", Font.BOLD, 14);    //Imposto Il Font

        pannelloChat.setLayout(null);
        pannelloChat.setBounds(33, 0, 420, 200);        // Impostazione Delle cordinate del pannello
        pannelloChat.setBackground(Color.DARK_GRAY);

        pannelloPrincipale.setLayout(null);
        pannelloPrincipale.setBackground(Color.DARK_GRAY);  // Impostazione del Colore
        pannelloPrincipale.setBounds(20, 0, 50, 20);

        DefaultCaret caret = (DefaultCaret) chatMessaggio.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chatMessaggio.setBounds(10, 10, 300, 180);
        chatMessaggio.setBackground(Color.BLACK);
        chatMessaggio.setForeground(Color.WHITE);
        chatMessaggio.setEditable(false);

        input.setBounds(153, 210, 200, 20);
        input.setFont(f);
        indicazione.setBounds(7, 210, 200, 20);
        indicazione.setForeground(Color.WHITE);
        bottoneInput.setBounds(370, 210, 90, 20);

        pannelloChat.add(chatMessaggio);
        JScrollPane scrollPane = new JScrollPane(chatMessaggio);  // Elemento Grafico per l'autoscorrimento dei messaggi
        scrollPane.setBounds(0, 0, 4200, 200);
        pannelloChat.add(scrollPane);

        // Aggiungo gli elementi Grafici al pannello Principale

        pannelloPrincipale.add(input);
        pannelloPrincipale.add(indicazione);
        pannelloPrincipale.add(bottoneInput);

        c.add(pannelloChat);
        c.add(pannelloPrincipale);

        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setEnabled(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Server");

        // I Pulsanti stanno in ascolto per Glie Eventi

        bottoneInput.addActionListener(this);
    }

    /**
     * Questo metodo serve per la gestione degli eventi in caso l'utente prema sui
     * bottoni.
     */

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String evento = e.getActionCommand();
        String comando = "";

        switch (evento) 
        {
        case "Esegui":

            comando = input.getText();

            input.setText("");

            try 
            {
                Comando(comando);
            } catch (IOException e1) 
            {
                e1.printStackTrace();
            }

            break;
        }
    }
}