package fr.ybo.transportsrennes.visu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import fr.ybo.transportsrennes.visu.api.modele.ReponseApi;
import fr.ybo.transportsrennes.visu.api.modele.StopLine;
import fr.ybo.transportsrennes.visu.modele.Parcour;
import fr.ybo.transportsrennes.visu.modele.Parcour.Arret;
import fr.ybo.transportsrennes.visu.modele.ParcoursManager;

@SuppressWarnings("serial")
public class PositionBusServlet extends HttpServlet {

	private final class DateDeserialiser implements JsonDeserializer<Date> {
		DateFormat dfm = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");

		@Override
		public Date deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2)
				throws JsonParseException {
			try {
				return dfm.parse(arg0.getAsJsonPrimitive()
						.getAsString().split("\\+")[0]);
			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String lineId = null;
		Integer macroDirection = null;
		if (req.getPathInfo() != null && req.getPathInfo().length() > 0) {
			String path = req.getPathInfo().substring(1);
			String[] champs = path.split("/");
			if (champs.length == 2) {
				lineId = champs[0];
				macroDirection = Integer.parseInt(champs[1]);
			}
		}

		if (lineId == null || macroDirection == null) {
			resp.setStatus(404);
			resp.setContentType("text/plain");
			resp.getWriter().println("Position doesn't exists");
			return;
		}

		Parcour monParcour = getMonParcours(lineId, macroDirection);

		if (monParcour == null) {
			resp.setStatus(404);
			resp.setContentType("text/plain");
			resp.getWriter().println("Parcour doesn't exists");
			return;
		}

		ReponseApi reponseApi = getReponseApi(lineId, macroDirection);

		Map<String, Date> nextDepartureByStop = constructNextDepartureByStop(reponseApi);

		List<String> lastStopOfBus = contructLastStopOfBus(monParcour,
				nextDepartureByStop);

		resp.setStatus(200);

		resp.setContentType("application/json; charset=UTF-8");
		Gson gson = new Gson();
		resp.getWriter().println(gson.toJson(lastStopOfBus));
	}

	private List<String> contructLastStopOfBus(Parcour monParcour,
			Map<String, Date> nextDepartureByStop) {
		List<String> lastStopOfBus = new ArrayList<String>();
		Date currentDate = null;
		String lastStop = null;
		for (Arret arret : monParcour.getStops()) {
			if (nextDepartureByStop.containsKey(arret.getStopId())) {
				if (currentDate != null
						&& nextDepartureByStop.get(arret.getStopId()).before(
								currentDate)) {
					lastStopOfBus.add(lastStop);
				}
				currentDate = nextDepartureByStop.get(arret.getStopId());
				lastStop = arret.getStopId();
			}
		}
		return lastStopOfBus;
	}

	private Map<String, Date> constructNextDepartureByStop(ReponseApi reponseApi) {
		Map<String, Date> nextDepartureByStop = new HashMap<String, Date>();

		for (StopLine stopLine : reponseApi.getOpendata().getAnswer().getData()
				.getStopline()) {
			if (!stopLine.getDepartures().getDeparture().isEmpty()) {
				nextDepartureByStop.put(stopLine.getStop(), stopLine
						.getDepartures().getDeparture().get(0).getContent());
			}
		}
		return nextDepartureByStop;
	}

	private ReponseApi getReponseApi(String lineId, Integer macroDirection) {
		Reader reader = new InputStreamReader(openInputStream(lineId,
				macroDirection));

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class,
				new DateDeserialiser());
		Gson gson = gsonBuilder.create();
		ReponseApi reponseApi = gson.fromJson(reader, ReponseApi.class);
		return reponseApi;
	}

	private Parcour getMonParcours(String lineId, Integer macroDirection) {
		Collection<Parcour> parcours = ParcoursManager.INSTANCE
				.getParcoursForLineId(lineId);
		Parcour monParcour = null;
		for (Parcour parcour : parcours) {
			if (parcour.getLigneId().equals(lineId)
					&& parcour.getMacroDirection().equals(macroDirection)) {
				monParcour = parcour;
				break;
			}
		}
		return monParcour;
	}

	private static InputStream openInputStream(String lineId,
			Integer macroDirection) {
		if (Constantes.BOUCHON) {
			return PositionBusServlet.class
					.getResourceAsStream("/fr/ybo/transportsrennes/visu/json/tempsReel"
							+ macroDirection + ".json");
		}
		StringBuilder urlString = new StringBuilder(
				"http://data.keolis-rennes.com/json/?cmd=");
		urlString.append("getbusnextdepartures");
		urlString.append("&version=");
		urlString.append("2.1");
		urlString.append("&key=");
		urlString.append("G7JE45LI1RK3W1P");
		urlString.append("&param[mode]=");
		urlString.append("line");
		urlString.append("&param[route]=");
		urlString.append(lineId);
		urlString.append("&param[direction]=");
		urlString.append(macroDirection);

		try {
			URL myUrl = new URL(urlString.toString());
			URLConnection connection = myUrl.openConnection();
			connection.setConnectTimeout(Constantes.CONNECT_TIMEOUT);
			connection.setReadTimeout(Constantes.READ_TIMEOUT);
			return connection.getInputStream();
		} catch (Exception exception) {
			Throwables.propagate(exception);
			return null;
		}
	}
}
