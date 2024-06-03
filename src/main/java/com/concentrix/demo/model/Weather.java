package com.concentrix.demo.model;

public class Weather {

	private long  pinCode;
	private String areaName;
	private Double maxTemp;
	private Double minTemp;
	private String humidity;
	private String rainFall;
	private String wind;

	public Weather() {
		super();
	}

	

	public Weather(long pinCode, String areaName, Double maxTemp, Double minTemp, String humidity, String rainFall,
			String wind) {
		super();
		this.pinCode = pinCode;
		this.areaName = areaName;
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		this.humidity = humidity;
		this.rainFall = rainFall;
		this.wind = wind;
	}



	public long getPinCode() {
		return pinCode;
	}

	public void setPinCode(long pinCode) {
		this.pinCode = pinCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Double getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(Double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public Double getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(Double minTemp) {
		this.minTemp = minTemp;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getRainFall() {
		return rainFall;
	}

	public void setRainFall(String rainFall) {
		this.rainFall = rainFall;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}
	
	
	
}
