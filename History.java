
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yao
 */
public class History extends Frame{
    private TextArea textarea;
    public History(String receiver){
        setLayout(null);
        setResizable(false);

        textarea = new TextArea("", 7, 45,TextArea.SCROLLBARS_BOTH);
        textarea.setBounds(20, 50, 330, 340);
        add(textarea);

        setSize(400, 400);
        setTitle("Chat Log");
        setVisible(true);
        textarea.setEditable(false);
        setBackground(Color.gray);

        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(
                WindowEvent e){
                    setVisible(false);
                }
            }
        );

        readHistory(receiver);
    }
    public void readHistory(String receiver){
        File file = new File(receiver);
        if (!file.exists()) {
            System.out.println("Log.txt does not exist.");
            return;
        }
        if (!(file.isFile() && file.canRead())) {
            System.out.println(file.getName() + " cannot be read from.");
            return;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            char current;
            while (fis.available() > 0) {
                current = (char) fis.read();
                this.textarea.append(Character.toString(current));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
