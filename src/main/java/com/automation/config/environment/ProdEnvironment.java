package com.automation.config.environment;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources({
	
	"classpath:prod.properties"
})

public interface ProdEnvironment extends Config {
	
	public String serverName();
	public String userName();
	public String password();
	public String accessKey();
	public String secretKey();
	public String amiId();
	public String instanceType();
	public String keyName();

}
