package com.automation.automation;

import java.util.ArrayList;
import java.util.List;

import org.aeonbits.owner.ConfigFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.automation.config.environment.ProdEnvironment;
import com.automation.ec2.aws.EC2Instance;
import com.automation.jenkins.aws.Jenkins;

public class Automation {


	static EC2Instance ec2 = new EC2Instance();
	


	static List<String> server = new ArrayList<>();
	
	ProdEnvironment prodInfo = ConfigFactory.create(ProdEnvironment.class);

	static Jenkins jenkins = new Jenkins();
	
	static String info;
	
	@BeforeTest
	public void before() {
		System.out.println("We are at before");		
	}


	@Test
	public void createEC2() {
		System.out.println("We are in createEC2()");
		server = ec2.createEc2WithElasticIP();

	}

	@Test
	public void jenkinsInstallation() {

		System.out.println("We are in jenkinsInstallation()");
		
		info = jenkins.installJenkinsAws(server);
		
		System.out.println("initial Admin password: " + info);
		


	}

	@AfterTest
	public void afterTest() {
		System.out.println("We are after test");
	}

}
