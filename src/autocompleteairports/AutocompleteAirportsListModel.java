package autocompleteairports;

public class AutocompleteAirportsListModel {
	String name, iata;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcao() {
		return iata;
	}

	public void setIcao(String icao) {
		this.iata = icao;
	}

	public AutocompleteAirportsListModel(String name, String iata) {
		super();
		this.name = name;
		this.iata = iata;
	}

	@Override
	public String toString() {
		return name + " " + iata;
	}
	
	

}
