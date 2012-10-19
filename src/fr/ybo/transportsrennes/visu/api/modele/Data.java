package fr.ybo.transportsrennes.visu.api.modele;

import java.util.ArrayList;
import java.util.List;

public class Data {

	private List<StopLine> stopline;

	public List<StopLine> getStopline() {
		if (stopline == null) {
			stopline = new ArrayList<StopLine>();
		}
		return stopline;
	}

	@Override
	public String toString() {
		return "Data [stopline=" + stopline + "]";
	}
	
}
