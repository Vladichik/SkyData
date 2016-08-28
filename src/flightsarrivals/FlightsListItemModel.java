package flightsarrivals;

public class FlightsListItemModel {
	private  String flightID, airlineCode, flightNumber, remarks, city, scheduledTime, currentTime, terminal, airlineImage;

	public String getFlightID() {
		return flightID;
	}

	public void setFlightID(String flightID) {
		this.flightID = flightID;
	}

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public String getAirlineImage() {
		return airlineImage;
	}

	public void setAirlineImage(String airlineImage) {
		this.airlineImage = airlineImage;
	}
	
	
	public FlightsListItemModel(String flightID, String airlineCode,
			String flightNumber, String remarks, String city,
			String scheduledTime, String currentTime, String terminal,
			String airlineImage) {
		super();
		this.flightID = flightID;
		this.airlineCode = airlineCode;
		this.flightNumber = flightNumber;
		this.remarks = remarks;
		this.city = city;
		this.scheduledTime = scheduledTime;
		this.currentTime = currentTime;
		this.terminal = terminal;
		this.airlineImage = airlineImage;
	}

	@Override
	public String toString() {
		return flightID + " " + airlineCode + " " + flightNumber + " " + remarks + " " + city + " " + scheduledTime + " " + currentTime + " " + terminal + " " + airlineImage;
	}



}
