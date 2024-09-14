package org.ppdx.mercarry.command;

import java.util.Arrays;

import org.springframework.context.ConfigurableApplicationContext;

public class CommandManager {
    public static void run(ConfigurableApplicationContext context, String... args) throws Exception {
        String cmdName = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);

        Command cmd = null;
        switch (cmdName) {
					case "populatedb":
                cmd = context.getBean(PopulatedbCommand.class);
                break;
            default:
                cmd = context.getBean(UnknownCommand.class);
								args = new String[] {cmdName};
                break;
        }
        cmd.run(args);
    }
}
