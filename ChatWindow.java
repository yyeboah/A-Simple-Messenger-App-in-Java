
import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

class ChatWindow extends Frame implements Runnable, ActionListener
{
    private Thread thread;
    private String ip; //this will record the ip
    private Button sendButton,logout, history;
    private TextArea textarea1, textarea2;
    private Label typeHereLabel;
    private MenuBar menubar;
    private Menu program,edit,help,background;
    private MenuItem mnExit,mnClear,mnBack,mnHow,mnAbout,mnColour1,mnColour2,mnback1,mnback2;
    private Socket socket;
    private String username;
    private String receiver;
    private InputStream in;
    private OutputStream out;
    private int character;
    private char[] chars = new char[1];
    public static DateFormat df;

    public ChatWindow(String uname,String receive,String inIp)
    {
        username = uname;
        receiver = receive;
        ip = inIp;
        df = new SimpleDateFormat ("E: yyyy.MM.dd  hh:mm:ss");

        setLayout(null);
        setResizable(false);

        menubar = new MenuBar();
        program = new Menu("Java Chat");
        edit = new Menu("Interface");
        help = new Menu("Details");
        background = new Menu(" Background Options");


       // mnExit = new MenuItem("Quit");
        //rogram.add(mnExit);
        //mnExit.addActionListener(this);

        mnBack = new MenuItem("Set window colour to default ");
        edit.add(mnBack);
        mnBack.addActionListener(this);

        mnColour1 = new MenuItem("Set Window colour to cyan ");
        edit.add(mnColour1);
        mnColour1.addActionListener(this);

        mnColour2 = new MenuItem("Set Window colour to green");
        edit.add(mnColour1);
        mnColour2.addActionListener(this);

        mnback1 = new MenuItem("Clear Background colour");
        background.add(mnback1);
        mnback1.addActionListener(this);

        mnback2 = new MenuItem("Set Background colour");
        background.add(mnback2);
        mnback2.addActionListener(this);

       // mnClear = new MenuItem("Refresh message Log");
       // edit.add(mnClear);
       // mnClear.addActionListener(this);

        mnHow = new MenuItem("User Manual ");
        help.add(mnHow);
        mnHow.addActionListener(this);

        mnAbout = new MenuItem("About Waizee! messenger");
        help.add(mnAbout);
        mnAbout.addActionListener(this);

        menubar.add(program);
        menubar.add(edit);
        menubar.add(help);
        menubar.add(background);
        setMenuBar(menubar);

        sendButton = new Button("Send");
        sendButton.setBounds(405, 570, 60, 25);
        add(sendButton);
        sendButton.addActionListener(this);
        sendButton.setBackground(Color.green);

        history = new Button("Check History");
        history.setBounds(405, 530, 125, 25);
        add(history);
        history.addActionListener(this);
        

        textarea1 = new TextArea("", 7, 45,
            TextArea.SCROLLBARS_VERTICAL_ONLY);
        textarea1.setBounds(20, 110, 380, 350);
        textarea1.setEditable(false);
        textarea1.setBackground(Color.white);//***********************8
        add(textarea1);

        typeHereLabel = new Label();
        typeHereLabel.setBounds(20, 470, 100, 20);
        typeHereLabel.setText("Chat Message :");
        add(typeHereLabel);

        logout = new Button("Block!");
        logout.setBounds(470, 570, 60, 25);
        add(logout);
        logout.addActionListener(this);
       



        textarea2 = new TextArea("", 7, 45,
            TextArea.SCROLLBARS_VERTICAL_ONLY);
        textarea2.setBounds(20, 490, 380, 120);
         textarea1.setBackground(Color.white);
        add(textarea2);
        textarea2.setBackground(Color.white);

        setBackground(Color.gray);
        setSize(550, 650);
        setResizable(false);

        setTitle(receiver);
        setVisible(true);
        textarea2.requestFocus();

        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(
                WindowEvent e){
                    setVisible(false);
                }
            }
        );


         try{
                socket = new Socket(ip, 9800);
                //textfield1.setText("Connecting....");

                in = socket.getInputStream();
                out = socket.getOutputStream();

                thread = new Thread(this);
                thread.start();
            }
            catch (IOException ioe){
                textarea1.setText("Intercom 1 must be running and\n"
                + "accessible before running Intercom 2.");
                //textfield1.setText("Not connected");
            }
            catch (Exception e){
                textarea1.setText(e.getMessage());
            }
        if(socket != null && socket.isConnected()){}
        try{
               String temp = uname + "\n";
                byte buffer[] = temp.getBytes();
                out.write(buffer);
            }
        catch(Exception ex){textarea1.setText(ex.getMessage());}//username sent

        try{
               String temp = receiver + "\n";
               byte buffer[] = temp.getBytes();
               out.write(buffer);
            }
        catch(Exception ex){textarea1.setText(ex.getMessage());}//receiver added




    }//ChatWindow()

    public void run(){
        String instring;
        try {

            BufferedReader BRin = new BufferedReader (new InputStreamReader(in));
            while((instring = BRin.readLine()) != null){
                textarea1.append(instring + "\n");//+ "\n"
                //textarea1.append("\n");
                ChatWindow.login(instring,this.receiver);
            }
        }catch (Exception e)
        {
            textarea1.setText(e.getMessage());
        }
    }//run()

    public void actionPerformed(ActionEvent event)
    {
        if(event.getSource() == sendButton){
            try{
                String str = textarea2.getText() + "\n";
                String str1 = textarea2.getText();
                byte buffer[] = str.getBytes();
                out.write(buffer);
                textarea2.setText("");
                textarea2.requestFocus();
                textarea1.append(username+":  "+str);
                ChatWindow.login(username+":  "+str1, receiver);
            }//sending a message
            catch(Exception ex)
                {textarea1.setText(ex.getMessage());}
        }

        if(event.getSource() == history){
            new History(this.receiver);
        }


        if(event.getSource() == logout){
            try{
                String str = "/quit";
                byte buffer[] = str.getBytes();
                out.write(buffer);
                //Thread.sleep(40);
                in.close();
                out.close();
                socket.close();
                JOptionPane.showMessageDialog(null, "You will no longer be bothered by " + ":"+ receiver);
            }
            catch(Exception ex)
                {textarea1.setText(ex.getMessage());}
        }

        if(event.getSource() == mnColour1){setBackground(Color.cyan);}
        if(event.getSource() == mnBack){setBackground(Color.darkGray);}
        if(event.getSource() == mnColour2){setBackground(Color.green);}

        if(event.getSource() == mnback1){textarea1.setBackground(Color.white);textarea2.setBackground(Color.white);}
        if(event.getSource() == mnback2){textarea1.setBackground(Color.gray);textarea2.setBackground(Color.gray);}
 if(event.getSource() == mnHow){JOptionPane.showMessageDialog(null, "The server must be running first.If you are not sure of the servers IP , contact on the developer ");
}
        if(event.getSource() == mnAbout){JOptionPane.showMessageDialog(null, "Programmed and designed by yao");
}

   // if(event.getSource() == mnExit){JOptionPane.showMessageDialog(null, "You are closing the chatwindow © SYZ");System.exit(0);}
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