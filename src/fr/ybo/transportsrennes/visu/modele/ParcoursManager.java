package fr.ybo.transportsrennes.visu.modele;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public enum ParcoursManager {

	INSTANCE;

	private Multimap<String, Parcour> parcours;

	private Multimap<String, Parcour> getParcours() {
		parcours = null;
		synchronized (this) {
			if (parcours == null) {
				Gson gson = new Gson();
				InputStream stream = ParcoursManager.class
						.getResourceAsStream("/fr/ybo/transportsrennes/visu/json/data.json");
				Type collectionType = new TypeToken<List<Parcour>>() {
				}.getType();
				List<Parcour> parcoursJson = gson.fromJson(
						new InputStreamReader(stream, Charset.forName("utf-8")), collectionType);

				parcours = ArrayListMultimap.create();
				for (Parcour parcour : parcoursJson) {
					parcours.put(parcour.getLigneId(), parcour);
				}
			}

		}
		return parcours;
	}
	
	public Collection<Parcour> getParcoursForLineId(String lineId) {
		return getParcours().get(lineId);
	}
	
	public Map<String, String> getLineIdByLineName() {
		Map<String, String> lineIdByLineName = new HashMap<String, String>();
		for (Parcour parcour : getParcours().values()) {
			lineIdByLineName.put(parcour.getLigneName(), parcour.getLigneId());
		}
		return lineIdByLineName;
	}

}
