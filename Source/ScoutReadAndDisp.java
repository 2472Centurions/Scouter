import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class ScoutReadAndDisp implements ActionListener
{
    ArrayList<String> varnames, unusedVarnames, values, unusedValues, teams,
    unusedTeams, metrics, validTeams;
    String chosenfile, raw, readout, os, path;
    int length, charnum;
    File file1;
    File[] filea1;
    FileWriter fw1;
    BufferedWriter bw1;
    FileReader fr1;
    BufferedReader br1;

    JFrame f1;
    JPanel omni, main, sub;
    JLabel lab;
    JButton b1,b2;
    JMenuBar smb;
    JMenu smenu, sdisplay, sdata, sfilelist;
    JMenuItem sopen, sopenfolder, swrite, send, sscatterplot, sbargraph, 
    sbargraph2, sbargraph3, sprinttoterminal, sprinttotxt, sclear, sload, 
    sconcat, ssave;
    JComboBox sfiles, sunused;

    SRADGrapherClass sgc;

    boolean end, noEdit;
    public ScoutReadAndDisp()
    {
        f1 = new JFrame("Read and Display");
        f1.setBounds(200,200,400,400);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setResizable(false);
        f1.setFont(new Font("Courier New", Font.PLAIN, 11));
        //see ScoutMain line 28
        Container c1 = f1.getContentPane();

        os = System.getProperty("os.name");
        //original plan for file paths used os specificity. Not used currently

        varnames = new ArrayList<String>();
        unusedVarnames = new ArrayList<String>();
        values = new ArrayList<String>();
        unusedValues = new ArrayList<String>();
        teams = new ArrayList<String>();
        unusedTeams = new ArrayList<String>();
        metrics = new ArrayList<String>();
        validTeams = new ArrayList<String>();

        detPath();
        getMetrics();

        b1 =  new JButton("Open");
        //b1.addActionListener(this);

        b2 =  new JButton("End");
        //b2.addActionListener(this);

        smb = new JMenuBar();
        smenu = new JMenu("File");
        sopen = new JMenuItem("Open");
        sopen.addActionListener(this);
        sopen.setToolTipText("Allows a .txt file to be imported");
        sopenfolder = new JMenuItem("Open Folder");
        sopenfolder.addActionListener(this);
        sopenfolder.setToolTipText("Allows a Scouter Config and Data folder to be imported");
        swrite = new JMenuItem("Write");
        swrite.addActionListener(this);
        swrite.setToolTipText("Opens the writing program");
        send = new JMenuItem("End");
        send.addActionListener(this);
        send.setToolTipText("Ends the program");
        sconcat = new JMenuItem("Concatenate");
        sconcat.addActionListener(this);
        sconcat.setToolTipText("Opens the concatenation program");
        smenu.add(sopen);
        smenu.add(sopenfolder);
        smenu.add(swrite);
        smenu.add(send);
        smenu.add(sconcat);
        smb.add(smenu);

        sdisplay = new JMenu("Display");
        sscatterplot = new JMenuItem("Scatter Plot");
        sscatterplot.addActionListener(this);
        sscatterplot.setToolTipText("Graphs relative team scores for comparison");
        sbargraph = new JMenuItem("Bar Graph");
        sbargraph.addActionListener(this);
        sbargraph.setToolTipText("Graphs total team scores for comparison");
        sbargraph2 = new JMenuItem("Bar Graph 2");
        sbargraph2.addActionListener(this);
        sbargraph2.setToolTipText("Graphs individual teams' scores in each metric");
        sbargraph3 = new JMenuItem("Bar Graph 3");
        sbargraph3.addActionListener(this);
        sbargraph3.setToolTipText("Graphs all teams' scores in each metric as they relate to each other");

        //sdisplay.add(sscatterplot);
        //be it known that Matt Burlowski loves scatter plots a bit too much
        sdisplay.add(sbargraph);
        sdisplay.add(sbargraph2);
        sdisplay.add(sbargraph3);
        smb.add(sdisplay);

        sdata = new JMenu("Data");
        sprinttoterminal = new JMenuItem("Print data to terminal");
        sprinttoterminal.addActionListener(this);
        sprinttoterminal.setToolTipText("Prints the data to a message box");
        sprinttotxt = new JMenuItem("Print data to .txt");
        sprinttotxt.addActionListener(this);
        sprinttotxt.setToolTipText("This one's just to make the menu aesthetically pleasing");
        sdata.add(sprinttoterminal);
        sdata.add(sprinttotxt);
        smb.add(sdata);

        sfilelist = new JMenu("File List");
        sclear = new JMenuItem("Clear");
        sclear.addActionListener(this);
        sclear.setToolTipText("Clears the used and unused file lists");
        sload = new JMenuItem("Load");
        sload.addActionListener(this);
        sload.setToolTipText("Loads used and unused file lists from saved configuration");
        ssave = new JMenuItem("Save");
        ssave.addActionListener(this);
        ssave.setToolTipText("Saves used and unused file list configurations");
        sfilelist.add(sclear);
        sfilelist.add(sload);
        sfilelist.add(ssave);
        smb.add(sfilelist);

        f1.setJMenuBar(smb);

        sfiles = new JComboBox();
        sfiles.addItem("Used Files");
        sfiles.addActionListener(this);
        sunused = new JComboBox();
        sunused.addItem("Unused Files");
        sunused.addActionListener(this);

        //System.out.println(""+sfiles.isVisible());

        sub = new JPanel(); 
        //sub.add(b1);
        //sub.add(b2);

        main = new JPanel();
        main.add(sfiles);
        main.add(sunused);

        omni = new JPanel();
        omni.setLayout(new BorderLayout());          
        omni.setSize(400,400);
        omni.add(main,BorderLayout.CENTER);
        omni.add(sub,BorderLayout.SOUTH);

        sfiles.setVisible(true);

        c1.add(omni);
        f1.show(); 

        getTeams();
    }

    private void startTheLoop(){
        //guess I never needed to start the loop
    }

    public void actionPerformed(ActionEvent evt){
        if(evt.getSource() == b1 || evt.getSource() == sopen){
            FileDialog fd = new FileDialog(f1, "Choose a file", FileDialog.LOAD);
            File fil1 = new File("anchor.txt");
            //
            //IMPLEMENT MULTIPLE FILE SELECTION!!!
            //stuff it, past aidan, I did it jeez
            //
            String temp = fil1.getAbsolutePath();
            String makesItWork = System.getProperty("sun.boot.class.path");
            int reduc = 10;
            if(System.getProperty("sun.boot.class.path").indexOf("BlueJ") > -1) reduc = 18;
            int direcStart = (temp.length() - reduc);

            //System.out.println(temp);
            //System.out.println(temp.substring(0,direcStart));
            /*if(os.indexOf("Windows") >= 0)fd.setDirectory(temp.substring(0,direcStart));
            if(os.indexOf("u") >= 0){
            fd.setDirectory(temp.substring(0,direcStart));
            }*/
            fd.setDirectory(temp.substring(0,direcStart));

            /*
             * set up so that multiple selections can be made!!!
             * did there been that
             */

            fd.setFile("*.txt");
            fd.setMultipleMode(true); //happy, past me?
            fd.setVisible(true);
            if(fd.getFile() != null){ //[0].getAbsolutePath();
                filea1 = fd.getFiles();
                for(int index = 0; index < filea1.length; index++){
                    readData(filea1[index]);
                    //gets the selected file's data and adds the file's name
                    //to the used files column
                }
            }
            //lab.setText("" + chosenfile + " has been chosen");
            //if(chosenfile != null)readData(chosenfile);
            //if(chosenfile != null)sfiles.addItem(chosenfile.substring(path.length(),chosenfile.length()));
            /*else {
            ScoutErrorBox seb = new ScoutErrorBox("No file was chosen");
            }*/

        }

        if(evt.getSource() == sopenfolder){
            //see concat
            //this is the last task to do before moving on to graphing
            JFileChooser jfc = new JFileChooser(path.substring(0,path.length()-24));
            //should start out looking at the flash drive but I'm not sure this line's right
            //doesn't really matter though
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //cuz it reads and processes at the folder level, not the file level
            jfc.setVisible(true);
            int returnVal = jfc.showOpenDialog(f1);
            File[] filea1= jfc.getSelectedFile().listFiles();
            //correction: it reads and processes at the folder level until here
            //gets all the files in the selected folder
            for(int index = 0; index < filea1.length; index++){
                for(int inndex = 0; inndex < validTeams.size(); inndex++){
                    if(jfc.getSelectedFile() != null && filea1[index].getName().indexOf("- "+validTeams.get(inndex)+".txt") > -1){
                        readData(filea1[index]);
                        sfiles.addItem(filea1[index].getName());
                        //this makes it such that when you open a folder,
                        //you don't get any files that don't contain a valid team
                        //one of the surprisingly few uses of validteams
                    }
                }
            }
        }

        if(evt.getSource() == sscatterplot){
            //still don't know why matt asked me to make one of these
            //to the reader: a scatter plot is for graphing against two infinitely divisible axes
            //a bar graph should be used for comparing an infinitely divisible axis to a countably divisible one
        }

        if(evt.getSource() == sbargraph){
            //main x is total weighted points
            sgc = new SRADGrapherClass(values, teams, validTeams, varnames, metrics, 0);
            //pulls up a graphical representation of data (a graph) without disposing the frame
        }

        if(evt.getSource() == sbargraph2){
            //sub x's are metrics
            if(teams.size() > 0)sgc = new SRADGrapherClass(values, teams, validTeams, varnames, metrics, 1);
            else{ 
                ScoutErrorBox seb = new ScoutErrorBox("No classes selected","",0);
            }
            //another graph, but not if there's no teams
        }

        if(evt.getSource() == sbargraph3){
            //sub x's are teams
            if(teams.size() > 0)sgc = new SRADGrapherClass(values, teams, validTeams, varnames, metrics, 2);
            else{ 
                ScoutErrorBox seb = new ScoutErrorBox("No classes selected","",0);
            }
            //graph again
        }

        if(evt.getSource() == swrite){
            ScoutRecordAndWrite unimportantVarName1 = new ScoutRecordAndWrite();
            f1.dispose();
            //just opens the writing program and disposes of the frame
        }

        if(evt.getSource() == sconcat){
            SRADConcat unimportantVarName1 = new SRADConcat();
            //opens the concatenator without disposing the frame
        }

        if(evt.getSource() == sprinttoterminal){
            String terminalOut = "<html>";
            String currentTeam = "default";
            for(int thru = 0; thru < varnames.size(); thru++){
                if(currentTeam != "default") terminalOut = terminalOut + "<br>";
                //System.out.println(currentTeam+"<");
                //System.out.println(teams.get(thru)+"<");
                String comp = teams.get(thru);
                if(currentTeam.indexOf(comp) >= 0){
                    terminalOut = terminalOut + "&#8195" + varnames.get(thru) + ": " + values.get(thru);
                }
                else{
                    currentTeam = teams.get(thru);
                    terminalOut = terminalOut + currentTeam + ":";
                    thru--;
                }
            }
            terminalOut = terminalOut + "</html>";
            //complicated, but end result is some nice spacing using html
            ScoutErrorBox seb = new ScoutErrorBox(terminalOut,"",1);
            //makes a message box with all the data (not great for any large amt of data)
            //System.out.println(terminalOut);
        }

        if(evt.getSource() == sprinttotxt){
            //yeah this one's fake too
        }

        if(evt.getSource() == sclear){
            int clearret = JOptionPane.showConfirmDialog(f1,"Are you sure you want to clear the Used and Unused files?","Warning",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(clearret == 0){
                noEdit = true;
                sfiles.removeAllItems();
                sunused.removeAllItems();
                //removes all the items from the dropdowns
                teams.clear();
                unusedTeams.clear();
                //removes all the items from the concurrent team arrays
                values.clear();
                unusedValues.clear();
                //removes all the items from the concurrent value arrays

                sfiles.addItem("Used Files");
                sunused.addItem("Unused Files");
                //sticks the default value in the top position
                //if you somehow screw up the dropdown menus, this will fix it
                if(sfiles.getItemAt(0) != "Used Files")sfiles.removeItemAt(0);
                if(sunused.getItemAt(0) != "Unused Files")sunused.removeItemAt(0);
                noEdit = false;
            }
        }

        if(evt.getSource() == sload){
            //these were gonna save the teams in the dropdowns but who cares :3
        }

        if(evt.getSource() == ssave){
            //second one of above comment
        }

        if(evt.getSource() == b2 || evt.getSource() == send){
            f1.dispose();
            //disposes frame
        }

        if(evt.getSource() == sfiles && sfiles.getSelectedItem() != "Used Files" && !(noEdit)){
            int editIndex = sfiles.getSelectedIndex()-1;
            System.out.println(""+sfiles.getSelectedIndex());
            sunused.addItem(sfiles.getSelectedItem());
            for(int index = 0; index < metrics.size(); index++){
                unusedTeams.add(teams.get(editIndex*metrics.size() + index));
            }
            for(int index = 0; index < metrics.size(); index++){
                unusedValues.add(values.get(editIndex*metrics.size() + index));
            }
            for(int index = 0; index < metrics.size(); index++){
                unusedVarnames.add(varnames.get(editIndex*metrics.size() + index));
            }
            //adds the clicked name, team, and data to the "unused" parallel variables

            for(int index = 0; index < metrics.size(); index++){
                teams.remove(editIndex*metrics.size());
                //because the arraylist size decreases by 1 every time
            }
            for(int index = 0; index < metrics.size(); index++){
                values.remove(editIndex*metrics.size());
                //like the previous element was just removed
            }
            for(int index = 0; index < metrics.size(); index++){
                varnames.remove(editIndex*metrics.size());
                //so it's on the next one without incrementing
            }
            sfiles.removeItem(sfiles.getSelectedItem());
            //removes the clicked name, team, and data from the "used" parallel variables
        }

        if(evt.getSource() == sunused && sunused.getSelectedItem() != "Unused Files" && !(noEdit)){
            int editIndex = sunused.getSelectedIndex()-1;
            sfiles.addItem(sunused.getSelectedItem());
            for(int index = 0; index < metrics.size(); index++){
                teams.add(unusedTeams.get(editIndex*metrics.size() + index));
            }
            for(int index = 0; index < metrics.size(); index++){
                values.add(unusedValues.get(editIndex*metrics.size() + index));
            }
            for(int index = 0; index < metrics.size(); index++){
                varnames.add(unusedVarnames.get(editIndex*metrics.size() + index));
            }
            //adds the clicked name, team, and data to the "used" parallel variables

            for(int index = 0; index < metrics.size(); index++){
                unusedTeams.remove(editIndex*metrics.size());
            }
            for(int index = 0; index < metrics.size(); index++){
                unusedValues.remove(editIndex*metrics.size());
            }
            for(int index = 0; index < metrics.size(); index++){
                unusedVarnames.remove(editIndex*metrics.size());
            }
            sunused.removeItem(sunused.getSelectedItem());
            //removes the clicked name, team, and data from the "unused" parallel variables
        }
    }

    public void danesSuggestion(String cf){
        /*the thing where it reads the name variables from a .txt instead
        of the vars array*/
        //whoops it does this in a different waaaay
    }

    public void readData(File cf){
        // use charAt(int) to read specific characters
        // instead of returning, just call var update method in SRAD Grapher
        // format: {AA}{BB}{data}{AA}{BB}{data}...{data}00
        // AA is the length in 2 hex chars
        // BB is the variable type (there will be a set number of vars after
        // we recieve rules, build for n variables to be implemented!)
        // data is the data that is AA characters long
        // 00 terminates the reader (since it's length 0)
        System.out.println(cf);
        file1 = cf;

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            raw = br1.readLine();

            if(raw == null){
                readout = ("String not present, choose other file");
                ScoutErrorBox seb = new ScoutErrorBox(readout,"",0);
            } else {
                //decrypt file here
                int linesread = 0;
                String copies = "";
                while(raw != null){
                    charnum = 0;
                    end = false;
                    while(end == false){
                        length = Integer.parseInt(raw.substring(charnum,charnum+2),16);
                        //reads the first half of the 2 info bytes, length, and converts from hex
                        if(length == 0){
                            end = true;
                            System.out.println("end");
                        }
                        else{
                            int varType = Integer.parseInt(""+raw.substring(charnum+2,charnum+4),16);
                            // reads second half of 2 info bytes, variable type, and converts from hex

                            //System.out.println("length "+length);
                            //System.out.println("length substring "+raw.substring(charnum,charnum+2));
                            //System.out.println("raw "+raw);
                            //System.out.println("var name "+vars[varType-1]);

                            varnames.add(metrics.get(varType-1));
                            teams.add(file1.getName().substring(0,file1.getName().length()-4)+copies);
                            if(teams.size()%metrics.size() == 0){
                                sfiles.addItem(teams.get(teams.size()-1));
                            }
                            //writes the filename minus ".txt" to the list of teams
                            //and to the dropdown menu

                            /*char[] tempdata = new char[length];
                            raw.getChars(charnum+4, charnum+4+length, tempdata, 0);
                            String temp = new String(tempdata);
                            for(int lingth = 0; lingth <= length; lingth++){
                            temp = temp + 
                            }*/

                            String temp = raw.substring(charnum+4, charnum+4+length);
                            //reads data from the first number after the info bytes for
                            //to the number whose distance from that is equal to the
                            //value of the variable 'length'

                            //System.out.println("value "+temp);

                            values.add(temp);
                            charnum = charnum+4+length;
                            //sets the new point to start reading from to the end of the data
                        }
                    }
                    raw = br1.readLine();
                    linesread++;
                    copies = " (" + linesread + ")";
                }
            }
            br1.close();
        } catch(IOException ioex){
            ScoutErrorBox seb = new ScoutErrorBox(ioex.getMessage(),"",0);
            //System.out.println(ioex.getMessage());
        }
    }

    public void getMetrics(){
        //here it is
        //has to be called after path is defined in detpath
        file1 = new File(path+"metrics.txt");
        String metricin = "h"; //placeholder
        metrics = new ArrayList<String>();
        int loopsRun = 0;

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            //br1.mark(12000);

            while(metricin != null){
                metricin = br1.readLine();
                boolean noSpaceYet = true;
                int spaceOfs = 0;
                int spacePlace = 0;
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
                }
            }
            br1.close();
        } catch(IOException ioex){System.out.println(ioex.getMessage());}
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

    public void getTeams(){
        //see ScoutMain 183-187
        File file1 = new File(path+"validteams.txt");
        String teamin = "h"; //placeholder
        validTeams.clear();

        try{
            fr1 = new FileReader(file1);
            br1 = new BufferedReader(fr1);
            while(teamin != null){
                teamin = br1.readLine();
                if(teamin != null)validTeams.add(teamin);
                //if(teamin != null)System.out.println(teamin);
            }
            br1.close();
            if(validTeams.size() == 0)System.out.println("no file validteams.txt detected");
        } catch(IOException ioex){System.out.println("failure in config");}

    }
}
