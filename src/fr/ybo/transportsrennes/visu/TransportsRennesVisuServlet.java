package fr.ybo.transportsrennes.visu;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import fr.ybo.transportsrennes.visu.modele.Parcour;
import fr.ybo.transportsrennes.visu.modele.ParcoursManager;

@SuppressWarnings("serial")
public class TransportsRennesVisuServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String lineId = "";
		if (req.getPathInfo() != null && req.getPathInfo().length() > 0) { 
			lineId = req.getPathInfo().substring(1);
		}
		Collection<Parcour> parcours = ParcoursManager.INSTANCE.getParcoursForLineId(lineId);
		if( parcours == null || parcours.isEmpty()) {
			resp.setStatus(404);
			resp.setContentType("text/plain");
			resp.getWriter().println(lineId + " doesn't exists");
		} else {
			resp.setContentType("application/json");
			Gson gson = new Gson();
			resp.getWriter().println(gson.toJson(parcours));
		}
	}
}
