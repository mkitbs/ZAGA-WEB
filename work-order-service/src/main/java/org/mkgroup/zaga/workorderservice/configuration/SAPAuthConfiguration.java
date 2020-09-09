package org.mkgroup.zaga.workorderservice.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties("sap.auth")
public class SAPAuthConfiguration {
	String username;
	String password;
}
