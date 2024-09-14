package org.ppdx.mercarry;

import org.ppdx.mercarry.command.CommandManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MercarryApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication app = new SpringApplication(MercarryApplication.class);
		if (args.length > 0) {
			app.setWebApplicationType(WebApplicationType.NONE);
			CommandManager.run(app.run(args), args);
		} else {
			app.run(args);
		}
	}
}
