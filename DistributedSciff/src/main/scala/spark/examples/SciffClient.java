package spark.examples;

/**
 * Created by utente on 21/02/17.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class SciffClient {
    String serverName;
    String []hs;
    int port;
    Boolean debug = false;

    public SciffClient(String serverName, int port, String[] hs, Boolean debug){
        this.serverName=serverName;
        this.port=port;
        this.hs=hs;
        this.debug=debug;
    }

    public String startClient(){
        String read =null;
        try {
            if (debug) System.out.println("Client going to connect to "+serverName+":"+port);
            Socket clientSocket = new Socket(serverName, port);
            if (debug) System.out.println("Client connected!");
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter outToServer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));


            //String hsArray[] = hs.split("\\.");
            for (String h : hs) {
                outToServer.write(h + "\n");
                outToServer.flush();
                //System.out.println("Client has sent : "+h);
                //read = inFromServer.readLine();  //soppressa la lettura di Letto: h...
                //System.out.println("Client has read : "+read);
            }
            outToServer.write("temp_end\n");
            outToServer.flush();
            read = inFromServer.readLine();  //non può essere soppressa: è il result parziale o totale
            //System.out.println("Result is "+read);
            if (!read.equals("Yes") && !read.equals("No") && !read.equals("Ni"))
                throw new IOException("Client has received an unexpected result: "+read);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            read = "No";
        }
        return read;
    }
}
