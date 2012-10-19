package fr.ybo.transportsrennes.visu;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.ybo.transportsrennes.visu.api.modele.Line;
import fr.ybo.transportsrennes.visu.api.modele.ReponseApi;
import fr.ybo.transportsrennes.visu.modele.Ligne;
import fr.ybo.transportsrennes.visu.modele.ParcoursManager;

@SuppressWarnings("serial")
public class LinesServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Map<String, String> lineIdByLineName = ParcoursManager.INSTANCE.getLineIdByLineName();
		
		ReponseApi reponseApi = getReponseApi();

		List<Ligne> lignes = new ArrayList<Ligne>();
		String baseUrl = reponseApi.getOpendata().getAnswer().getData().getBaseurl();
		for (Line line : reponseApi.getOpendata().getAnswer().getData().getLine()) {
			if (lineIdByLineName.containsKey(line.getName())) {
				Ligne ligne = new Ligne();
				ligne.setLineId(lineIdByLineName.get(line.getName()));
				ligne.setLineName(line.getName());
				ligne.setPictoUrl(baseUrl + line.getPicto());
				lignes.add(ligne);
			}
		}
		
		Collections.sort(lignes, new Comparator<Ligne>() {
			@Override
			public int compare(Ligne o1, Ligne o2) {
				return o1.getLineId().compareTo(o2.getLineId());
			}});
		
		
		resp.setContentType("application/json; charset=UTF-8");
		Gson gson = new Gson();
		resp.getWriter().println(gson.toJson(lignes));
		
	}
	


	private ReponseApi getReponseApi() {
		Reader reader = new InputStreamReader(openInputStream());
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		ReponseApi reponseApi = gson.fromJson(reader, ReponseApi.class);
		return reponseApi;
	}

	private static InputStream openInputStream() {
		if (Constantes.BOUCHON) {
			return PositionBusServlet.class
					.getResourceAsStream("/fr/ybo/transportsrennes/visu/json/lines.json");
		}
		StringBuilder urlString = new StringBuilder(
				"http://data.keolis-rennes.com/json/?cmd=");
		urlString.append("getlines");
		urlString.append("&version=");
		urlString.append("2.0");
		urlString.append("&key=");
		urlString.append("G7JE45LI1RK3W1P");
		urlString.append("&param[size]=");
		urlString.append("100");

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
