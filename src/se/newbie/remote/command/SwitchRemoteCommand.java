package se.newbie.remote.command;

import java.util.List;

/**
 * Switch commands switches command after each execute, starting
 * with the first command and then the next.
 *
 * Command only switches if the executed command succeeds.
 *
 * Example of switch commands are on/off commands.
 */
public class SwitchRemoteCommand implements RemoteCommand {
	private List<RemoteCommand> commands;
	private int commandIndex = 0;
	private String identifier;
	
	public SwitchRemoteCommand(String identifier) {
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return this.identifier;
	}	

	public void addCommand(RemoteCommand command) {
		this.commands.add(command);
	}
	
	public void removeCommand(RemoteCommand command) {
		this.commands.remove(command);
	}
	
	public int execute() {
		int status = 1;
		if (commands.size() > 0) {
			if (commandIndex >= commands.size()) {
				commandIndex = 0;
			}	
			status = this.commands.get(commandIndex).execute();
			if (status > 0) {
				commandIndex = commandIndex++;
			}
		}
		return status;
	}	
}