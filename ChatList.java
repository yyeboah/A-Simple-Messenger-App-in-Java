
import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.lang.*;
import javax.swing.*;




public class ChatList extends Frame implements Runnable, ActionListener
{
    private Thread thread;
    private String username;// this takes the username
    private String receiver;// this takes the receiver
    private Button login,chat, addBuddy, logout,ignoreButton,history;
    private List buddylist= new List(); //used to create a list object called buddylist
    private List onlinelist= new List(); // used to create new list object called onlinelist
    private TextField textfield1, buddyName,textfield0,textfield2,textfield3,textfield4,textfield5;
    private MenuBar menubar; //used to vrate a menu bar item called menubar
    private Menu program,edit,help,servers; //used to create meu items "program,edit,help"
    private MenuItem mnExit,mnBack,mnClear,mnHow,mnAbout,mnColour1,mnColour2,mnback1,mnback2,localhost,remotehost,newcontact;
    private Socket socket; //used to creat a socket object of the class Socket
    private PrintWriter out; //
    private BufferedReader in ; //
    private boolean online; // used to set a user online or offline
    private String ip; //a string object called IP to hold the ip of the host
    private int character; //
    private char[] chars = new char[1]; //used to store tha characters in an array
    private static DateFormat df; //used to set the data format for storing the chat log

    public static void main(String[] args)
    {
        new ChatList();
    }//main()

