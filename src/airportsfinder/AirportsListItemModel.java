package airportsfinder;

public class AirportsListItemModel {
	private String foundAirportName, foundAirportICAO;
	
	
	//******** SETTERS AND GETTERS **********//

	public String getFoundAirportName() {
		return foundAirportName;
	}
	public void setFoundAirportName(String foundAirportName) {
		this.foundAirportName = foundAirportName;
	}
	public String getFoundAirportICAO() {
		return foundAirportICAO;
	}
	public void setFoundAirportICAO(String foundAirportICAO) {
		this.foundAirportICAO = foundAirportICAO;
	}
	//***************************************//
	
	//*************** CONSTRUCTOR ***********//
	public AirportsListItemModel(String foundAirportName,
			String foundAirportICAO) {
		super();
		this.foundAirportName = foundAirportName;
		this.foundAirportICAO = foundAirportICAO;
	}
	@Override
	public String toString() {
		
		return foundAirportName + " " + foundAirportICAO;
	}
	
	
}
