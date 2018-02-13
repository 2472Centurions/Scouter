import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class SRADGrapherClass implements ActionListener,AdjustmentListener, MouseMotionListener
{
    JFrame f2;
    JPanel main, sub, viewpanel, graphpanei;
    JScrollPane scroll;
    JButton b1;
    ArrayList<String> teams, validTeams, values, varnames, metrics;
    ArrayList<JPanel> graphpanels;
    ArrayList<SRADGraphics> graphics;
    SRADGraphics sg11;
    JScrollBar scrolljsb;

    JMenuBar smb;
    JMenu smenu;
    JMenuItem ssearch, scompare;

    int type;
    double widthMod;

    public SRADGrapherClass(ArrayList<String> valsIn, ArrayList<String> teamsIn, ArrayList<String> allTeams, ArrayList<String> metricsIn, ArrayList<String> allMetrics, int typeIn)
    {
        f2 = new JFrame("Graphical Representation");
        f2.setBounds(200,100,650,682);
        f2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        f2.setResizable(true);
        f2.setFont(new Font("Courier New", Font.PLAIN, 11));
        Container c1 = f2.getContentPane();

        System.out.println("it begins");

        teams = teamsIn;
        validTeams = allTeams;
        values = valsIn;
        varnames = metricsIn;
        metrics = allMetrics;
        graphpanels = new ArrayList<JPanel>();
        graphics = new ArrayList<SRADGraphics>();

        smb = new JMenuBar();
        smenu = new JMenu("Graph");
        ssearch = new JMenuItem("Search");
        ssearch.addActionListener(this);
        ssearch.setToolTipText("Jumps to the location of the entered team number");
        scompare = new JMenuItem("Compare");
        scompare.addActionListener(this);
        scompare.setToolTipText("Compares two team's scores in a metric");

        smenu.add(ssearch);
        smenu.add(scompare);
        smb.add(smenu);
        f2.setJMenuBar(smb);

        sub = new JPanel();
        //sub.add(b1);

        main = new JPanel();

        scroll = new JScrollPane();
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolljsb = scroll.getHorizontalScrollBar();
        scrolljsb.addAdjustmentListener(this);

        type = typeIn;

        /*
         *create JMenu for searching team numbers
         */

        int dimen = 600;
        if(type == 0){ //all teams from least total points to most total points
            graphpanels.clear();
            graphics.clear();
            JPanel graphpanel = new JPanel();
            graphpanel.setLayout(new BorderLayout());
            graphpanel.setSize(dimen*3,dimen);
            graphpanel.setPreferredSize(new Dimension(dimen*3,dimen));
            SRADGraphics sg1 = new SRADGraphics(values,teams,validTeams,varnames,metrics,type,0,dimen,3,scrolljsb);
            sg1.addMouseMotionListener(this);
            graphpanel.add(sg1,BorderLayout.CENTER);
            graphpanels.add(graphpanel);
            graphics.add(sg1);
            ssearch.setToolTipText("Jumps to the location of the entered team number");
            //System.out.println("Type 0");
        }
        if(type == 1){ //each graph is one team listing all metrics
            graphpanels.clear();
            graphics.clear();
            for(int index = 0; index < teams.size()/metrics.size(); index++){
                JPanel graphpanel = new JPanel();
                graphpanel.setLayout(new BorderLayout());
                graphpanel.setSize(dimen,dimen);
                graphpanel.setPreferredSize(new Dimension(dimen,dimen));
                SRADGraphics sg1 = new SRADGraphics(values,teams,validTeams,varnames,metrics,type,index,dimen,1,scrolljsb);
                sg1.addMouseMotionListener(this);
                graphpanel.add(sg1,BorderLayout.CENTER);
                graphpanels.add(graphpanel);
                graphics.add(sg1);
            }
            ssearch.setToolTipText("Jumps to the location of the entered team number");
            //System.out.println("Type 1");
        }
        if(type == 2){ //each graph is one metric listing all teams
            graphpanels.clear();
            graphics.clear();
            widthMod = 1;
            int teamcount = teams.size()/metrics.size();
            if(teamcount > 10){
                widthMod = widthMod + 0.1*(teamcount - 10);
            }
            for(int index = 0; index < metrics.size(); index++){
                JPanel graphpanel = new JPanel();
                graphpanel.setLayout(new BorderLayout());
                graphpanel.setSize((int)(dimen*widthMod),dimen);
                graphpanel.setPreferredSize(new Dimension((int)(dimen*widthMod),dimen));
                SRADGraphics sg1 = new SRADGraphics(values,teams,validTeams,varnames,metrics,type,index,dimen,widthMod,scrolljsb);
                sg1.addMouseMotionListener(this);
                graphpanel.add(sg1,BorderLayout.CENTER);
                graphpanels.add(graphpanel);
                graphics.add(sg1);
            }
            ssearch.setToolTipText("Jumps to the location of the entered metric");
            //System.out.println("Type 2");
        }

        if(graphpanels.size()>0){
            viewpanel = new JPanel();
            viewpanel.setLayout(new GridLayout(0,graphpanels.size()));
            for(int index = 0; index < graphpanels.size(); index++){
                viewpanel.add(graphpanels.get(index));
            }
            scroll.setViewportView(viewpanel);
            f2.addMouseMotionListener(this);
        }
        /*graphpanei = new JPanel();
        graphpanei.setLayout(new BorderLayout()); 
        graphpanei.setSize(dimen*3,dimen);
        sg11 = new SRADGraphics(values,teams,metrics,type,0,dimen);
        graphpanei.add(sg11,BorderLayout.CENTER);
        main.add(viewpanel);
        graphpanei.setPreferredSize(new Dimension(1800,600));
        scroll = new JScrollPane(graphpanei);*/
        c1.add(scroll);
        f2.show(); 
        //sg11.repaint();
        /*for(int index = 0; index < graphics.size(); index++){
        graphics.get(index).repaint();
        }*/
        //System.out.println("scroll bar: "+scroll.getHorizontalScrollBar().getValue());
        for(int index = 0; index < graphics.size(); index++){
            graphics.get(index).repaint();
        }
    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == ssearch){
            JOptionPane jop = new JOptionPane();
            boolean valid = false;
            boolean validInter = false;
            String dialogIn = "default";
            int scrollval;
            int targetValue = 0;
            int targetIndex = 10000;
            while(valid == false){
                valid = false;
                if(type < 2)dialogIn = jop.showInputDialog(null,"Input team number","Search",JOptionPane.PLAIN_MESSAGE);
                else dialogIn = jop.showInputDialog(null,"Input metric name","Search",JOptionPane.PLAIN_MESSAGE);
                if((dialogIn != null && dialogIn != "default") && type < 2){
                    for(int index = 0; index < teams.size(); index += metrics.size()){
                        if(teams.get((teams.size()-index)-1).indexOf(dialogIn) > -1){
                            valid = true;
                            targetIndex = teams.size()/metrics.size()-index;
                            targetIndex--;
                        }
                        System.out.println("comparison: "+teams.get(index).compareTo(dialogIn));
                        System.out.println("index: "+teams.get(index).indexOf(dialogIn));
                    }
                    int barWidth = graphics.get(0).getBarWidth();

                    System.out.println("targetIndex: "+targetIndex);
                    if(type == 0){
                        targetValue = (20+barWidth)*targetIndex;
                        if(targetValue < scrolljsb.getMaximum()){
                            scrolljsb.setValue(targetValue);
                            validInter = true;
                        }
                    }
                    if(type == 1){
                        targetValue = graphpanels.get(0).getWidth()*targetIndex;
                        if(targetValue < scrolljsb.getMaximum()){
                            scrolljsb.setValue(targetValue);
                            validInter = true;
                        }
                    }
                }
                if((dialogIn != null && dialogIn != "default") && type > 1){
                    for(int index = 0; index < metrics.size(); index++){
                        if(metrics.get((metrics.size()-index)-1).indexOf(dialogIn) > -1){
                            valid = true;
                            targetIndex = metrics.size()-index;
                        }
                        //System.out.println(""+metrics.get(index).compareTo(dialogIn));
                    }
                    int barWidth = graphics.get(0).getBarWidth();

                    targetValue = (int)(graphpanels.get(0).getWidth()*targetIndex);
                    System.out.println("targetValue: "+targetValue);
                    if(targetValue < scrolljsb.getMaximum()){
                        scrolljsb.setValue(targetValue);
                        validInter = true;
                    }
                }

                //System.out.println(""+scroll.getHorizontalScrollBar().getMaximum());
                if(valid && validInter)System.out.println("first valid location of "+dialogIn+" found at "+(targetValue));
                else if(targetIndex == 10000)System.out.println("no location found/window exited");
                else if(valid) System.out.println("value exceeds scrollbar");
                else System.out.println("no location found/window exited");
            }
        }

        if(evt.getSource() == scompare){
            SRADCompareBox scb = new SRADCompareBox(teams, metrics, values);
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent evt){
        for(int index = 0; index < graphics.size(); index++){
            graphics.get(index).repaint();
        }
        //System.out.println(""+scrolljsb.getValue());
        if(teams.size() != values.size() || teams.size() != varnames.size() || teams.size()%metrics.size() != 0){
            f2.dispose();
            //custom title, error icon
            String error = "this message should not appear";
            if(teams.size() != values.size())error = "amount of teams does not equal amount of data";
            if(teams.size() != varnames.size() || teams.size()%metrics.size() != 0)error = "metrics do not sync up with data";
            JOptionPane.showMessageDialog(null,
                "<html>There was a data length mismatch:<br>"+error+"</html>",
                "Shit hit the fan",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mouseMoved(MouseEvent evt){
        for(int index = 0; index < graphics.size(); index++){
            graphics.get(index).sendVars(evt.getX(), evt.getY());
        }
        for(int index = 0; index < graphics.size(); index++){
            graphics.get(index).repaint();
        }
        /*System.out.println("size" + graphics.size());
        System.out.println("functions" + evt.getX());*/
    }

    public void mouseDragged(MouseEvent evt){

    }
}
