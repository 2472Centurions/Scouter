import java.awt.*;
import java.awt.print.*;
import java.awt.Frame.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame.*;
import java.util.*;
import java.io.*;
/**
 * 
 * Integer.toHexString(int i) is pertinent to write method (returns a string)
 * 
 */
public class ScoutRecordAndWrite implements KeyListener, MouseListener, ActionListener, WindowListener
{
    boolean endgame, ctrlDown, noSpaceYet;
    int selnum, checksave, spaceOfs, spacePlace, matchnum, sel;
    ArrayList<Integer> stepSize;
    String team, teamin, metricin, path, os, match;
    ArrayList<PanelData> panels;
    ArrayList<String> validteams, metrics, matches;
    ArrayList<ArrayList<String>> ewwGross;
    JFrame f2;
    JPanel omni, main, sub;
    JButton b1,b2;
    JMenuBar smb;
    JMenu smenu, sedit;
    JMenuItem ssavejoke, sread, send, sundo;
    JComboBox[] cbarray;
    JComboBox teams;

    File file1;
    FileWriter fw1;
    BufferedWriter bw1;
    FileReader fr1;
    BufferedReader br1;
    public ScoutRecordAndWrite()
    {
        panels = new ArrayList<PanelData>();
        validteams = new ArrayList<String>();
        ewwGross = new ArrayList<ArrayList<String>>();
        matches = new ArrayList<String>();
        os = System.getProperty("os.name");
        detPath();
        getTeams();
        getTeamsFromSchedule();
        getMetrics();

        f2 = new JFrame("Record And Write");
        f2.addWindowListener(this); //this checks whether the window is closed, among other things
        f2.setBounds(500,25,500,60*metrics.size());
        f2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f2.setResizable(false);
        f2.setFont(new Font("Courier New", Font.PLAIN, 11));
        Container c1 = f2.getContentPane();

        //the next few lines define the menu bar and add listeners
        smb = new JMenuBar();
        smenu = new JMenu("File");
        ssavejoke = new JMenuItem("Save");
        ssavejoke.addActionListener(this);
        ssavejoke.setToolTipText("The program saves on close, this doesn't do anything");
        sread = new JMenuItem("Read");
        sread.addActionListener(this);
        sread.setToolTipText("Opens the reading program");
        send = new JMenuItem("End");
        send.addActionListener(this);
        send.setToolTipText("Ends the program");
        smenu.add(ssavejoke);
        smenu.add(sread);
        smenu.add(send);
        smb.add(smenu);

        sedit = new JMenu("Edit");
        sundo = new JMenuItem("Undo");
        sundo.addActionListener(this);
        sundo.setToolTipText("<html>I <i>wish</i> this one worked</html>");
        sedit.add(sundo);
        //smb.add(sedit);

        f2.setJMenuBar(smb);  

        b1 = new JButton("Submit");
        //creates an unused button (it was for testing)

        sub = new JPanel(); 
        //sub.add(b1);
        //sub.add(b2);
        //sub is currently unused, might put a label here for readouts

        //ArrayLists defined here

        teamCheck();
        //ensures team is legit

        main = new JPanel();
        main.setLayout(new GridLayout(metrics.size(),0));
        //creates main as a series of smaller panels

        for(int index = 0; index < metrics.size(); index++){
            PanelData pan = new PanelData(stepSize.get(index), /*step size*/
                    metrics.get(index) /*name*/);
            main.add(pan.getpanel());
            panels.add(pan);
        }
        //PanelData classes are created and the panels from them 
        //are added to the screen

        panels.get(0).updateBG(true);
        for(int index = 0; index < panels.size(); index++){
            for(int innerdex = 0; innerdex < 4; innerdex++){
                panels.get(index).getpanel().getComponent(innerdex).addKeyListener(this);
                panels.get(index).getpanel().getComponent(innerdex).addMouseListener(this);
                //puts a KeyListener on every text field and button
            }
        }

        //System.out.println(""+panels.get(0).getpanel().getComponent(1).isFocusable());
        selnum = 0; //first panel is selected

        omni = new JPanel();
        omni.setLayout(new BorderLayout());          
        omni.setSize(400,400);
        omni.add(main,BorderLayout.CENTER);
        omni.add(sub,BorderLayout.SOUTH);
        //creates omni as a center panel and a lower panel and adds
        //main and sub to them

        c1.add(omni);
        if(match != null && sel != -1)f2.show();
        else{
            f2.dispose();
            ScoutMain irrelevantVariableName2 = new ScoutMain();
        }

        //System.out.println("Keep this open, the terminal is used by this program");
    }

