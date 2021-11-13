package com.melonebianchi.chatsocket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.*;

public class ConsoleServer extends JFrame implements ActionListener
{
    ServerSocket server;
    ListaClient lista;
    BufferedReader tastiera;
    String comando;
    String messaggio;
    boolean stop = true;
    
    Container c = new Container();
    JPanel pannelloPrincipale = new JPanel();
    JPanel pannelloChat = new JPanel();
    JLabel indicazione = new JLabel("Inserire Messaggio =");
    JButton bottoneInput = new JButton("Esegui");
    JTextField input = new JTextField(50);
    JTextArea chatMessaggio;

    public ConsoleServer(ServerSocket server, ListaClient lista, JTextArea chatMessaggio)
    {
        this.chatMessaggio = chatMessaggio;
        this.server = server;
        this.lista = lista;
        tastiera = new BufferedReader(new InputStreamReader(System.in));
        Thread.currentThread().setName("Server");
        
       Grafica();  

    } 

    public void setSTOP(boolean stop)
    {
        this.stop = stop;
    }

    public boolean isSTOP()
    {
        return stop;
    }

    public void Comando(String comando) throws IOException
    {
        messaggio = "";

        if(comando.contains("/list"))
                {
                    chatMessaggio.append("La Lista E' La Seguente: " + lista.ListaUtentiServer() + "\n");
                }
                else if(comando.contains("/all"))
                {   
                        for(int i = 4; i < comando.length(); i++)
                            messaggio += comando.charAt(i);

                  if(!isEmpty())
                     lista.InvioTotale("Server", messaggio);

                     chatMessaggio.append(comando);
                }
                else if(comando.contains("/clear"))
                {
                    for(int i = 0; i < 30; i++)
                    {
                        chatMessaggio.append("\n");
                    }
                }
    }

    public boolean isEmpty()
    {
        if(messaggio.isEmpty())
            chatMessaggio.append("Il Messaggio E' Vuoto");

        return messaggio.isEmpty();
    }

    public void Grafica()
    {
        c = this.getContentPane();

        Font f = new Font("Verdana", Font.BOLD, 14);

        pannelloChat.setLayout(null);
        pannelloChat.setBounds(33, 0, 420, 200);
        pannelloChat.setBackground(Color.DARK_GRAY);

        pannelloPrincipale.setLayout(null);
        pannelloPrincipale.setBackground(Color.DARK_GRAY);
        pannelloPrincipale.setBounds(20, 0, 50, 20);

        DefaultCaret caret = (DefaultCaret)chatMessaggio.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        chatMessaggio.setBounds(10, 10, 300, 180);
        chatMessaggio.setBackground(Color.BLACK);   
        chatMessaggio.setForeground(Color.WHITE); 
        chatMessaggio.setEditable(false);
        
        input.setBounds(133, 210, 200, 20);
        input.setFont(f);
        indicazione.setBounds(10, 210, 200, 20);
        indicazione.setForeground(Color.WHITE);
        bottoneInput.setBounds(350, 210, 80, 20);

        this.setSize(500, 300);
        this.setLocationRelativeTo(null);
        this.setEnabled(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        pannelloChat.add(chatMessaggio);
        JScrollPane scrollPane = new JScrollPane(chatMessaggio);
        scrollPane.setBounds(0, 0, 4200, 200);   
        pannelloChat.add(scrollPane);

        pannelloPrincipale.add(input);
        pannelloPrincipale.add(indicazione);
        pannelloPrincipale.add(bottoneInput);

        c.add(pannelloChat);
        c.add(pannelloPrincipale);

        bottoneInput.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
     {
         String evento = e.getActionCommand();
         String comando = "";

         switch(evento)
         {
             case "Esegui":
                comando = input.getText();
                input.setText("");
            try 
            {
                Comando(comando);
            }
            catch (IOException e1)
             {
              e1.printStackTrace();
             } 

             break;
         }
     }
}
