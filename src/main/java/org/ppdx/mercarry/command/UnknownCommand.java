package org.ppdx.mercarry.command;

import org.springframework.stereotype.Component;

@Component
public class UnknownCommand implements Command {
    public void run(String... args) throws Exception {
				System.err.println("Unknown command: " + args[0]);
    }
}
