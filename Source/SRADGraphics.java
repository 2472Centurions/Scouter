import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class SRADGraphics extends JPanel
{
    int graphType, valueSection, dimen, c1, c2, teamcount, mouseX, mouseY, bw;
    double widthMod;
    ArrayList<String> values, teams, validTeams, varnames, metrics;
    JScrollBar scrolljsb;
    Image bg;
    public SRADGraphics(ArrayList<String> valsIn, ArrayList<String> teamsIn, ArrayList<String> valTeamsIn, ArrayList<String> varnamesIn, ArrayList<String> metricsIn, int type, int section, int dimenIn, double widthMultip, JScrollBar jsbIn)
    {
        System.out.println("graphics begins");
        if(valsIn.size()%metricsIn.size() == 0){
            values = valsIn; //all the variable data
        } else{
            //ScoutErrorBox seb = new ScoutErrorBox("data is incomplete",0);
            System.out.println("data is incomplete");
        }
        teams = teamsIn; //all the relevant teams, parallel to the values list
        validTeams = valTeamsIn; //all the team numbers
        varnames = varnamesIn; //all the relevant variable names, parallel to the values list
        metrics = metricsIn; //all the variable names
        graphType = type; //the type of graph being drawn, 0, 1, or 2
        valueSection = section; //the section of the data to read, starts at 0
        dimen = dimenIn; //the height & width variable for the panel

        bg = Toolkit.getDefaultToolkit().getImage("graphbg.png");

        //constants
        c1 = (int)dimen/8; //height divided by 8
        c2 = (int)dimen/12; //height divided by 12
        teamcount = teams.size()/metricsIn.size(); //number of teams
        widthMod = widthMultip; //width multiplier

        scrolljsb = jsbIn;
    }

    public void paint (Graphics g)         
    {
        super.paint(g);
        g.setFont(new Font("Courier New", Font.PLAIN, 11));
        /*System.out.println("paint begins");
        System.out.println("mousex: " + mouseX);
        System.out.println("mousey: " + mouseY);*/
        /*if(valueSection%2 == 0)g.setColor(new Color(237,237,237));
        else g.setColor(Color.LIGHT_GRAY);*/
        g.drawImage(bg,75,0,(int)(11*widthMod*c2-75),dimen,null);
        if(graphType == 0){
            ArrayList<Integer> finalVals = new ArrayList<Integer>();
            for(int index = 0; index < teamcount; index++){
                int totalscore = 0;
                for(int innerdex = 0; innerdex < metrics.size(); innerdex++){
                    totalscore = totalscore + Integer.parseInt(values.get(index*metrics.size() + innerdex));
                }
                finalVals.add(totalscore); //total score for each team, need to create parallel team list
            }

            int highestval = 0;
            for(int index = 0; index < finalVals.size(); index++){
                if(finalVals.get(index) > highestval) highestval = finalVals.get(index);
            }
            while(highestval%50 != 0){
                highestval++;
            }

            g.setColor(Color.BLACK);
            g.drawLine(c1,c2,c1,11*c2);
            for(int index = 0; index < 11; index++){
                g.drawLine(c1-5, (index+1)*c2, c1, (index+1)*c2);
            }

            for(int index = 0; index < 11; index++){
                g.drawString(""+(highestval*(10-index))/10,(int)(75/2 - (""+(highestval*(10-index))/10).length()*4-1),(index+1)*c2+4);
            }

            g.drawLine(c1,11*c2,(int)(11*widthMod*c2),11*c2);

            //determine where the bars should start here
            int xLength = (int)(11*widthMod*c2-75);
            int temcount = 1;
            if(finalVals.size() > 0) temcount = finalVals.size();
            int barWidth = (int)(xLength/temcount - 20);
            if(barWidth < 0) barWidth = barWidth + 10;
            if(barWidth > 100) barWidth = 100;

            ArrayList<Integer> barStartX = new ArrayList<Integer>();
            for(int index = 0; index < finalVals.size(); index++){
                barStartX.add(75 + 20*(index+1) + barWidth*index);
            }

            g.setColor(Color.gray);
            for(int index = 0; index < finalVals.size(); index++){
                g.fillRect(barStartX.get(index),(int)(50 + (500-(finalVals.get(index)*500)/highestval)),barWidth,(finalVals.get(index)*500)/highestval);
            }

            g.setColor(Color.black);
            boolean noDupeDraw = true;
            for(int index = 0; index < temcount; index++){
                if(teams.size() > 0 && ((mouseX < 75 + 20*(index+1) + barWidth*(index+1) && noDupeDraw) || teams.get(index*metrics.size()) == null)){
                    String t = teams.get(index*metrics.size());
                    g.drawString(t, (int)(100*widthMod) - (t.length()*2+(t.length()-1)*1) + scrolljsb.getValue(), 25); 
                    noDupeDraw = false;
                }
            }
                  
        }

        if(graphType == 1){
            ArrayList<Integer> finalVals = new ArrayList<Integer>();
            for(int index = 0; index < metrics.size(); index++){
                finalVals.add(Integer.parseInt(values.get(index + valueSection*metrics.size())));

                //total score for each team, need to create parallel team list
            }

            int highestval = 0;
            for(int index = 0; index < finalVals.size(); index++){
                if(finalVals.get(index) > highestval) highestval = finalVals.get(index);
            }
            while(highestval%50 != 0 || highestval == 0){
                highestval++;
            }

            g.setColor(Color.BLACK);

            g.drawLine(c1,c2,c1,11*c2);
            for(int index = 0; index < 11; index++){
                g.drawLine(c1-5, (index+1)*c2, c1, (index+1)*c2);
            }

            for(int index = 0; index < 11; index++){
                g.drawString(""+(highestval*(10-index))/10,(int)(75/2 - (""+(highestval*(10-index))/10).length()*4-1),(index+1)*c2+4);
            }

            g.drawLine(c1,11*c2,(int)(11*widthMod*c2),11*c2);
            
            //determine where the bars should start here
            int xLength = (int)(11*widthMod*c2-75);
            int metcount = 1;
            if(finalVals.size() > 0) metcount = finalVals.size();
            int barWidth = (int)(xLength/metcount - 20);
            if(barWidth < 0) barWidth = barWidth + 10;
            if(barWidth > 100) barWidth = 100;

            ArrayList<Integer> barStartX = new ArrayList<Integer>();
            for(int index = 0; index < finalVals.size(); index++){
                barStartX.add(75 + 20*(index+1) + barWidth*index);
            }

            g.setColor(Color.gray);
            for(int index = 0; index < finalVals.size(); index++){
                g.fillRect(barStartX.get(index),(int)(50 + (500-(finalVals.get(index)*500)/highestval)),barWidth,(finalVals.get(index)*500)/highestval);
            }

            g.setColor(Color.black);
            boolean noDupeDraw = true;
            for(int index = 0; index < metcount; index++){
                if((mouseX < 75 + 20*(index+1) + barWidth*(index+1) && noDupeDraw)/* || metrics.get(index) == null*/){
                    String m = metrics.get(index);
                    g.drawString(m, (int)(300*widthMod) - (m.length()*2+(m.length()-1)*1), 25); 
                    noDupeDraw = false;
                }
            }
            String t = teams.get(valueSection*metrics.size());
            //System.out.println("string t: "+t);
            g.drawString(t, (int)(300*widthMod) - (t.length()*2+(t.length()-1)*1), 575); 
              
        }

        if(graphType == 2){
            ArrayList<Integer> finalVals = new ArrayList<Integer>();
            for(int index = 0; index < teamcount; index++){
                finalVals.add(Integer.parseInt(values.get(index*metrics.size() + valueSection)));

                //total score for each team, need to create parallel team list
            }

            int highestval = 0;
            for(int index = 0; index < finalVals.size(); index++){
                if(finalVals.get(index) > highestval) highestval = finalVals.get(index);
            }
            while(highestval%50 != 0){
                highestval++;
            }

            g.setColor(Color.BLACK);

            g.drawLine(c1,c2,c1,11*c2);
            for(int index = 0; index < 11; index++){
                g.drawLine(c1-5, (index+1)*c2, c1, (index+1)*c2);
            }

            for(int index = 0; index < 11; index++){
                g.drawString(""+(highestval*(10-index))/10,(int)(75/2 - (""+(highestval*(10-index))/10).length()*4-1),(index+1)*c2+4);
            }
            
            g.drawLine(c1,11*c2,(int)(11*widthMod*c2),11*c2);
            
            //determine where the bars should start here
            int xLength = (int)(11*widthMod*c2-75);
            int metcount = 1;
            if(finalVals.size() > 0) metcount = finalVals.size();
            int barWidth = (int)(xLength/metcount - 20);
            bw = barWidth;
            if(barWidth < 0) barWidth = barWidth + 10;
            if(barWidth > 100) barWidth = 100;

            ArrayList<Integer> barStartX = new ArrayList<Integer>();
            for(int index = 0; index < finalVals.size(); index++){
                barStartX.add(75 + 20*(index+1) + barWidth*index);
            }

            g.setColor(Color.gray);
            for(int index = 0; index < finalVals.size(); index++){
                g.fillRect(barStartX.get(index),(int)(50 + (500-(finalVals.get(index)*500)/highestval)),barWidth,(finalVals.get(index)*500)/highestval);
            }

            g.setColor(Color.black);
            boolean noDupeDraw = true;
            for(int index = 0; index < metcount; index++){
                if((mouseX < 75 + 20*(index+1) + barWidth*(index+1) && noDupeDraw)/* || metrics.get(index) == null*/){
                    String m = teams.get(index*metrics.size());
                    g.drawString(m, (int)(300*widthMod) - (m.length()*2+(m.length()-1)*1), 25); 
                    noDupeDraw = false;
                }
            }
            String t = metrics.get(valueSection);
            //System.out.println("string t: "+t);
            g.drawString(t, (int)(300*widthMod) - (t.length()*2+(t.length()-1)*1), 575);
        }
    }

    public void sendVars(int mX, int mY){
        mouseX = mX;
        mouseY = mY;
    }
    
    public void incSection(){
        valueSection++;
    }
    
    public void decSection(){
        valueSection--;
    }
    
    public int getBarWidth(){
        return bw;
    }
}
