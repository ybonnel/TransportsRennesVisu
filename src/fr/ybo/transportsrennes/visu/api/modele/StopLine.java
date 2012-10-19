package fr.ybo.transportsrennes.visu.api.modele;

public class StopLine {

	private Departures departures;
	private Integer direction;
	private String route;
	private String stop;
	
	public Departures getDepartures() {
		return departures;
	}
	public void setDepartures(Departures departures) {
		this.departures = departures;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public String getStop() {
		return stop;
	}
	public void setStop(String stop) {
		this.stop = stop;
	}
	@Override
	public String toString() {
		return "StopLine [departures=" + departures + ", direction="
				+ direction + ", route=" + route + ", stop=" + stop + "]";
	}
	
	
	
}
