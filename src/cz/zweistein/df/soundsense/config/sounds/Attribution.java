package cz.zweistein.df.soundsense.config.sounds;

public class Attribution {
	
	private String url;
	private String license;
	private String author;
	private String description;
	private String note;
	
	public Attribution(String url, String license, String author,
			String description, String note) {
		super();
		this.url = url;
		this.license = license;
		this.author = author;
		this.description = description;
		this.note = note;
	}

	public String getUrl() {
		return this.url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getLicense() {
		return this.license;
	}
	
	public void setLicense(String license) {
		this.license = license;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return this.url;
	}
	
	

}
