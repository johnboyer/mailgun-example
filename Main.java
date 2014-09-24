/*
Main.java
2014-09-24

Copyright 2014 John Boyer

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.rodaxsoft.mailgun.example;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 * Example Main class using the latest Jersey Client SDK 2.12
 * @author John Boyer
 *
 */
public class Main {

	public static void main(String[] args) {
		Log log = LogFactory.getLog(Main.class);
		
		final Client client = ClientBuilder.newClient();
		
		HttpAuthenticationFeature authFeature;
		authFeature = HttpAuthenticationFeature.
				basicBuilder().
				nonPreemptive().
				credentials("api", "key-3ax6xnjp29jd6fds4gc373sgvjxteol0").
				build();
		client.register(authFeature);

		WebTarget webTarget = client.
				target("https://api.mailgun.net/v2/samples.mailgun.org").
				path("messages");
		
		Form form = new Form();
		form.param("to", "john@example.com");
		form.param("from", "noreply@example.com");
		form.param("subject", "Hello World");
		form.param("text", "This is my first Mailgun email from Java");
		//Set the Reply-TO header
		form.param("h:Reply-To", "support@example.com");
		
		Invocation.Builder invocationBuilder;
		invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
		Response response = invocationBuilder.post(Entity.form(form));
		int status = response.getStatus();
		log.info("status = " + status);
		
		if(response.hasEntity()) {
			String json = response.readEntity(String.class);
			log.info("entity = " + json);
		}
	}

}
