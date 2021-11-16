package com.melonebianchi.chatsocket.client;

import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;

/**
 * Il Client si avvia con un'interfaccia grafica. La finestra all'avvio chiede
 * di inserire un indirizzo IP e una porta. Il Client tenta una connessione, se
 * va a buon fine il Client chiede di inserire un nome per potersi identificare
 * all'interno del Server. Il Client invia il nome e il Server risponde se il
 * nome Utente inserito è valido o no. in esito negativo dovremmo reinserire il
 * nome. In caso di esito positivo il Client può comunicare all'interno della
 * Chat con i seguenti comandi.
 * 
 * /all + messaggio --> Questo invia un messaggio a tutti
 * 
 * /private + nomeClient + messaggio --> Questo invia un messaggio privato solo
 * al Client che l'utente desidera.
 * 
 * /exit --> Il Client si disconnette dalla Chat.
 */

public class Client extends JFrame implements ActionListener 
{
    String nomeServer;                              // Variabile per l'inserimento dell'indirizzo IP
    String portaServer;                             // Variabile per la porta del Server
    Socket msocket;                                 // Socket per poter instaurare una connessione
    BufferedReader indalServer;                     // Buffered per leggere i messaggi che il Server ci invia
    DataOutputStream outVersoServer;                // DataOutput per inviare al Server i nostri messaggi
    String messaggio = "";                          // Variabile per contenere il messaggio che vogliamo inviare
    String stringaUtente;                           // Variabile per l'inserimento del comando
    String nomehost;                                // Variabile per inserire l'host in caso volessimo scrivere ad un Client
    String stringaRispostaServer;                   // Variabile per contenere la risposta del Server
    Threadascolto threadAscolto;                    // Thread per consentire la ricezzione dei messaggi da parte del Server

    Container c = new Container();                                          // Contenitore
    JPanel pannelloPrincipale = new JPanel();                               // Pannello principale dove vengono aggiunti i vari componenti della Grafica
    JPanel pannelloChat = new JPanel();                                     // Pannnello per la visualizzazione dei Messaggi
    JLabel indicazione = new JLabel("Inserire Messaggio");                  // Indicazione di Input per l'utente
    JLabel indicazioneIp = new JLabel("Inserire Indirizzo IP");             // Indicazione di Input per l'utente
    JLabel indicazionePorta = new JLabel("Inserire Porta");                 // Indicazione di Input per l'utente
    JLabel indicazioneNomeHost = new JLabel("Inserire Nome Host");          // Indicazione di Input per l'utente
    JButton bottoneInput = new JButton("Esegui");                           // Bottone di input per inviare i messaggi
    JButton bottoneConnetti = new JButton("Connetti");                      // Bottone di input per la connessione al Server
    JButton bottoneHost = new JButton("Invia");                         // Bottone di input per l'inserimento e l'invio del Nome al Server
    JTextField input = new JTextField(50);                                  // Elemento grafico per l'inserimento delle Stringhe
    JTextField inputIP = new JTextField(50);                                // Elemento grafico per l'inserimento delle Stringhe
    JTextField inputPorta = new JTextField(50);                             // Elemento grafico per l'inserimento delle Stringhe
    JTextField inputNomeHost = new JTextField(50);                          // Elemento grafico per l'inserimento delle Stringhe
    JTextArea chatMessaggio = new JTextArea();                              // Un'area di Testo per visualizzare la ricezzione e l'invio dei messaggi

    public Client()
     {
        Grafica();      // Richiamo della Grafica
    }

    /**
     * Questo Metodo imposta la visualizzazione a Video della grafica che compone il
     * Client
     */

