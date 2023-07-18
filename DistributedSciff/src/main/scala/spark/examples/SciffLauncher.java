package spark.examples;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by utente on 24/02/17.
 */
public class SciffLauncher {
    //private static final String path_to_AlpBPM = "/home/daniela/AlpBPM"; //SUL c4-master-0: "/home/ubuntu/AlpBPM" //SU lab3n86 "/home/daniela/AlpBPM"; //SUL MAC: "/Users/utente/Desktop/AlpBPM";
    //private static final String path_to_swipl = "/usr/bin"; //SUL MAC: "/usr/local/bin";

    private ServerSocket welcomeSocket;
    private int port;


    private String [] cmd;
    private Scanner in;
    private Scanner err;
    private PrintWriter out;// = new PrintWriter(proc.getOutputStream());
    //int result = -1;
    private String result = "Ni";
    private Process proc;

    private ArrayList<String> [] eventsToMeet;
    private Boolean debug;
    private String lastEvent;

    public SciffLauncher(String model, String observability,
                         String durationConstr,String interTConstr,String options, Boolean debug, String path_to_AlpBPM,String path_to_swipl) {
        cmd = new String[]{path_to_swipl, "-g", "working_directory(_,'"+path_to_AlpBPM+"'),spark_trace_verification("
                + model+ ","+observability+","+durationConstr+","+interTConstr+","+options+")",
                "-t", "halt", path_to_AlpBPM+"/AlpBPM.pl"};
        this.debug=debug;
    }

    public SciffLauncher(String icList, String options, Boolean debug, String path_to_AlpBPM,String path_to_swipl) {
        cmd = new String[]{path_to_swipl, "-g", "working_directory(_,'"+path_to_AlpBPM+"'),spark_trace_verification_standard_sciff('"
                + icList+ "',"+options+")",
                "-t", "halt", path_to_AlpBPM+"/AlpBPM.pl"};
        this.debug=debug;
    }

    private void runStandardErrorThread(){
        if (err==null)
            err = new Scanner(proc.getErrorStream()) ;
        new Thread(){public void run(){
            System.err.println("Debugging thread ERROR started");
            while (err.hasNextLine())
                System.err.println("ERR:"+err.nextLine());
        }}.start();
    }
    private void runStandardOutputThread() {
        new Thread(){public void run(){
            System.out.println("Debugging thread OUTPUT started");
            while (in.hasNextLine())
                System.out.println("OUT:"+in.nextLine());
        }}.start();
    }

    public String launchCompleteModel(String [] values, String lastEvent){
        this.lastEvent= lastEvent;
        try {
            if (values.length==0) throw new IOException("The event list should never be empty at this stage!");
            ProcessBuilder pb = new ProcessBuilder(cmd);
            proc = pb.start();
            // Start reading from the program
            this.setOut(new PrintWriter(proc.getOutputStream())) ;  //write to sciff stdin
            this.setIn(new Scanner(proc.getInputStream()));         //read from sciff stdout
            if (debug) {
                String t="";
                for (String c: cmd)
                    t=t+" "+c;
                System.out.println("Debug is true. swipl started with command: "+t);
                runStandardErrorThread();  //this should consume the WARN and all the other unrelevant messages
            }
            if (getIn().hasNextLine()) {
                getIn().nextLine(); //consume first WARN (WARNING: File defaults.pl not found. Assuming default dir is ..)
            }
            String r = null;
            Boolean foundLast =false;
            for (String event : values) {
                //if (debug) System.out.println("SciffLauncher: sending "+event+" to swipl");
                out.println(event+".");
                out.flush();

                if (event.equals(lastEvent))
                    foundLast=true;
                if (debug) System.out.print("SciffLauncher: sent "+event+" to swipl\t");

                if (in.hasNextLine() ){
                    r = in.nextLine();
                    if (debug) System.out.println(r);
                    if (r.equals("No")||r.equals("Yes")) {
                        setResult(r);
                        return r;
                    }
                }
            }
            if (r==null)   throw new IOException("The return value should never be null!");
            setResult(r);
            if (debug) System.out.println("SciffLauncher returning r="+r);

            return r;
        } catch (IOException e) {
            e.printStackTrace();
            setResult("No");
            return "No";
        }
    }