    public void mainLoop(){
    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == sread){
            boolean tempbool = saveData();
            if(tempbool){
                ScoutReadAndDisp irrelevantVariableName1 = new ScoutReadAndDisp();
                f2.dispose();
            }
            //switches to ScoutReadAndDisp
        } 
        if(evt.getSource() == ssavejoke){
            //saveData();
        } //saves, but it really doesn't need to
        if(evt.getSource() == send){
            boolean tempbool = saveData();
            //System.out.println(""+tempbool);
            if(tempbool){
                ScoutMain irrelevantVariableName2 = new ScoutMain();
                f2.dispose();
            }
            //ends the program after saving
        } 
        if(evt.getSource() == sundo){
            //panels.get(selnum).reinstateValue();
            //reinstates the last value inputted by buttons (yea right)
            //if you wanna implement a working undo feature, be my guest
            //it's not important enough for me to bother though
        } 
    }

    public void keyPressed(KeyEvent evt){
        if(evt.getKeyCode() == 37){ //left
            panels.get(selnum).minus();
            //decreases selected text field
        } 
        if(evt.getKeyCode() == 38){ //up
            panels.get(selnum).updateBG(false);
            selnum = (panels.size()+selnum-1)%panels.size();
            panels.get(selnum).updateBG(true);
            panels.get(selnum).getpanel().getComponent(1).requestFocusInWindow();
            //cycles "up" through the JPanels in main
        } 
        if(evt.getKeyCode() == 39){ //right
            panels.get(selnum).plus();
            //increases the selected text field
        } 
        if(evt.getKeyCode() == 40){ //down
            panels.get(selnum).updateBG(false);
            selnum = (selnum+1)%panels.size();
            panels.get(selnum).updateBG(true);
            panels.get(selnum).getpanel().getComponent(1).requestFocusInWindow();
            //cycles "down" through the JPanels in main
        } 
        /*if(evt.getKeyCode() == 71){ //g
        getTeamsFromSchedule();
        //checks if ctrl is pressed
        } */
    }

    public void keyReleased(KeyEvent evt){
        testBoxes();
    }

    public void keyTyped(KeyEvent evt){}

    public void mousePressed(MouseEvent evt){
        for(int index = 0; index < panels.size(); index++){
            if(evt.getSource() == panels.get(index).getpanel().getComponent(1)){
                panels.get(selnum).updateBG(false);
                selnum = index;
                panels.get(selnum).updateBG(true);
                //changes the indexed background to the normal color, then changes the index to the clicked index
                //then sets the indexed background to be highlighted
            }
            if(evt.getSource() == panels.get(index).getpanel().getComponent(2)){
                panels.get(selnum).updateBG(false);
                selnum = index;
                panels.get(selnum).updateBG(true);
                //similar
            }
            if(evt.getSource() == panels.get(index).getpanel().getComponent(3)){
                panels.get(selnum).updateBG(false);
                selnum = index;
                panels.get(selnum).updateBG(true);
            }
            //it was easier to copy the first one 3 times than
            //create a big if/for statement
        }
        //updates the highlighted JPanel to the one holding whichever 
        //textbox is in focus
    }

    public void mouseReleased(MouseEvent evt){
        testBoxes();
    }

    public void mouseClicked(MouseEvent evt){}

    public void mouseEntered(MouseEvent evt){}

    public void mouseExited(MouseEvent evt){}

    public void windowActivated(WindowEvent evt){
        //System.out.println("activated");
    }

    public void windowClosed(WindowEvent evt){
        //System.out.println("closed");
    }

    public void windowClosing(WindowEvent evt){
        //System.out.println("closing");
        boolean tempbool = saveData();
        if(tempbool){
            ScoutMain irrelevantVariableName2 = new ScoutMain();
            f2.dispose();
        }
        //saves the game and closes when x button clicked
        //like the one in the top right corner...?
        //it's red? that one?
        //nvm you'll see it
    }

    public void windowDeactivated(WindowEvent evt){
        //System.out.println("deactivated");
    }

    public void windowDeiconified(WindowEvent evt){
        //System.out.println("deiconified");
    }

    public void windowIconified(WindowEvent evt){
        //System.out.println("did you just");
        //System.out.println("bring me back right now");
    }

    public void windowOpened(WindowEvent evt){
        //System.out.println("opened");
    }

    //these are unused, replaced by the getMetrics and getTeams style of method
    public void readConfig(){
    }

    public void setConfig(){
    }

    public boolean saveData(){
        checksave = 0;
        testBoxes(); //this mostly invalidates the checking system,
                     //but the more security the merrier security
        String data = "";
        boolean isInvalidData = false;

        for(int index = 0; index < panels.size(); index++){
            String temp = Integer.toHexString(panels.get(index).getFieldText().length()).toUpperCase();
            if(panels.get(index).getFieldText().length() < 16) data = data + "0";
            data = data + temp;
            //data length
            temp = Integer.toHexString(index+1).toUpperCase();
            if(index < 16) data = data + "0";
            data = data + temp;
            //data type
            data = data + panels.get(index).getFieldText();
            //data
            checksave++;
            //ensures file saved
            int determ = 0;
            try{
                determ = Integer.parseInt(panels.get(index).getFieldText());
            } catch(NumberFormatException nfe){
                isInvalidData = true;
            }
            //ensures that data is numbers
        }
        //System.out.println(data);

        //System.out.println(""+team.length());
        if(team != null && team.length() != 0 && !(isInvalidData)){
            file1 = new File(path+"Qualification "+(matchnum+1)+" - "+team+".txt");
            try{
                fw1 = new FileWriter(file1);
                bw1 = new BufferedWriter(fw1);
                bw1.write(data+"00");
                bw1.close();
                System.out.println("file saved");
            } catch(IOException ioex){ System.out.println("something went wrong with file writing");}
        }
        //saves the data if the data is valid

        //System.out.println("checksave: "+checksave+" panels.size(): "+panels.size());
        if(isInvalidData){
            System.out.println("non-integer characters found");
            System.out.println("revise before closing");
            return false;
        } else if(checksave == panels.size() || team.length() == 0)return true;
        else return false;
        //complains if data is invalid, otherwise returns an appropriate value,
        //true if sucessful or there's no team (for testing), false if not
        //these checks are mostly superfluous, but it's like fire sprinklers in an aquarium
        //if it happens, it's nice to have it
    }

    public void getMetrics(){
        //see SCoutRecordAndWrite 478-479
        file1 = new File(path+"metrics.txt");
        metricin = "h"; //placeholder
        metrics = new ArrayList<String>();
        stepSize = new ArrayList<Integer>();
        int loopsRun = 0;

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            //br1.mark(12000);

            while(metricin != null){
                metricin = br1.readLine();
                noSpaceYet = true;
                spaceOfs = 0;
                if(metricin != null){
                    for(int index = 0; index < metricin.length(); index++){
                        if(noSpaceYet)spaceOfs++;
                        if(Character.isWhitespace(metricin.charAt(index))){
                            noSpaceYet = false;
                            spacePlace = index;
                        }
                        //System.out.println(metricin.substring(index,index+1)+" "+noSpaceYet);
                    }

                    if(metricin.length() > 0)metrics.add(metricin.substring(0,spacePlace));
                    if(metricin.length() > 0)stepSize.add(Integer.parseInt(metricin.substring(spacePlace+1,metricin.length())));
                    //if(metricin.length() > 0)System.out.println(metricin.substring(0,spacePlace)+"<--");
                }
            }
            br1.close();
        } catch(IOException ioex){System.out.println("failure in config");}
    }

    public void getTeams(){
        //see ScoutMain 183-187
        file1 = new File(path+"validteams.txt");
        teamin = "h"; //placeholder

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            while(teamin != null){
                teamin = br1.readLine();
                if(teamin != null)validteams.add(teamin);
                if(teamin != null)System.out.println(teamin);
            }
            br1.close();
            if(validteams.size() == 0)System.out.println("no file validteams.txt detected");
        } catch(IOException ioex){System.out.println("failure in config");}

    }

    public void getTeamsFromSchedule(){
        file1 = new File(path+"frcschedule.txt");
        teamin = "h";

        /*format:
         * match number 1
         * team 1
         * team 2
         * team 3
         * team 4
         * team 5
         * team 6
         * match number 2 
         * team 1 
         * team 2 
         * etc.
         **/

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            teamin = br1.readLine();
            while(teamin != null){
                //if(teamin != null)System.out.println(teamin);
                matches.add(teamin);
                ArrayList<String> innerGross = new ArrayList<String>();
                for(int index = 0; index < 6; index++){
                    teamin = br1.readLine();
                    if(teamin != null)innerGross.add(teamin);
                    if(teamin != null && index == 5) ewwGross.add(innerGross);
                    if(teamin == null && index < 5) System.out.println("improper formatting in frcschedule.txt");
                    //if(teamin != null)System.out.println(teamin);
                }
                teamin = br1.readLine();
            }
            br1.close();
        }catch(IOException ioex){System.out.println("failure in schedule config");}
    }

    public void teamCheck(){
        boolean invalid = true;
        matchnum = 0;
        //this should be supplemented with a JOptionPane with a ComboBox (and it was)
        while(invalid){
            boolean isNumber = true;
            JOptionPane jop = new JOptionPane();
            match = jop.showInputDialog(null,"Input match number","Team Number",JOptionPane.PLAIN_MESSAGE);
            /*try{
            int i = Integer.parseInt(match);
            } catch(NumberFormatException nfe){
            isNumber = false;
            System.out.println("enter a valid number");
            }*/
            boolean isGoodString = false;
            for(int index = 0; index < matches.size();index++){
                //System.out.println("match: "+match);
                //System.out.println("matches: "+matches.get(index));

                try{
                    if(Integer.parseInt(match) == index+1){
                        isGoodString = true;
                        matchnum = index;
                        //System.out.println("matchnum: "+matchnum);
                    }
                } catch(NumberFormatException nfe){
                    if(match != null && matches.get(index).compareTo(match) == 0){
                        //you have to input either <match number> or
                        //Qualification <match number>
                        isGoodString = true;
                        matchnum = index;

                    }
                    if(match != null)System.out.println("difference: "+matches.get(index).compareTo(match));
                }
                //System.out.println("isGoodString: "+isGoodString);
                if((/*isNumber ||*/ isGoodString) && matchnum > -1 && matchnum < matches.size()){
                    invalid = false;
                } //else {ScoutErrorBox seb = new ScoutErrorBox("Enter a valid match number", 0);}
            }
            if(match == null){
                invalid = false;
            }
        }

        if(match != null){
            boolean unselected = true;
            while(unselected){
                teams = new JComboBox();
                teams.addItem("Select A Team");
                for(int index = 0; index < 6; index++){
                    teams.addItem(ewwGross.get(matchnum).get(index));
                }

                sel = JOptionPane.showConfirmDialog(null,teams,"Select Team",JOptionPane.PLAIN_MESSAGE);
                System.out.println(sel);
                if(teams.getSelectedIndex() != 0){
                    team = teams.getSelectedItem().toString();
                    System.out.println(team);
                    unselected = false;
                }
                if(sel == -1){
                    unselected = false;
                }
            }
        }
    }

    public void scheduleCheck(){
    }

    public void detPath(){
        //see ScoutMain 208-213
        File fil1 = new File("anchor.txt");
        String temp = fil1.getAbsolutePath();
        String makesItWork = System.getProperty("sun.boot.class.path");
        int reduc = 10;
        if(System.getProperty("sun.boot.class.path").indexOf("BlueJ") > -1) reduc = 18;
        int direcStart = (temp.length() - reduc);
        System.out.println(temp);
        /*if(os.indexOf("Windows") >= 0)path = temp.substring(0,direcStart)+"Scouter Config and Data\\";
        if(os.indexOf("Linux") >= 0){
        path = temp.substring(0,direcStart)+"Scouter Config and Data\\";
        }*/
        path = temp.substring(0,direcStart)+"Scouter Config and Data"+File.separator;
        System.out.println(path);
    }

    public void testBoxes(){
        //letter removal, so that input can be made to the textfields
        //with the data in them without breaking the code with letters
        //this is in keyReleased so it happens *after* any character enters the text field
        
        //the input comes from the text field in focus

        
        //the output starts as an empty string

        char[] valNums = {'1','2','3','4','5','6','7','8','9','0'};
        //the valid characters are placed in this array
        for(int outerdex = 0; outerdex < panels.size(); outerdex++){
            boolean valid = false;
            String strIn = panels.get(outerdex).getFieldText();
            String strOut = "";

            for(int index = 0; index < strIn.length(); index++){
                valid = false;//valid needs to be reset each time, makes more
                              //formatting sense to do it here

                for(int innerdex = 0; innerdex < 10; innerdex++){
                    //each letter in the input is tested against the valid characters
                    if(strIn.charAt(index) == valNums[innerdex] /*&&
                    (index != 0 || innerdex != 9)*/){
                        valid = true;
                    }
                }

                if(valid){
                    //if the character is valid it gets added to the output
                    strOut = strOut + strIn.substring(index,index+1);
                }
            }
            System.out.println(panels.get(outerdex).getFieldText());
            if(Integer.parseInt(strOut) > 200){
                strOut = "200";
            }

            panels.get(outerdex).setFieldText(strOut,valid);
        }
        //the text field is set to the output
    }
}
