package fr.ybo.transportsrennes.visu.api.modele;

import java.util.ArrayList;
import java.util.List;

public class Departures {
	
	private List<Departure> departure;

	public List<Departure> getDeparture() {
		if (departure == null) {
			departure = new ArrayList<Departure>();
		}
		return departure;
	}

	@Override
	public String toString() {
		return "Departures [departure=" + departure + "]";
	}

}