    public String launchPartitionedModel(String [] values, int sm, Boolean subtraceCompleteCheck, ArrayList<String> [] eventsToMeet, String lastEvent){
        this.lastEvent= lastEvent;
        try {
            if (values.length==0) throw new IOException("The event list should never be empty at this stage!");
            ProcessBuilder pb = new ProcessBuilder(cmd);
            proc = pb.start();
            // Start reading from the program
            this.setOut(new PrintWriter(proc.getOutputStream())) ;  //write to sciff stdin
            this.setIn(new Scanner(proc.getInputStream()));         //read from sciff stdout
            if (debug) {
                String t="";
                for (String c: cmd)
                    t=t+" "+c;
                System.out.println("Debug is true. swipl started with command: "+t);
                runStandardErrorThread();  //this should consume the WARN and all the other unrelevant messages
            }
            if (getIn().hasNextLine()) {
                getIn().nextLine(); //consume first WARN (WARNING: File defaults.pl not found. Assuming default dir is ..)
            }
            String r = null;

            if (!subtraceCompleteCheck && eventsToMeet.length == 0 ) {
                //events to meet are: sm++:start/end & sm+=2:start/end
                int f=sm+1; int s=sm+2;
                this.eventsToMeet = new ArrayList[1] ;
                //SubmodelVariables sv = new SubmodelVariables();
                //this.eventsToMeet = sv.subtraceCompleteIf()[0];
                this.eventsToMeet[0] = new ArrayList<String>();
                this.eventsToMeet[0].add(f+"_start");
                this.eventsToMeet[0].add(f+"_end");
                this.eventsToMeet[0].add(s+"_start");
                this.eventsToMeet[0].add(s+"_end");
            } else if (subtraceCompleteCheck)
                this.eventsToMeet = eventsToMeet;
            Boolean foundLast =false;
            for (String event : values) {
                //if (debug) System.out.println("SciffLauncher: sending "+event+" to swipl");
                out.println(event+".");
                out.flush();

                if (event.equals(lastEvent))
                    foundLast=true;
                metEvent(event);
                if (debug) System.out.print("SciffLauncher: sent "+event+" to swipl\t");

                if (in.hasNextLine() ){
                    r = in.nextLine();
                    if (debug) System.out.println(r);
                    if (r.equals("No")||r.equals("Yes")) {
                        setResult(r);
                        return r;
                    }
                }
            }
            //here r should be Ni
            //if the lastEvent was naturally found in the stream it should have been already passed to sciff process
            //and the sciff should have already gave us No or Yes
            if (this.allEventsFound() && ! foundLast) {
                out.println(lastEvent+"."); //end_of_the_world is generated here for each submodel
                out.flush();
                r = in.nextLine();
                if (debug) System.out.println("Answer after "+lastEvent+" : "+r);
            }
            if (r==null)   throw new IOException("The return value should never be null!");
            setResult(r);
            if (debug) System.out.println("SciffLauncher returning r="+r);

            return r;
        } catch (IOException e) {
            e.printStackTrace();
            setResult("No");
            return "No";
        }
    }

    private void metEvent(String event){
        for (ArrayList<String> etm : eventsToMeet) {
            etm.remove(event);
        }
    }

    private boolean allEventsFound(){
        for (ArrayList<String> etm : eventsToMeet) {
            if (etm.size()==0) return true;
        }
        return false;
    }

    private ServerSocket create(int[] ports) throws IOException {
        for (int port : ports) {
            try {
                this.port = port;
                return new ServerSocket(port);
            } catch (IOException ex) {
                //continue; // try next port
            }
        }
        throw new IOException("No free port found");
    }

    public void launchTheServer(char V_H) {
        try{
            welcomeSocket = create(new int[]{8000,8001,8002,8003,8004,8005,8006,8007,8008,8009,8010,8011,8012,8013,8014,8015,8016,8017,8018,8019,8020,
                    8021,8022,8023,8024,8025,8026,8027,8028,8029,8030,8031,8032,8033,8034,8035,8036,8037,8038,8039,8040});
            welcomeSocket.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace(); return;
        }
        final char VorH=V_H;

        new Thread(){
            public void run(){
                try{
                    while(getResult().equals("Ni")) {
                        if (debug) System.out.println("Server going to accept connections");

                        Socket connectionSocket = welcomeSocket.accept();
                        if (debug) System.out.println("Server accepted a connection");

                        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                        BufferedWriter outToClient = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));

                        String lineRead = inFromClient.readLine();
                        Boolean foundLast = false;
                        while (!lineRead.equals("temp_end")) {
                            if (getResult().equals("Ni")) {    //getResult()==-1) {
                                //se la sciff è già terminata con "Yes" o "No" mangio solo i seguenti eventi dal client
                                // fino al temp_end, non invio su out verso sciff process perchè suppongo sia terminato
                                out.println(lineRead + ".");  //forward lineRead to SCIFF
                                out.flush();
                                if (VorH=='H') {
                                    if (lineRead.equals(lastEvent))
                                        foundLast = true;
                                    metEvent(lineRead);
                                }
                                if (debug) System.out.println("SciffLauncher: sent " + lineRead + " to swipl");

                                if (in.hasNextLine()) {
                                    String r = in.nextLine();
                                    if (debug) System.out.println(r);
                                    if (r.equals("No") || r.equals("Yes"))
                                        setResult(r);
                                }
                            }
                            lineRead = inFromClient.readLine();
                        }

                        if (VorH=='H' && allEventsFound() && getResult().equals("Ni") && !foundLast) {
                            //check su foudLast è ridondande, ma così evito di mandare due volte lastEvent
                            out.println(lastEvent + ".");
                            out.flush();
                            String r = in.nextLine();
                            if (debug) System.out.println("Answer after lastEvent: " + r);
                            setResult(r);
                        }
                        outToClient.write(getResult() + "\n");
                        outToClient.flush();
                        connectionSocket.close();
                    }
                    welcomeSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        setResult("Ni");
    }

    public synchronized String getResult(){
        return this.result;
    }
    public synchronized void setResult(String result){
        this.result = result;
    }


    public synchronized PrintWriter getOut(){
        //System.out.println("SciffLauncher: Getting the OUT printwriter to swipl");
        try {
            while (this.out == null)
                this.wait();
        } catch (InterruptedException e) {e.printStackTrace();}
        return this.out;
    }
    private synchronized void setOut(PrintWriter out){
        this.out = out;
        this.notify();
    }

    public synchronized Scanner getIn(){
        try {
            while (this.in == null)
                this.wait();
        } catch (InterruptedException e) {e.printStackTrace();}
        return this.in;
    }
    private synchronized void setIn(Scanner in){
        this.in = in;
        this.notify();
    }

    public int getPort(){
        return this.port;
    }


}
