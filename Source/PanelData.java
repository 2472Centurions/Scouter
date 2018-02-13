import java.awt.*;
import java.awt.print.*;
import java.awt.Frame.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import javax.swing.JFrame.*;
import javax.swing.text.*;
import java.util.*;
import java.io.*;
public class PanelData implements ActionListener
{
    JPanel panel;
    JButton plus, minus;
    JLabel metric;
    JTextField data;
    JFormattedTextField dataa;
    NumberFormat numfor;
    NumberFormatter numform;
    boolean highlight;
    SpinnerNumberModel spinModel;
    JSpinner spin;
    Color defaultc;
    int stepsize,max,min;
    String prevVal;

    public PanelData(int step,String in)
    {
        /*This class represents each of the panels that appear in the 
         *GUI of ScoutReadAndWrite.
         */
        
        panel = new JPanel();
        plus = new JButton("+");
        plus.addActionListener(this);
        minus = new JButton("-");
        minus.addActionListener(this);
        metric = new JLabel(in); //the variable label
        
        /*spinModel = new SpinnerNumberModel(0,0,200,step);
        spin = new JSpinner(spinModel);
        numfor = NumberFormat.getInstance();
        numform = new NumberFormatter(numfor);
        numform.setValueClass(Integer.class);
        numform.setMinimum(0);
        numform.setMaximum(Integer.MAX_VALUE);
        numform.setAllowsInvalid(false);
        numform.setCommitsOnValidEdit(true);
        dataa = new JFormattedTextField(numform);*/
        data = new JTextField("0",10); //the data field
        preserveValue(); //this doesn't really do anything
        //dataa.setSize(100,20);
        
        panel.add(metric);
        //panel.add(spin);
        panel.add(data);
        panel.add(minus);
        panel.add(plus);
        
        defaultc = panel.getBackground();
        //System.out.println("r "+defaultc.getRed());
        //System.out.println("g "+defaultc.getGreen());
        //System.out.println("b "+defaultc.getBlue());
        stepsize = step;
        min = 0;
        max = 200; //one might want to change this if more points are attainable
    }

    public JPanel getpanel(){
        return panel;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == plus){
            plus();
        }
        if(e.getSource() == minus){
            minus();
        }
    }

    //these two methods allow for button and mouse based interaction with the panels
    public void plus(){
        preserveValue();
        data.setText(""+(int)(Integer.parseInt(data.getText())+stepsize));
        if(Integer.parseInt(data.getText()) >max)data.setText(""+max);
        //int newval = (int)spin.getNextValue();
        //if(newval < (int)spin.getMaximum())spin.setValue("" + newval);
        
    }

    public void minus(){
        preserveValue();
        data.setText(""+(int)(Integer.parseInt(data.getText())-stepsize));
        if(Integer.parseInt(data.getText()) < min)data.setText(""+min);
        //int newval = (int)spin.getPreviousValue();
        //if(newval > (int)spin.getMinimum())spin.setValue("" + newval);
        
    }

    //these methods make it simpler to get the data with the current architecture
    public String getFieldText(){
        //return (String)spin.getValue();
        return data.getText();
    }
    
    public void setFieldText(String in, boolean valid){
        if(valid)preserveValue();
        data.setText(in);
        if(in.length() == 0)data.setText("0");
        
    }

    public String getLabelText(){
        return metric.getText();
    }
    
    //despite my best efforts, these two don't work
    public void preserveValue(){
        prevVal = data.getText();
    }
    
    public void reinstateValue(){
        data.setText(prevVal);
    }

    //highlights the selected background
    //(or dims the deselected background)
    public void updateBG(boolean in){
        highlight = in;
        if(highlight)panel.setBackground(Color.white); 
        else panel.setBackground(defaultc);
        panel.revalidate();//redraws panel graphics
    }
}