    public void Grafica() 
    {
        c = this.getContentPane();

        Font f = new Font("Verdana", Font.BOLD, 14); // Impostazione Del Font

        pannelloChat.setLayout(null);
        pannelloChat.setBounds(33, 0, 420, 200); // Impostazione Delle cordinate del pannello
        pannelloChat.setBackground(Color.DARK_GRAY); // Impostazione del Colore

        pannelloPrincipale.setLayout(null);
        pannelloPrincipale.setBackground(Color.DARK_GRAY);
        pannelloPrincipale.setBounds(20, 0, 50, 20);

        DefaultCaret caret = (DefaultCaret) chatMessaggio.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chatMessaggio.setBounds(10, 10, 300, 180);
        chatMessaggio.setBackground(Color.BLACK);
        chatMessaggio.setForeground(Color.WHITE);
        chatMessaggio.setEditable(false);

        input.setBounds(153, 210, 200, 20);
        inputIP.setBounds(153, 70, 200, 20);
        inputPorta.setBounds(153, 120, 200, 20);
        inputNomeHost.setBounds(153, 230, 200, 20);
        input.setFont(f);
        inputIP.setFont(f);
        inputPorta.setFont(f);
        inputNomeHost.setFont(f);
        indicazione.setBounds(7, 210, 200, 20);
        indicazione.setForeground(Color.WHITE);
        indicazioneIp.setBounds(203, 45, 200, 20);
        indicazioneIp.setForeground(Color.WHITE);
        indicazionePorta.setBounds(215, 100, 200, 20);
        indicazionePorta.setForeground(Color.WHITE);
        indicazioneNomeHost.setBounds(7, 230, 200, 20);
        indicazioneNomeHost.setForeground(Color.WHITE);
        bottoneInput.setBounds(370, 210, 90, 20);
        bottoneConnetti.setBounds(153, 160, 120, 20);
        bottoneHost.setBounds(370, 230, 90, 20);

        pannelloChat.add(chatMessaggio);
        JScrollPane scrollPane = new JScrollPane(chatMessaggio); // Elemento Grafico per l'autoscorrimento dei messaggi
        scrollPane.setBounds(0, 0, 4200, 200);
        pannelloChat.add(scrollPane);

        // Aggiungo gli elementi Grafici al pannello Principale

        pannelloPrincipale.add(input);
        pannelloPrincipale.add(indicazione);
        pannelloPrincipale.add(inputIP);
        pannelloPrincipale.add(inputPorta);
        pannelloPrincipale.add(inputNomeHost);
        pannelloPrincipale.add(bottoneInput);
        pannelloPrincipale.add(indicazioneIp);
        pannelloPrincipale.add(indicazionePorta);
        pannelloPrincipale.add(bottoneConnetti);
        pannelloPrincipale.add(indicazioneNomeHost);
        pannelloPrincipale.add(bottoneHost);

        c.add(pannelloChat);
        c.add(pannelloPrincipale);

        // Impostazioni Della Finestra

        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setEnabled(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Chat Client Server");

        // I Pulsanti stanno in ascolto per Glie Eventi

        bottoneInput.addActionListener(this);
        bottoneConnetti.addActionListener(this);
        bottoneHost.addActionListener(this);

        pannelloChat.setVisible(false);
        input.setVisible(false);
        indicazione.setVisible(false);
        bottoneInput.setVisible(false);
        bottoneHost.setVisible(false);
        inputNomeHost.setVisible(false);
        indicazioneNomeHost.setVisible(false);

    }

    /**
     * Questo Metodo ha il compito di interpretare il comando inserito dall'utente
     * per verificare eventuali errori prima dell'invio
     */

    public void Comunica() 
    {

        try 
        {
            stringaUtente = input.getText(); // Prendo il Testo dall'elemento grafico per interpretare il comando

            String nomeUtente = "";

            if (stringaUtente.contains("/private ")) // Controllo Se Il Messaggio è Di Tipo privato. Ovvero vado a prendere il nome e il messaggio inserito per controllare se ci siano errori di sintassi
            {
                for (int i = 9; i < stringaUtente.length(); i++)
                 {

                    if (stringaUtente.charAt(i) != ' ') 
                    {
                        nomeUtente += stringaUtente.charAt(i); // prendo il nome utente del Client con cui si vuole comunicare
                    }
                }

                for (int i = 9 + nomeUtente.length(); i < stringaUtente.length(); i++) 
                {
                    messaggio += stringaUtente.charAt(i); // Prendo il messaggio che si vuole inviare
                }
            } 
            else if (stringaUtente.contains("/all ")) // Controllo se il messaggio è di tipo globale.
            {
                for (int i = 4; i < stringaUtente.length(); i++) 
                {
                    messaggio += stringaUtente.charAt(i); // Prendo il messaggio che si vuole inviare
                }
            } 
            else if (stringaUtente.contains("/exit")) // Controllo se l'utente decide di socllegarsi
            {
                bottoneInput.setVisible(false);
            } 
            else 
            {
                chatMessaggio.append("Errore input, riprovare" + "\n"); // In Caso Di errore stampo la seguente Stringa
            }

            input.setText("");

            if (!isEmpty() || stringaUtente.matches("/exit")) // Controllo se all'interno del messaggio è presente una stringa, quindi se ha senso inviare al Server il+ comando
            {
                chatMessaggio.append("invio stringa" + "\n");
                outVersoServer.writeBytes(stringaUtente + "\n"); // Invio al Server
            }
        } 
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Questo metodo permette la connessione al Client, andando prima di tutto a
     * controllare se l'IP e la Porta sono valori numerici(Controllo Sull'Input) In
     * caso di esito positvio il Client Riesce a Connettersi al Server
     */

    public void Connetti() 
    {
        nomeServer = inputIP.getText(); // Prendo l'IP
        portaServer = inputPorta.getText(); // Prendo la Porta

        if (!nomeServer.isEmpty() && !portaServer.isEmpty()) // Controllo se i parametri non sono vuoti
        {

            if (!nomeServer.matches("[a-zA-Z]+") || nomeServer.matches("localhost") && !portaServer.matches("[a-zA-Z]+")) // controllo se all'interno dei parametri non ci siano frasi o altro
            {
                try 
                {
                    msocket = new Socket(nomeServer, Integer.valueOf(portaServer)); // Connessione al Server tramite Socket
                } 
                catch (NumberFormatException | IOException e1)
                 {
                    e1.printStackTrace();
                }

                inputIP.setVisible(false);
                inputPorta.setVisible(false);
                indicazioneIp.setVisible(false);
                indicazionePorta.setVisible(false);
                bottoneConnetti.setVisible(false);
                pannelloChat.setVisible(true);
                inputNomeHost.setVisible(true);
                indicazioneNomeHost.setVisible(true);
                bottoneHost.setVisible(true);
            } 
            else 
            {
                indicazioneIp.setText("Inserire Un Indirizzo IP");
                indicazionePorta.setText("Inserire Una Porta");
            }
        }
    }

    /**
     * Questa classe gestisce l'inserimento del nome utente da inviare al Server
     */

    public void Inserisci() 
    {
        nomehost = inputNomeHost.getText(); // Prendo il nome utente inserito

        try 
        {
            // Inizializzo Gli Stream
            outVersoServer = new DataOutputStream(msocket.getOutputStream());
            indalServer = new BufferedReader(new InputStreamReader(msocket.getInputStream()));

            outVersoServer.writeBytes(nomehost + "\n"); // Invio al Server

            stringaRispostaServer = indalServer.readLine(); // Attendo la Risposta Dal Server

            // Controllo Se il Server mi da l'OK del nome utente inserito, altrimenti dovrò
            // inserirne un altro.
            if (stringaRispostaServer.equals("OK")) 
            {
                threadAscolto = new Threadascolto(indalServer, outVersoServer, msocket, chatMessaggio); // Imposto il Thread che ascolterà i messaggi dal Server
                threadAscolto.start(); // Lancio il Thread
                this.setTitle(nomehost);

                bottoneInput.setVisible(true);
                bottoneHost.setVisible(false);
                input.setVisible(true);
                inputNomeHost.setVisible(false);
                indicazioneNomeHost.setVisible(false);
                indicazione.setVisible(true);
            } 
            else 
            {
                chatMessaggio.append(stringaRispostaServer + "\n");

                chatMessaggio.append("Reinserire Nuovo Nome:" + "\n");
            }

        } catch (NumberFormatException | IOException e1) {
            e1.printStackTrace();
        }
    }

    public boolean isEmpty() // Controlla se il messaggio è vuoto o no(true = vuoto; false = non vuoto)
    {
        return messaggio.isEmpty();
    }

    /**
     * Questo metodo serve per la gestione degli eventi in caso l'utente prema sui
     * bottoni.
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        String evento = e.getActionCommand();

        switch (evento) 
        {
        case "Connetti":

            Connetti();
            break;

        case "Invia":

            Inserisci();

            break;

        case "Esegui":
            Comunica();
            break;
        }
    }

}
