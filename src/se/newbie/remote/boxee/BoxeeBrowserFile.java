package se.newbie.remote.boxee;

public class BoxeeBrowserFile {
	public enum FileType {
		file, directory
	}
	
	private FileType fileType;
	private String file;
	private String label;
	
	public BoxeeBrowserFile(FileType fileType, String label, String file) {
		this.fileType = fileType;
		this.label = label;
		this.file = file;
	}
	
	public FileType getFileType() {
		return fileType;
	}

	public String getLabel() {
		return label;
	}	
	
	public String getFile() {
		return file;
	}
}
