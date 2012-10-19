package fr.ybo.transportsrennes.visu.api.modele;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private List<StopLine> stopline;
	
	private String baseurl;
	
	private List<Line> line;

	public List<StopLine> getStopline() {
		if (stopline == null) {
			stopline = new ArrayList<StopLine>();
		}
		return stopline;
	} 

	public String getBaseurl() {
		return baseurl;
	}
	
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	
	public List<Line> getLine() {
		if (line == null) {
			return new ArrayList<Line>();
		}
		return line;
	}
	
	@Override
	public String toString() {
		return "Data [stopline=" + stopline + "]";
	}
	
}
