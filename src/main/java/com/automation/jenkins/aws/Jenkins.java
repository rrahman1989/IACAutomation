package com.automation.jenkins.aws;

import java.io.IOException;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;

import com.automation.config.environment.ProdEnvironment;
import com.automation.utility.ssh.Expect;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Jenkins {
	
	ProdEnvironment prodInfo = ConfigFactory.create(ProdEnvironment.class);
	public String jenkinsPassword;
	
	public String installJenkinsAws(List<String> server) {
		
		String userName = prodInfo.userName();
		String password = prodInfo.password();
		System.out.println("we are at test! " + " server: " + server.get(0) + " user name: " + userName + " password: "
				+ password);
		String command = "ls -ltr";

		
		
		
		
		try {
			//Thread.sleep(3*60*1000);
			JSch jsch = new JSch();

			
			System.out.println("Elastic ip: " + server.get(0));
			
			Session session = jsch.getSession(userName, server.get(0).toString(), 22);

			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(60 * 1000);
			System.out.println(session.isConnected());
			Channel channel = session.openChannel("shell");
			Expect expect = new Expect(channel.getInputStream(), channel.getOutputStream());
			channel.connect();
			expect.expect("$");
			System.out.println("before: " + expect.before + " match: " + expect.match);
			expect.send("pwd\n");
			expect.expect("$");
			expect.send("sudo yum -y install wget\n");
			expect.expect("$");
			expect.send(
					"sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo\n");
			expect.expect("$");
			expect.send("sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key\n");
			expect.expect("$");
			expect.send("sudo yum -y install java-11-openjdk\n");
			expect.expect("$");
			expect.send("sudo yum -y install jenkins\n");
			expect.expect("$");
			expect.send("sudo chkconfig jenkins on\n");
			expect.expect("$");
			expect.send("sudo service jenkins start\n");
			expect.expect("$");
			expect.send("sudo cat /var/lib/jenkins/secrets/initialAdminPassword\n");
			expect.expect("$");
			String jenkinsPassword = expect.before;
			System.out.println("jenkins password: " + jenkinsPassword);
			System.out.println("pwd before: " + expect.before + " match: " + expect.match);
			expect.send("exit\n");
			expect.expectEOF();
			System.out.println(expect.before);
			expect.close();
			session.disconnect();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		
		return jenkinsPassword;
	}
	

}
