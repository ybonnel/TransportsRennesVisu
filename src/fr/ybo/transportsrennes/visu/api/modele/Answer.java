package fr.ybo.transportsrennes.visu.api.modele;

public class Answer {
	
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Answer [data=" + data + "]";
	}

}
