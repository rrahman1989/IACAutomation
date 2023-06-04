package com.automation.utility.ssh;

import java.io.File;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHConnection {
	
	 public Session session;
	 
	 public void connect() {
	        try {
	 
	            JSch jsch = new JSch();
	            String user = "codekru"; // your username
	            String host = "127.0.0.1"; // your secure server address
	            int port = 2222; // your secure server port
	            String key = System.getProperty("user.dir") + File.separator + "key.dms";
	 
	            jsch.addIdentity(key);
	            session = jsch.getSession(user, host, port);
	            session.setConfig("StrictHostKeyChecking", "no");
	            session.setTimeout(15000);
	            session.connect();
	            System.out.println("connected");
	 
	        } catch (JSchException e) {
	            e.printStackTrace();
	        }
	 
	    }
	 
	 private String executeCommand(String command) {
	        String finalResult = "";
	        try {
	            String result = null;
	 
	            Channel channel = session.openChannel("exec");
	            ((ChannelExec) channel).setCommand(command);
	 
	            channel.setInputStream(null);// this method should be called before connect()
	 
	            ((ChannelExec) channel).setErrStream(System.err);
	            InputStream inputStream = channel.getInputStream();
	            channel.connect();
	            byte[] byteObject = new byte[10240];
	            while (true) {
	                while (inputStream.available() > 0) {
	                    int readByte = inputStream.read(byteObject, 0, 1024);
	                    if (readByte < 0)
	                        break;
	                    result = new String(byteObject, 0, readByte);
	                    finalResult = finalResult + result;
	                }
	                if (channel.isClosed())
	                    break;
	 
	            }
	            channel.disconnect();
	            System.out.println("Disconnected channel " + channel.getExitStatus());
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return finalResult;
	 
	    }
	 
	 public Channel connectToLinuxBox(String user, String host, String password) {
		 
		 JSch jsch = new JSch();
			Session session;
			try {
				session = jsch.getSession(user, host);
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				session.connect(60 * 1000);
				return session.openChannel("shell");
			} catch (JSchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
			
	 }

}
