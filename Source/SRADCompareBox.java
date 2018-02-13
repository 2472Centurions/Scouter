import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class SRADCompareBox implements ActionListener
{
    JFrame f1;
    JPanel omni, main, sub;
    JComboBox comp1, comp2, metricbox;
    JButton compare;
    ArrayList<String> teams, metrics, values;
    int t;
    public SRADCompareBox(ArrayList<String> teamsIn, ArrayList<String> metricsIn, ArrayList<String> valuesIn)
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int)d.getHeight();
        int width = (int)d.getWidth();

        f1 = new JFrame("Comparison");
        f1.setBounds((int)(width/2-175),(int)(height/2-100),350,200);
        f1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f1.setResizable(true);
        f1.setFont(new Font("Courier New", Font.PLAIN, 11));
        Container c1 = f1.getContentPane();

        teams = teamsIn;
        metrics = metricsIn;
        values = valuesIn;

        comp1 = new JComboBox();
        comp1.addItem("Team 1");
        for(int index = 0; index < teams.size()/metrics.size(); index++){
            comp1.addItem(teams.get((index)*metrics.size()));
        }
        comp1.addActionListener(this);

        comp2 = new JComboBox();
        comp2.addItem("Team 2");
        for(int index = 0; index < teams.size()/metrics.size(); index++){
            comp2.addItem(teams.get((index)*metrics.size()));
        }
        comp2.addActionListener(this);

        metricbox = new JComboBox();
        metricbox.addItem("Metric");
        metricbox.addItem("Total Points");
        for(int index = 0; index < metrics.size(); index++){
            metricbox.addItem(metrics.get(index));
        }
        metricbox.addActionListener(this);

        compare = new JButton("Compare");
        compare.addActionListener(this);

        main = new JPanel();
        main.add(comp1);
        main.add(comp2);
        main.add(metricbox);

        sub = new JPanel();
        sub.add(compare);

        omni = new JPanel();
        omni.setLayout(new BorderLayout());
        omni.add(main,BorderLayout.CENTER);
        omni.add(sub,BorderLayout.SOUTH);

        //omni.setSize(omni.getPreferredSize());
        t = metricbox.getPreferredSize().width;
        System.out.println(""+t);
        f1.setSize(200+t/2,200);
        omni.setSize(200+t/2,200);
        omni.validate();

        f1.add(omni);
        f1.show();
    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == compare){
            if(comp1.getSelectedIndex()  > 0 && comp2.getSelectedIndex() > 0 && metricbox.getSelectedIndex() > 0){
                int comp1Index = (comp1.getSelectedIndex()-1)*metrics.size();
                int comp2Index = (comp2.getSelectedIndex()-1)*metrics.size();
                int metricIndex = (metricbox.getSelectedIndex()-1);
                int comp1Out = 0;
                int comp2Out = 0;
                String sign = " = ";

                if(metricIndex > 0){
                    comp1Out = Integer.parseInt(values.get(comp1Index + metricIndex-1));
                    comp2Out = Integer.parseInt(values.get(comp2Index + metricIndex-1));
                    if(comp1Out > comp2Out) sign = " > ";
                    if(comp1Out < comp2Out) sign = " < ";
                } else {
                    for(int index = 0; index < metrics.size(); index++){
                        comp1Out = comp1Out + Integer.parseInt(values.get(comp1Index + index));
                    }
                    for(int index = 0; index < metrics.size(); index++){
                        comp2Out = comp2Out + Integer.parseInt(values.get(comp2Index + index));
                    }
                    if(comp1Out > comp2Out) sign = " > ";
                    if(comp1Out < comp2Out) sign = " < ";
                }

                //System.out.println(""+comp1Out+sign+comp2Out);
                String printcomp = " has an equal score in ";
                if(comp1Out > comp2Out){
                    printcomp = " has a higher score in ";
                }
                if(comp1Out < comp2Out){
                    printcomp = " has a lower score in ";
                }
                String printcompraw = comp1Out+sign+comp2Out;
                String printcompout = "<html>"+comp1.getSelectedItem()+printcomp+metricbox.getSelectedItem()+" compared to "+comp2.getSelectedItem()+"</html>";
                int lastOutsideIndex = 0;
                System.out.println(String.valueOf(comp1Out).length()+String.valueOf(comp2Out).length()+3);
                /*for(int index = 0; index < printcompout.length(); index++){
                    if(printcompout.charAt(index) == ' ' && lastOutsideIndex > 35){
                        System.out.println("yea that's a space for me dawg "+index);
                        printcompout = printcompout.substring(0,index)+"<br>"+printcompout.substring(index+1,printcompout.length());
                        lastOutsideIndex = 0;
                    }
                  lastOutsideIndex++;
                }*/
                f1.dispose();
                ScoutErrorBox seb = new ScoutErrorBox(printcompraw,printcompout,1);
            } else{
                System.out.println("Put in some teams");
            }
        }
    }
}