package com.automation.ec2.aws;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.aeonbits.owner.ConfigFactory;

import com.automation.config.environment.ProdEnvironment;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.AllocateAddressRequest;
import software.amazon.awssdk.services.ec2.model.AllocateAddressResponse;
import software.amazon.awssdk.services.ec2.model.AssociateAddressRequest;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.DomainType;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.InstanceType;
import software.amazon.awssdk.services.ec2.model.RunInstancesRequest;
import software.amazon.awssdk.services.ec2.model.RunInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Tag;

public class EC2Instance {

	ProdEnvironment prodInfo = ConfigFactory.create(ProdEnvironment.class);

	// Set your access key and secret key
	static String accessKey;
	static String secretKey;

	// Set the AWS region and instance details
	static Region region = Region.US_EAST_1;
	static String amiId;
	static String instanceType;
	static String keyName;



	public List<String> createEc2WithElasticIP() {

		List<String> ec2InstanceInfo = new ArrayList();

		accessKey = prodInfo.accessKey();
		secretKey = prodInfo.secretKey();
		amiId = prodInfo.amiId();
		instanceType = prodInfo.instanceType();
		keyName = prodInfo.keyName();

		// Create the EC2 client with the provided access key and secret key
		Ec2Client ec2Client = Ec2Client.builder().region(region)
				.credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
				.build();

		// Set the user data script to enable password-based login
		String userData = "#cloud-config\n" + "password: "+prodInfo.password()+"\n" + "chpasswd: { expire: False }\n"
				+ "ssh_pwauth: True\n";

		String encodedUserData = Base64.getEncoder().encodeToString(userData.getBytes(StandardCharsets.UTF_8));

		// Create the request to run the instance
		RunInstancesRequest runRequest = RunInstancesRequest.builder().imageId(amiId)
				.instanceType(InstanceType.fromValue(instanceType)).keyName(keyName).securityGroups("launch-wizard-2")
				.userData(encodedUserData).minCount(1).maxCount(1).build();

		// Launch the instance
		RunInstancesResponse response = ec2Client.runInstances(runRequest);

		// Get the instance ID of the created instance
		String instanceId = response.instances().get(0).instanceId();

		// Add tags to the instance (optional)
		Tag tag = Tag.builder().key("Name").value("IACJenkins").build();

		CreateTagsRequest tagRequest = CreateTagsRequest.builder().resources(instanceId).tags(tag).build();

		ec2Client.createTags(tagRequest);

		System.out.println("EC2 instance created with ID: " + instanceId);

		// Wait for the instance to reach the "running" state
		WaitForInstanceState(ec2Client, instanceId, InstanceStateName.RUNNING);

		// Get the public IP address of the instance
		DescribeInstancesResponse describeResponse = ec2Client
				.describeInstances(DescribeInstancesRequest.builder().instanceIds(instanceId).build());
		String publicIpAddress = describeResponse.reservations().get(0).instances().get(0).publicIpAddress();

		// Allocate an Elastic IP address
		AllocateAddressResponse allocateResponse = ec2Client
				.allocateAddress(AllocateAddressRequest.builder().domain(DomainType.VPC).build());
		String allocationId = allocateResponse.allocationId();
		String publicIp = allocateResponse.publicIp();

		// Associate the Elastic IP address with your instance
		AssociateAddressRequest associateRequest = AssociateAddressRequest.builder().instanceId(instanceId)
				.allocationId(allocationId).build();
		ec2Client.associateAddress(associateRequest);

		System.out.println("Elastic IP address allocated: " + publicIp);

		System.out.println("public ip address: " + publicIpAddress);
		
		ec2InstanceInfo.add(publicIp);
		ec2InstanceInfo.add(publicIpAddress);

		return ec2InstanceInfo;
	}

	private static void WaitForInstanceState(Ec2Client ec2Client, String instanceId, InstanceStateName state) {
		boolean instanceInDesiredState = false;

		while (!instanceInDesiredState) {
			DescribeInstancesResponse describeResponse = ec2Client
					.describeInstances(DescribeInstancesRequest.builder().instanceIds(instanceId).build());

			Instance instance = describeResponse.reservations().get(0).instances().get(0);
			if (instance.state().name() == state) {
				instanceInDesiredState = true;
			} else {
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
