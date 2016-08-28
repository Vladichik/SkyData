package flightsdepartures;

public class DeparturesListItemModel {
	private String airlineCode, flightNumber, flightID, composedFlightNumber, destinationCity, destinationAirport, terminal, gate, flightTime, flightDate, remarks, remarkCode, airlineLogo;

	
	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getFlightID() {
		return flightID;
	}

	public void setFlightID(String flightID) {
		this.flightID = flightID;
	}

	public String getComposedFlightNumber() {
		return composedFlightNumber;
	}

	public void setComposedFlightNumber(String composedFlightNumber) {
		this.composedFlightNumber = composedFlightNumber;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDestinationAirport() {
		return destinationAirport;
	}

	public void setDestinationAirport(String destinationAirport) {
		this.destinationAirport = destinationAirport;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public String getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(String flightTime) {
		this.flightTime = flightTime;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRemarkCode() {
		return remarkCode;
	}

	public void setRemarkCode(String remarkCode) {
		this.remarkCode = remarkCode;
	}

	public String getAirlineLogo() {
		return airlineLogo;
	}

	public void setAirlineLogo(String airlineLogo) {
		this.airlineLogo = airlineLogo;
	}

	public DeparturesListItemModel(String airlineCode, String flightNumber,
			String flightID, String composedFlightNumber,
			String destinationCity, String destinationAirport, String terminal,
			String gate, String flightTime, String flightDate, String remarks,
			String remarkCode, String airlineLogo) {
		super();
		this.airlineCode = airlineCode;
		this.flightNumber = flightNumber;
		this.flightID = flightID;
		this.composedFlightNumber = composedFlightNumber;
		this.destinationCity = destinationCity;
		this.destinationAirport = destinationAirport;
		this.terminal = terminal;
		this.gate = gate;
		this.flightTime = flightTime;
		this.flightDate = flightDate;
		this.remarks = remarks;
		this.remarkCode = remarkCode;
		this.airlineLogo = airlineLogo;
	}

	@Override
	public String toString() {
		return airlineCode + " " + flightNumber + " " + flightID + " " + composedFlightNumber + " " + destinationCity + " " + destinationAirport + " " + terminal + " " + gate + " " + flightTime + " " + flightDate + " " + remarks + " " + remarkCode + " " + airlineLogo;
	}
	
	

}
