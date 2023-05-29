import java.io.*;
import java.net.*;

public class MyClient{
	Socket s;
	DataOutputStream dout;
    BufferedReader din;
	String msg = "";
	String servers;
	int serverID = 0;
    int jobID = 0;
    int nRecs = 0;
    String serverMessage = "";
	int largestServerType = 0;
    String largestServer = "";
	int largestServerCount = 0;
	int GetsAllCount = 0;
    int firstcapableserver;
	int firstcapableID;
	

	


	public MyClient(String address, int port) throws Exception{
		s = new Socket(address, port);
		dout = new DataOutputStream(s.getOutputStream());
	    din = new BufferedReader(new InputStreamReader(s.getInputStream()));  
  	}


	public void send(String message ) throws Exception{
		this.dout.write((message + "\n").getBytes("UTF-8"));
		this.dout.flush();
	}

	
	public static void main(String[] args) throws Exception{
        MyClient c = new MyClient("127.0.0.1",50000);
		c.byClient();
		c.din.close();
		c.s.close();
	}
       	

	public void byClient() throws Exception{
		this.send("HELO");
	   	this.din.readLine();
		this.send("AUTH "+System.getProperty("user.name"));
		this.din.readLine();
		String Lastmsg = "";
		String[] job;

		while(!Lastmsg.equals("NONE")){
			this.send("REDY"); 
			Lastmsg = this.din.readLine();
			
			job = Lastmsg.split("\\s+");
			if(job[0].equals("JOBN")){
				send("GETS Avail" + " " + job[4] + " " + job[5] + " " + job[6]); // get memory core speed from jobn
				serverMessage = this.din.readLine();
				String[] serverData = serverMessage.split("\\s+");
				int nRecs; 
				if(serverData[1].equals("0")){ // data nrecs length.. no. rec = 0 so no server avail
					send("OK");
					serverMessage = this.din.readLine();
					send("GETS Capable" + " " + job[4] + " " + job[5] + " " + job[6]); // get the capable servers inorder to queue the job
					// send GETS message
					serverMessage = this.din.readLine();
					serverData = serverMessage.split("\\s+");
					
					nRecs = Integer.parseInt(serverData[1]); // updating value of nrecs 
					send("OK");
					int countServer = 0;
					for (int i = 0; i < nRecs; i += 1) { // loop nrec number of time to read everyline of the servers
						serverMessage = this.din.readLine();
						String[] serverInfo = serverMessage.split("\\s+");
						if(countServer == 0){   // ensures we are only keeping the first server int he list
							servers = serverInfo[0] + " " +serverInfo[1];
							countServer++;
						}
						else {
							continue;	
						}

					}
				}
				else{
					nRecs = Integer.parseInt(serverData[1]); // if there is available servers
					send("OK");
					int countServer = 0;
					for (int i = 0; i < nRecs; i += 1) {
						serverMessage = this.din.readLine();
						String[] serverInfo = serverMessage.split("\\s+");
						if(countServer == 0){
							servers = serverInfo[0] + " " +serverInfo[1];
							countServer++;
						}
						else {
							continue;	
						}

					}
				}
				send("OK");
				serverMessage = this.din.readLine(); // to rcv .
				
					//schedule job
				send("SCHD" + " " + job[2] +" " +servers);
				Lastmsg = this.din.readLine();
			}
			if(job[0].equals("JCPL")){
				continue;
			}
		}
		send("QUIT"); //termination line 
		this.din.readLine();
	}
}



	


 





