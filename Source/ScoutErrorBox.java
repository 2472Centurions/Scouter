import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class ScoutErrorBox implements WindowListener, ActionListener
{
    JFrame f1;
    JPanel omni, main, sub, centersTheTextInMain;
    JLabel msg, msg2;
    JButton close;
    public ScoutErrorBox(String message,String message2, int type)
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)d.getHeight();
        int width = (int)d.getWidth();

        if(type == 0)f1 = new JFrame("Error");
        else f1 = new JFrame("Message");
        f1.setBounds(width/2-125,height/2-100,250,200);
        f1.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f1.setResizable(true);
        f1.setFont(new Font("Courier New", Font.PLAIN, 11));
        f1.addWindowListener(this);

        msg = new JLabel(message);
        msg2 = new JLabel(message2);

        close = new JButton("close");
        close.addActionListener(this);

        centersTheTextInMain = new JPanel();
        centersTheTextInMain.add(msg);
        
        main = new JPanel();
        main.setLayout(new GridLayout(2,1));
        main.add(centersTheTextInMain);
        if(msg2.getText().length() > 0)main.add(msg2);

        sub = new JPanel();
        sub.add(close);

        omni = new JPanel();
        omni.setLayout(new BorderLayout());
        omni.setSize(200,100);
        omni.add(main, BorderLayout.CENTER);
        omni.add(sub, BorderLayout.SOUTH);

        f1.add(omni);
        f1.show();
    }

    public void actionPerformed(ActionEvent evt){
        f1.dispose();
    }

    public void windowActivated(WindowEvent evt){
        //System.out.println("activated");
    }

    public void windowClosed(WindowEvent evt){
        //System.out.println("closed");
    }

    public void windowClosing(WindowEvent evt){
        //System.out.println("closing");
        f1.dispose();
    }

    public void windowDeactivated(WindowEvent evt){
        //System.out.println("deactivated");
        f1.dispose();
    }

    public void windowDeiconified(WindowEvent evt){
        //System.out.println("deiconified");
    }

    public void windowIconified(WindowEvent evt){
        //System.out.println("iconified");
    }

    public void windowOpened(WindowEvent evt){
        //System.out.println("opened");
    }
}
