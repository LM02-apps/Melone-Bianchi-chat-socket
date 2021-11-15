Protocollo Chat

Server

- Il Server si avvia sulla porta 6778, avviando anche un’interfaccia grafica(Console Server) per poter visualizzare messaggi che riceve il Server, quali Client si connettono e poter interagire anche con i Client.

- Una volta avvenuta la connessione, la classe ServerAccept riceve il nome del client appena connesso e il client corrente viene inizializzato con la classe ThreadClient, viene avviato e lo salva nella classe ListaClient all’interno di un HashMap. In caso di eccezione restituisce un errore riguardo alla connessione.

- Nella classe ThreadClient vengono inizializzati le seguenti variabili:
 il nome del Client;
 il socket del Client; 
 la stringa ricevuta; 
 la lista client;
 l’output e l’input del Client;

- Nel metodo run viene impostato il nome del Client al thread, subito     dopo chiama il metodo Comunica.

- Il metodo Comunica inizializza l’output del client e richiama il metodo ListaClient* dalla classe ListaClient.

- Nella classe ListaClient sono specificati i metodi per aggiungere e rimuovere un client, inviare a tutti i client o a solo un client il messaggio e il metodo ListaClient*.

- All’interno del metodo ListaClient*, avviene la creazione della lista di tutti i client connessi, subito dopo il server richiama il metodo InviaLista, che manda il messaggio a tutti i client.

- Il server a questo punto attende la stringa dal client, stampandola nel suo terminale. All’interno di questa stringa abbiamo il messaggio e il comando che il server dovrà eseguire, il controllo dell’istruzione avviene durante la chiamata del metodo Comando.

- Il metodo Comando controlla se il messaggio ricevuto deve essere mandato a tutti i client o a un solo utente. 

Se la stringa, all’inizio del messaggio, contiene /all, significa che il server dovrà inviare a tutti il messaggio con il metodo InvioTotale che si trova nella classe ListaClient. Prima di fare ciò il server richiama il metodo PulisciStringa che rimuove /all per poter inviare una stringa pulita ai Client.
Se la stringa, all’inizio del messaggio, contiene /private, nomeutente e il messaggio, il server interpreta il comando da inviare al Client con il nomeutente all’interno della Stringa. Come con /all richiama il metodo PulisciStringa. 

- Il metodo PulisciStringa sostituisce /all e /private nomeutente con uno spazio.

- Un Client se vuole disconnettersi, invia al Server il comando /exit. Il Server prima di dargli la conferma per potersi disconnettere, lo rimuove dalla lista dei Client connessi, invia nuovamente a tutti la nuova lista dei Client connessi, infine avvien la disconessione inviando al Client il messaggio di conferma(“END”).



Client

Client si avvia, la prima istruzione che esegue è il metodo Connetti. All’interno del metodo si Inizializza un BufferedReader per riceve gli input da tastiera. Subito dopo viene chiesto all’utente di inserire il nome dell’Client. 

Il Client tenta la connessione al server, se la connessione va a buon fine, inizializza gli input e gli output. A questo punto invia il nome che l’utente ha inserito e lancia un thread per potersi mettere in ascolto in caso il server dovesse inviare dei messaggi al Client. 

Il Client in questo momento attende che l’utente inserisca una stringa da inviare al Server con delle regole che sono le seguenti:
/all messaggio (invia il messaggio a tutti i client)
/private nomeClient messaggio (invia il messaggio solo al client specifico)
/exit (Il Client si disconnette)
