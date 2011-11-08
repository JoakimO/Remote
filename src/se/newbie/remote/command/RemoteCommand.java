package se.newbie.remote.command;

/**
 * The command represent a action taken by a device.
 * Example of commands is "Play", "Stop" and "Forward".
 */
public interface RemoteCommand {
	public String getIdentifier();

	public int execute(RemoteCommandArguments arguments);
}