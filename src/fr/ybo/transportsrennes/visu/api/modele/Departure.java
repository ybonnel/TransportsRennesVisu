package fr.ybo.transportsrennes.visu.api.modele;

import java.util.Date;

public class Departure {
	
	private Date content;

	public Date getContent() {
		return content;
	}

	public void setContent(Date content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Departure [content=" + content + "]";
	}
	
}