    public ChatList() // the list that holds the contacts
    {
        setLayout(null);
        setResizable(false); //sets the field so that user cannot change the sice

        menubar = new MenuBar(); //creates new menu bar object
        program = new Menu("Java Chat"); //creates a new menu object called Java chat
        edit = new Menu(" Interface"); // a new menu object called  interface
        help = new Menu("Details"); // a new menu object called Details
        servers = new Menu("Servers"); // server menu object details

        mnExit = new MenuItem("Exit"); //a new menu item caleled Exit
        program.add(mnExit); // adds the menu item "Exit" to the menu object "program"
        mnExit.addActionListener(this); // the action listener for the "exit" item.

        newcontact = new MenuItem("Add New Contact");
        program.add(newcontact);
        newcontact.addActionListener(this);

        localhost = new MenuItem("Local Host");
        servers.add(localhost);
        localhost.addActionListener(this);

        remotehost = new MenuItem("Remote Server");
        servers.add(remotehost);
        remotehost.addActionListener(this);

        mnBack = new MenuItem("Set window colour to default "); // menu item for setting window background
        edit.add(mnBack); // add this option to the edit menu="interface"
        mnBack.addActionListener(this); // add an action listener to listen for events

        mnColour1 = new MenuItem("Set Window colour to orange "); // for setting window colour to orange
        edit.add(mnColour1); // add it to menu item, edit
        mnColour1.addActionListener(this); // add action listener to listen for events

        mnColour2 = new MenuItem("Set Window colour to green");
        edit.add(mnColour1); // add it to menu item, "edit"
        mnColour1.addActionListener(this);

        mnHow = new MenuItem("User Manual"); // create menu item called "user manual "
        help.add(mnHow); // add this menu item to "help"
        mnHow.addActionListener(this); // add an action listener

        mnAbout = new MenuItem("Informaiton");
        help.add(mnAbout);
        mnAbout.addActionListener(this);
       /* adds the created menu bars: program, edit, help and menubar to the menu items */
        menubar.add(program);
        menubar.add(edit);
        menubar.add(help);
        menubar.add(servers);
        setMenuBar(menubar);

        /* creates and adds the text fields to the UI */
        textfield0 = new TextField("Login Name");
        textfield0.setBounds(25, 100, 110, 20);
        add(textfield0);
        textfield0.setBackground(Color.white);



        textfield4 = new TextField("Checking...");
        textfield4.setBounds(290, 130, 80, 20);
        add(textfield4);
        textfield4.setBackground(Color.orange);
        textfield4.setEditable(false);

         //textfield5 = new TextField("Wellcome to RoseIndia.net. \n " + "This is a web
//);
        //textfield5.setBounds(220, 250, 1000, 150);
        //add(textfield5);
        //textfield5.setBackground(Color.pink);
        //textfield5.setEditable(false);
        //history = new Button("View Chat Log");
       // history.setBounds(290, 133, 100, 35);
       // add(history);
       // history.addActionListener(this);
        //history.setBackground(Color.pink);

        textfield1 = new TextField("Server's IP");
        textfield1.setBounds(150, 100, 110, 20);
        add(textfield1);
        textfield1.setBackground(Color.white);
        textfield1.setVisible(true);


        login = new Button("Login");
        login.setBounds(270, 100, 60, 20);
        add(login);
        login.addActionListener(this);
        login.setBackground(Color.green);



        chat = new Button("Begin Chat");
        chat.setBounds(25, 70, 110, 20);
        add(chat);
        chat.addActionListener(this);
        

        //ignoreButton = new Button("ignore");
       // ignoreButton.setBounds(300, 50 , 80 , 20);
       // add(ignoreButton);
        //ignoreButton.addActionListener(this);
       // ignoreButton.setBackground(Color.pink);


        buddylist.setSize(270, 450);
        buddylist.setLocation(20, 130);
        buddylist.addActionListener(this);
        add(buddylist);

       // onlinelist.setSize(100, 150);
       // onlinelist.setLocation(290, 210);
       // onlinelist.addActionListener(this);
       // onlinelist.setBackground(Color.green);
       // add(onlinelist);



        buddyName = new TextField("new contact");
        buddyName.setBounds(290, 190, 80, 20);
        add(buddyName);
        buddyName.setVisible(false);
        buddyName.setBackground(Color.white);


        addBuddy = new Button("add contact");
        addBuddy.setBounds(290, 220, 80, 20);
        addBuddy.setVisible(false);


        add(addBuddy);
        addBuddy.addActionListener(this);
     

         logout = new Button("Logout");
        logout.setBounds(340, 100, 60, 20);
        add(logout);
        logout.addActionListener(this);
        logout.setBackground(Color.yellow);

                    logout.setEnabled(false);

                    ;




        setBackground(Color.lightGray);
        setSize(420,600);
        setResizable(false);

        setTitle("Java Chat");
        setVisible(true);

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(
                WindowEvent e){
                    System.exit(0);
                }
            }
        );
    }//ChatList()

    public void run(){
        String instring;
        try {
            in = new BufferedReader (new InputStreamReader(socket.getInputStream()));
            while((instring = in.readLine()) != null){
                boolean notPresent = true;
                for(int i = 0; i < buddylist.getItemCount(); i++){
                    if(buddylist.getItem(i).equals(instring)){notPresent = false;}

                }

                if(notPresent){
                    if(!(instring.equals(this.username)))
                    this.buddylist.add(instring);
                    //new log file for the signed in client
                }
            }
        }catch (Exception e){}



    }//run()

    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == login){
            if(!online){
                try{
                    ip = textfield1.getText();
                    socket = new Socket(ip, 9800);
                    out = new PrintWriter (socket.getOutputStream(), true);
                    thread = new Thread(this);
                    thread.start();
                }
                catch (IOException ioe){}
                catch (Exception e){}

                if(socket != null && socket.isConnected()){
                    textfield1.setText("Online");
                    textfield4.setText("Online");
                    textfield4.setBackground(Color.green);
                    login.setEnabled(false);
                    logout.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "You are successfully logged In !" );
                    online = true;
                }else{
                    textfield1.setText("Please Input Valid Server IP!");
                    JOptionPane.showMessageDialog(null, "SERVER IS OFFLIINE !" );
                    textfield4.setText("Failed");
                textfield4.setBackground(Color.red);
                }

                try{
                    this.username = textfield0.getText();
                    String temp = this.username ;
                    out.println(temp);
                }catch(Exception ex){}//username sent

                try{
                    this.receiver = "ComPlex?!@**3St(YeAh)ring";
                    String temp = this.receiver;
                    out.println(temp);
                }catch(Exception ex){}//receiver added
            }
        }

        if(event.getSource() == chat){
            try{}catch(Exception ex){}
        }

       if(event.getSource() == buddylist){
            try{
                String str = buddylist.getSelectedItem();

                new ChatWindow(textfield0.getText(),str,ip);

            }
            catch(Exception ex){}


        }
        if(event.getSource() == addBuddy){
            try{
                String str = buddyName.getText();
                buddylist.add(str);
                onlinelist.add(str);
                buddyName.setText("Buddy's Name");
                buddyName.requestFocus();

            }
            catch(Exception ex){}
        }
        if(event.getSource() == history){
            new History(this.receiver);
        }
        if(event.getSource() == localhost){
            textfield1.setText("127.0.0.1");

        }

        if(event.getSource() == logout){
            try{
                String str = "/quit";
                out.println(str);
                textfield1.setText("signed out");
                textfield4.setText("offline");
                textfield1.setVisible(true);
                textfield4.setBackground(Color.orange);
                login.setEnabled(true);
                    logout.setEnabled(false);
                    localhost.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "You are successfully logged out of the system");
                    buddylist.clear();
                //Thread.sleep(40);
                out.close();
                socket.close();
                online = false;
            }
            catch(Exception ex){}

        }

       if(event.getSource() == mnExit){JOptionPane.showMessageDialog(null, "The client application is exiting now");System.exit(0);}
        if(event.getSource() == mnColour1){setBackground(Color.orange);}
        if(event.getSource() == mnBack){setBackground(Color.lightGray);}
        if(event.getSource() == mnColour2){setBackground(Color.orange);}
        if(event.getSource() == mnback1){textfield1.setBackground(Color.white);textfield0.setBackground(Color.white);buddyName.setBackground(Color.white);}
        if(event.getSource() == mnback2){textfield1.setBackground(Color.gray);textfield0.setBackground(Color.gray);buddyName.setBackground(Color.gray);}
 if(event.getSource() == remotehost){textfield1.setText("59.174.45.180");JOptionPane.showMessageDialog(null, "You are connecting to the remote server");}

        if(event.getSource() == mnHow){JOptionPane.showMessageDialog(null, "The server must be running first.If you are not sure of the servers IP , contact on the developer");
}
        if(event.getSource() == mnAbout){JOptionPane.showMessageDialog(null, "www.campusevo.com");
}
         if(event.getSource() == newcontact){buddyName.setVisible(true);addBuddy.setVisible(true);}

    }//actionPerformed()

public static synchronized void login(String msg, String user){
        try {
            Date now = new Date();
            String currentTime = ChatWindow.df.format(now);
            BufferedWriter log = new BufferedWriter(new FileWriter(user, true));
            log.write(currentTime);
            log.newLine();
            log.write(msg);
            log.newLine();
            log.flush();
            log.close();
        } catch (IOException e) { }
    }
}