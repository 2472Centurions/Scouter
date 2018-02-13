import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class SRADConcat implements ActionListener
{
    JFrame f1;
    JPanel omni, main, sub;
    JLabel files;
    JButton concat1;
    JMenuBar smb;
    JMenu sfile;
    JMenuItem sopen, sopen2, sconcat;
    JComboBox filesIn;
    String chosenfile, chosenfile2, path, pathOut, cf, cf2;
    File[] fil1;
    ArrayList<String> pathsIn, validTeams, dataHold;
    int direcStart;
    String[][] valid, valid2;
    String[] validNames;

    FileWriter fw2;
    BufferedWriter bw2;
    FileReader fr1, fr2;
    BufferedReader br1, br2;

    public SRADConcat()
    {
        detPath();
        pathsIn = new ArrayList<String>();
        validTeams = new ArrayList<String>();
        getTeams();

        f1 = new JFrame("Concatenator");
        f1.setBounds(300,250,400,300);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.setFont(new Font("Courier New", Font.PLAIN, 11));
        f1.setResizable(false);

        files =  new JLabel("Folder 1 → Folder 2");// → is alt code 26
        cf = "Folder 1";
        cf2 = "Folder 2";
        
        concat1 = new JButton("Concatenate");
        concat1.addActionListener(this);

        smb = new JMenuBar();
        sfile = new JMenu("File");
        sopen = new JMenuItem("Open Source File");
        sopen.addActionListener(this);
        sopen2 = new JMenuItem("Open Destination File");
        sopen2.addActionListener(this);
        sconcat = new JMenuItem("Concatenate");
        sconcat.addActionListener(this);
        sfile.add(sopen);
        sfile.add(sopen2);
        sfile.add(sconcat);
        smb.add(sfile);

        f1.setJMenuBar(smb);

        filesIn = new JComboBox();
        filesIn.addItem("Files to Concatenate");
        filesIn.addActionListener(this);

        sub = new JPanel();
        sub.add(concat1);

        main = new JPanel();
        //main.add(filesIn);
        main.add(files);

        omni = new JPanel();
        omni.setLayout(new BorderLayout());          
        omni.setSize(400,400);
        omni.add(main,BorderLayout.CENTER);
        omni.add(sub,BorderLayout.SOUTH);

        f1.add(omni);

        f1.show();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == sopen){
            /*FileDialog fd = new FileDialog(f1, "Choose a file", FileDialog.LOAD);
            fd.setDirectory(path);
            fd.setFile("*.txt");
            fd.setMultipleMode(true);
            fd.setVisible(true);
            if(fd.getFile() != null){ //[0].getAbsolutePath();
            fil1 = fd.getFiles();
            for(int index = 0; index < fil1.length; index++){
            pathsIn.add(fil1[index].getAbsolutePath());
            filesIn.addItem(fil1[index].getName());
            }
            //chosenfile = fd.getFile();
            //String t = files.getText();
            //files.setText(chosenfile+t.substring(t.length()-(chosenfile2.length() + 3),t.length()));
            }*/
            JFileChooser jfc = new JFileChooser(path.substring(0,path.length()-24));
            //odds on, the above code won't sync up in a JRE, but it should
            //just default to another directory
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //jfc.setVisible(true);
            int returnVal = jfc.showOpenDialog(f1);
            if(jfc.getSelectedFile() != null){
                chosenfile = jfc.getSelectedFile().getAbsolutePath();
                cf = jfc.getSelectedFile().getName();
            }
            String t = files.getText();
            files.setText(cf+t.substring(t.length()-(cf2.length()+3),t.length()));
            //this line puts the selected file on the GUI
        }

        if(e.getSource() == sopen2){
            /*FileDialog fd = new FileDialog(f1, "Choose a file", FileDialog.LOAD);
            fd.setDirectory(path);
            fd.setFile("*.txt");
            fd.setMultipleMode(false);
            fd.setVisible(true);
            if(fd.getFile() != null){
            cf2 = fd.getFiles()[0].getAbsolutePath();
            chosenfile2 = fd.getFile();
            String t = files.getText();
            files.setText(t.substring(0,3)+chosenfile2);
            }*/
            JFileChooser jfc = new JFileChooser(path.substring(0,path.length()-24));
            jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //jfc.setVisible(true);
            int returnVal = jfc.showOpenDialog(f1);
            if(jfc.getSelectedFile() != null){
                chosenfile2 = jfc.getSelectedFile().getAbsolutePath();
                cf2 = jfc.getSelectedFile().getName();
            }
            String t = files.getText();
            files.setText(t.substring(0,cf.length()+3)+cf2);
            //
            //same notes as e.getSource() == sopen
            //
        }

        if(e.getSource() == sconcat || e.getSource() == concat1){
            if(chosenfile != "Folder 1" && chosenfile2 != "Folder 2"){
                concatenateFiles();
            }
            else{
                ScoutErrorBox seb = new ScoutErrorBox("Choose a source and destination","",0);
            }
        }
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

    public void concatenateFiles(){
        /*
         * this method attempts to find two files with valid team names,
         * then copy data that only the input file has to the
         * output file (actually uses arrays of files, but w/e)
         */
        dataHold = new ArrayList<String>();

        File direc = new File(chosenfile);
        File[] datas = direc.listFiles();
        File direc2 = new File(chosenfile2);
        File[] datas2 = direc2.listFiles();
        for(int index = 0; index < datas.length; index++){
            System.out.println(datas[index].getName());
        }

        valid = detValidIn(datas);
        //see comment for detValidIn()
        valid2 = detValidOut(datas2,valid);
        //see comment for detValidOut()

        ArrayList<String> failures = new ArrayList<String>();

        for(int index = 0; index < valid.length; index++){
            for(int inndex = 0; inndex < validTeams.size();inndex++){
                //for(int innndex = 0; innndex < datas2.length;innndex++){
                if(valid2[index][0] != null && valid[index][0].indexOf("- "+validTeams.get(inndex)+".txt") > -1){
                    /*
                     * these if statements are like this so teams with names like
                     * 2134 or 1213 won't write to team 213's data file
                     */
                    System.out.println("Detected Team: " + validTeams.get(inndex));
                    File file1 = new File(valid[index][0]);
                    //System.out.println("file 1: " + file1.getName());
                    File file2 = new File(valid2[index][0]);
                    //System.out.println("file 2: " + file2.getName());

                    try{
                        fr1 = new FileReader(file1);
                        br1 = new BufferedReader(fr1);
                        fr2 = new FileReader(file2);
                        br2 = new BufferedReader(fr2);

                        /*
                         * this is the part where the method actually
                         * determines what is in the input file, but
                         * not in the output file, then rewrites the
                         * output file with that data appended to it 
                         */
                        String dataIn = "";
                        dataHold.clear();
                        System.out.println("dataHold length " + dataHold.size());
                        while(dataIn != null){
                            dataIn = br2.readLine();
                            if(dataIn != null)dataHold.add(dataIn);
                        }

                        dataIn = "";
                        while(dataIn != null){
                            dataIn = br1.readLine();
                            if(dataIn != null && 
                            !(dataHold.contains(dataIn))){
                                dataHold.add(dataIn);
                            }
                        }
                        //in retrospect, coulda just rewrote everything in the file,
                        //then written the new stuff, like I did elsewhere
                        
                        fw2 = new FileWriter(file2);
                        bw2 = new BufferedWriter(fw2);

                        for(int innerdex = 0; innerdex < dataHold.size(); innerdex++){
                            bw2.write(dataHold.get(innerdex));
                            System.out.println(dataHold.get(innerdex));
                            bw2.newLine();
                        }

                        br1.close();
                        br2.close();
                        bw2.close();
                    } catch(IOException ioex){
                        System.out.println(ioex.getMessage());
                    }
                }

                if(valid2[index][0] == null || valid[index][0].indexOf(validTeams.get(inndex)) > -1){
                    failures.add(validNames[index]);
                }
                
            }
        }

        if(failures.size() > 0){
            String terminalOut = "<html>Files that failed to concatenate";
            for(int thru = 0; thru < failures.size(); thru++){
                terminalOut = terminalOut + "<br>";
                terminalOut = terminalOut + "&#8195" + " " + failures.get(thru);
                // the " " here prevents "&#8195" from reading a filename
                // that starts with a number as a different html code 
                // which starts with the one for em space
            }
            terminalOut = terminalOut + "</html>";

            ScoutErrorBox sebFails = new ScoutErrorBox(terminalOut, "", 0);
        }
    }

    public void getTeams(){
        //this method is common between a few classes too
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

    public String[][] detValidIn(File[] datasIn){
        //set up so that the name of each valid file in datasIn gets put into ValidatedFiles
        String[][] validatedFiles = new String[datasIn.length][2];
        validNames = new String[datasIn.length];
        
        for(int index = 0; index < datasIn.length; index++){
            for(int innerdex = 0; innerdex < validTeams.size(); innerdex++){
                if(datasIn[index].getName().indexOf("- "+validTeams.get(innerdex)+".txt") > -1){
                    //System.out.println("name in: "+datasIn[index].getName());
                    //System.out.println("number in: " + validTeams.get(innerdex));
                    validatedFiles[index][0] = datasIn[index].getAbsolutePath();
                    validatedFiles[index][1] = validTeams.get(innerdex);
                    validNames[index] = datasIn[index].getName();
                } 
            }
        }

        return validatedFiles;
    }

    public String[][] detValidOut(File[] datasIn, String[][] strIn){
        /*
         * set up so that for each valid doc in strIn, there's a valid
         * doc at the same index in validatedFiles, making a
         * parallel array to the one in detValidIn
         */
        String[][] validatedFiles = new String[strIn.length][2];
        //ArrayList<String> onePerTeam = new ArrayList<String>();
        //I wonder what this was meant to do?

        for(int index = 0; index < strIn.length; index++){
            validatedFiles[index][0] = null;
            for(int innerdex = 0; innerdex < datasIn.length; innerdex++){
                if(datasIn[innerdex].getAbsolutePath().indexOf("- "+strIn[index][1]+".txt") > -1 && validatedFiles[index][0] == null){
                    //System.out.println("name out: "+datasIn[innerdex].getName());
                    //System.out.println("number out: " + strIn[index][1]);
                    validatedFiles[index][0] = datasIn[innerdex].getAbsolutePath();
                    validatedFiles[index][1] = strIn[index][1];
                }
            }
        }

        return validatedFiles;
    }
}
