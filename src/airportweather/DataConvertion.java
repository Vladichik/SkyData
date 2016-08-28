package airportweather;


import android.annotation.SuppressLint;

@SuppressLint("DefaultLocale")
public class DataConvertion {
	

	public static void ConvertMilesToKilometers(String miles){
		Double km = Double.valueOf(miles) * 1.6;
		AirportWether.airportVisKm = String.format("%.0f", Double.valueOf(km));
	}

	public static void CalculateRelativeAirHumidity(String temp, String dp){
		Double temperature = Double.parseDouble(temp);
		Double dewPoint = Double.parseDouble(dp);
		Double humidity = 100 * Math.pow((112 - 0.1 * temperature + dewPoint) / (112 + 0.9 * temperature), 8);
		AirportWether.airportHumidity = String.format("%.0f", humidity);
	}

	public static void ConvertInHgtoMilibar(String inHg){
		Double inchOfMercury = Double.parseDouble(inHg);
		Double milibar = inchOfMercury * 33.86;
		AirportWether.airportMilibar = String.format("%.0f", milibar);
	}

	public static void ConverCentigradeToFarenheit(String cent){
		Double centigrade = Double.parseDouble(cent);
		Double farenheit = centigrade * 1.8 + 32;
		AirportWether.airportTempF = String.format("%.0f", farenheit);
	}
	
	public static void FormatReportTime(String isoTime){
		String parseDateString = isoTime.substring(0,18);
		StringBuffer output = new StringBuffer().append(parseDateString.substring(8,10) + "/").append(parseDateString.substring(5,7) + " ")
				.append(parseDateString.substring(11,13) + ":").append(parseDateString.substring(14,16));
		AirportWether.airportReportTime = output.toString();   
	}

	public static String CloudCoverageConverter(String coverage){
		//System.out.println("ON ENTER " + coverage);
		if(coverage.contains("Few")){
			coverage = "2/8";
		}
		if(coverage.contains("Scattered")){
			coverage = "4/8";
		}
		if(coverage.contains("Broken")){
			coverage = "6/8";
		}
		if(coverage.contains("Overcast")){
			coverage = "8/8";
		}
		//System.out.println("ON EXIT " + coverage);
		return coverage;
	}
	
	public static String formatFlightDuration(String duration, String hourSting, String minString){
		int convertedDuration = Integer.parseInt(duration);
		int hours = convertedDuration / 60;
		String minutes = String.valueOf(convertedDuration % 60);
		String formatedDuration = hours + hourSting + " " + minutes + minString;
		System.out.println(formatedDuration);
		return formatedDuration;
	}
	
}
