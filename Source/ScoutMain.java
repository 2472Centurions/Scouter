import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
public class ScoutMain implements /**KeyListener, MouseListener,*/ ActionListener/**, Printable*/
{
    JFrame sframe;
    JPanel smain;
    //JButton sb1, sb2;
    JMenuBar smb;
    JMenu smenu, ssetup;
    JMenuItem sread, swrite, steamsfromschedule, snewteam, spath;
    String path, chosenfile;
    ArrayList<String> validTeams;

    FileWriter fw1;
    BufferedWriter bw1;
    FileReader fr1;
    BufferedReader br1;
    public ScoutMain(){
        detPath();
        sframe = new JFrame("Scouter");
        sframe.setBounds(500,300,200,200);
        sframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sframe.setFont(new Font("Courier New", Font.PLAIN, 11));
        //this font sadly doesn't get implemented, but it's nice and monospaced

        Container c = sframe.getContentPane();
        //very important :3

        /**sb1 = new JButton("Read");
        sb1.addActionListener(this);

        sb2 = new JButton("Write");
        sb2.addActionListener(this);
        //maybe switch these to JMenuBar*/
        
        //menubar definitions for a while
        //every menu item should have a tooltip, even if it does nothing or
        //doesn't really need one
        smb = new JMenuBar();
        smenu = new JMenu("File");
        sread = new JMenuItem("Read");
        sread.addActionListener(this);
        sread.setToolTipText("Opens the reading program");
        swrite = new JMenuItem("Write");
        swrite.addActionListener(this);
        swrite.setToolTipText("Opens the writing program");
        smenu.add(sread);
        smenu.add(swrite);
        smb.add(smenu);

        ssetup = new JMenu("Setup");
        steamsfromschedule = new JMenuItem("Get valid teams from schedule");
        steamsfromschedule.addActionListener(this);
        steamsfromschedule.setToolTipText("take a guess");
        snewteam = new JMenuItem("New valid team");
        snewteam.addActionListener(this);
        snewteam.setToolTipText("Inserts a new team into the list of valid teams");
        spath = new JMenuItem("Path");
        spath.addActionListener(this);
        spath.setToolTipText("Prints out the directory the program thinks it's in");
        ssetup.add(steamsfromschedule);
        ssetup.add(snewteam);
        ssetup.add(spath);
        smb.add(ssetup);

        sframe.setJMenuBar(smb);

        smain = new JPanel();          
        smain.setSize(200,200);

        c.add(smain);
        sframe.show();
    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == sread){
            ScoutReadAndDisp srad = new ScoutReadAndDisp();
            sframe.dispose();
            //just opens the reader and disposes the frame
        }
        if(evt.getSource() == swrite){
            ScoutRecordAndWrite sraw = new ScoutRecordAndWrite();
            sframe.dispose();
            //just opens the writer and disposes the frame
        }
        if(evt.getSource() == steamsfromschedule){
            /*
             * this gets teams from the schedule and adds them to the
             * list of valid teams (important if you want to concatenate)
             */
            File file1 = new File(path+"frcschedule.txt");
            String teamin = "h"; //placeholder
            ArrayList<String> scheduleTeams = new ArrayList<String>();

            try{
                fr1 = new FileReader(file1);
                br1 = new BufferedReader(fr1);
                while(teamin != null){
                    teamin = br1.readLine();
                    boolean noExcep = true;
                    try{
                        Integer.parseInt(teamin);
                    } catch(NumberFormatException nfe){
                        noExcep = false;
                    }
                    //makes sure the current value of teamin is an integer
                    boolean isNotAlreadyInTheListOfValidTeams = true;
                    for(int index = 0; index < validTeams.size(); index++){
                        if(teamin != null && validTeams.get(index).compareTo(teamin) == 0){
                            isNotAlreadyInTheListOfValidTeams = false;
                        }
                    }
                    //makes sure the current value of teamin isn't in validteams.txt already

                    if(teamin != null && noExcep && isNotAlreadyInTheListOfValidTeams /*&& teamin.indexOf("a") < 0*/)scheduleTeams.add(teamin);
                    //if the data is good and it's not one of the "qualification"
                    //lines, it gets added to validteams.txt
                    if(teamin != null)System.out.println(teamin);
                }
                br1.close();
                if(validTeams.size() == 0)System.out.println("no file validteams.txt detected");
            } catch(IOException ioex){System.out.println("failure in config");}
            File file2 = new File(path+"validteams.txt");

            try{
                fw1 = new FileWriter(file2);
                bw1 = new BufferedWriter(fw1);
                for(int index = 0; index < validTeams.size(); index++){
                    bw1.write(validTeams.get(index));
                    bw1.newLine();
                    //writes all the teams originally from validteams.txt to validteams.txt
                }

                for(int index = 0; index < scheduleTeams.size(); index++){
                    bw1.write(scheduleTeams.get(index));
                    bw1.newLine();
                    //writes all the new teams from frcschedule.txt to validteams.txt
                }

                bw1.close();
                if(validTeams.size() == 0)System.out.println("no file validteams.txt detected");
            } catch(IOException ioex){System.out.println("failure in schedule merging");}
        }
        if(evt.getSource() == snewteam){
            String newTeam = JOptionPane.showInputDialog(sframe,"Input new team","New Team",JOptionPane.PLAIN_MESSAGE);
            //opens a window and saves keyboard input as newTeam
            if(newTeam != null){
                File file2 = new File(path+"validteams.txt");

                try{
                    fw1 = new FileWriter(file2);
                    bw1 = new BufferedWriter(fw1);
                    for(int index = 0; index < validTeams.size(); index++){
                        bw1.write(validTeams.get(index));
                        bw1.newLine();
                    }

                    boolean noExcep = true;
                    try{
                        Integer.parseInt(newTeam);
                    } catch(NumberFormatException nfe){
                        noExcep = false;
                    }
                    boolean isNotAlreadyInTheListOfValidTeams = true;
                    for(int index = 0; index < validTeams.size(); index++){
                        if(newTeam != null && validTeams.get(index).compareTo(newTeam) == 0){
                            isNotAlreadyInTheListOfValidTeams = false;
                        }
                    }
                    
                    if(isNotAlreadyInTheListOfValidTeams && noExcep){
                        bw1.write(newTeam);
                    }
                    bw1.close();
                    //about the same as getteamsfromschedule from line 145 - line 168
                    if(validTeams.size() == 0)System.out.println("no file validteams.txt detected");
                } catch(IOException ioex){
                    System.out.println("failure in schedule merging");
                }
            } else {
                System.out.println("no team inputted");
            }
        }
        if(evt.getSource() == spath){
            File fil1 = new File("anchor.txt");
            ScoutErrorBox seb = new ScoutErrorBox("","<html>path: "+path+"<br>path (actual): "+fil1.getAbsolutePath()+"<br>sun.boot.class.path: "+System.getProperty("sun.boot.class.path")+"</html>",0);
        }
    }

    public void getTeams(){
        /*
         * this function gets all the teams for confirmation of stuff. It's
         * normally useless, but a few things use it and if anything else
         * needs it in the future, good to have. The concatenator
         * needs this, for example.
         */
        validTeams = new ArrayList<String>();
        File file1 = new File(path+"validteams.txt");
        String teamin = "h"; //placeholder

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            while(teamin != null){
                teamin = br1.readLine();
                if(teamin != null)validTeams.add(teamin);
                if(teamin != null)System.out.println(teamin);
            }
            br1.close();
            if(validTeams.size() == 0)System.out.println("no file validteams.txt detected");
        } catch(IOException ioex){
            System.out.println("failure in config");
        }
    }

    public void detPath(){
        /*
         * this function determines the location of the program in a roundabout
         * way, but it works and I'm too lazy to find a way to fix it. Getting
         * a path like this means some things where you'd normally need to
         * open a filechooser can be done sans human interaction
         */
        File fil1 = new File("anchor.txt"); //text doc in program file
        String temp = fil1.getAbsolutePath();
        String makesItWork = System.getProperty("sun.boot.class.path");
        int reduc = 10;
        if(System.getProperty("sun.boot.class.path").indexOf("BlueJ") > -1) reduc = 18;
        int direcStart = (temp.length() - reduc); //18 is length of "Scouter\anchor.txt"
                                               //and also "Scouter/anchor.txt"
        //System.out.println(temp);
        /*if(os.indexOf("Windows") >= 0)path = temp.substring(0,direcStart)+"Scouter Config and Data\\";
        if(os.indexOf("Linux") >= 0){
        path = temp.substring(0,direcStart)+"Scouter Config and Data\\";
        }*/
        path = temp.substring(0,direcStart)+"Scouter Config and Data"+File.separator;
        //System.out.println(path);
        getTeams();
        //does the getTeams function
    }
}
