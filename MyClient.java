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
	int core;
	int memory;
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
	   	System.out.println(this.din.readLine());
		this.send("AUTH "+System.getProperty("user.name"));
		System.out.println(this.din.readLine());
		String Lastmsg = "";
		String[] job;

		while(!Lastmsg.equals("NONE")){
			this.send("REDY"); 
			Lastmsg = this.din.readLine();
			System.out.println("Server Message: " + Lastmsg);
			job = Lastmsg.split("\\s+");
			if(job[0].equals("JOBN")){
			send("GETS Avail" + " " + job[4] + " " + job[5] + " " + job[6]); 
			serverMessage = this.din.readLine();
			String[] serverData = serverMessage.split("\\s+");
			if(serverData[1].equals("0")){
			send("GETS Capable" + " " + job[4] + " " + job[5] + " " + job[6]); 
			// send GETS message
			serverMessage = this.din.readLine();
			System.out.println("Server message " + serverMessage);
			String[] serverList = serverMessage.split("\\s+");
			int nRecs = Integer.parseInt(serverList[1]);
            send("OK");
			int countServer = 0;
			for (int i = 0; i < nRecs; i += 1) {
				serverMessage = this.din.readLine();
				String[] serverInfo = serverMessage.split("\\s+");
				if(countServer == 0){
					servers = serverInfo[0] + " " +serverInfo[1];
					countServer++;

					// serverList = serverMessage.split("\\s+");
					// serverType = Integer.parseInt(serverList[4]);
					// if (serverType > largestServerType) {
					// largestServerType = serverType;
					// largestServer = serverList[0];
					// largestServerCount = 1;
				}
				else {
					continue;	
				}
			}
			}
			else{

			}	

			// 	int serverType = 0;
			// 	for (int i = 0; i < nRecs; i += 1) {
			// 		serverMessage = this.din.readLine();
			// 		System.out.println("Server message " + serverMessage);
			// 		serverList = serverMessage.split("\\s+");
			// 		serverType = Integer.parseInt(serverList[4]);
			// 		if (serverType > largestServerType) {
			// 			largestServerType = serverType;
			// 			largestServer = serverList[0];
			// 			largestServerCount = 1;
			// 		}
			// 		else if(serverType == largestServerType){
			// 			if(serverList[0].equals(largestServer)){
			// 				largestServerCount ++;
			// 			}	
			// 		}
			// 	}
				// System.out.println(largestServer +" "+ Integer.toString(largestServerCount));
				// GetsAllCount++;
				// System.out.println(Integer.toString(GetsAllCount));
			send("OK");
			serverMessage = this.din.readLine();
			}
			if(job[0].equals("JOBN")){ //schedule job
				send("SCHD" + " " + job[2] +" " +servers);
				Lastmsg = this.din.readLine();
			}
			if(job[0].equals("JCPL")){
				continue;
			}	
		}
				
		
		send("QUIT");
		this.din.readLine();
	}
}

	


 





