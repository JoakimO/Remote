package se.newbie.remote.command;

import java.util.List;

/**
 * Macro command will execute all the commands upon execute.
 */
public class MacroRemoteCommand implements RemoteCommand {
	private List<RemoteCommand> commands;
	private String identifier;
	
	public MacroRemoteCommand(String identifier) {
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
		for (RemoteCommand command : commands) {
			status &= command.execute();
		}
		return status;
	}
}