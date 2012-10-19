package fr.ybo.transportsrennes.visu.api.modele;

public class ReponseApi {
	
	private OpenData opendata;

	public OpenData getOpendata() {
		return opendata;
	}

	public void setOpendata(OpenData opendata) {
		this.opendata = opendata;
	}

	@Override
	public String toString() {
		return "ReponseApi [opendata=" + opendata + "]";
	}

}
