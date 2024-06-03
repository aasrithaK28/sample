package com.concentrix.demo.service;

import java.util.List;

import com.concentrix.demo.model.Weather;

public interface WeatherService {
	
	void createWeatherTable();
	boolean validateWeatherDetails(Weather weather);
	Weather createWeather(Weather weather);
	Weather updateWeather(Long pinCode, Weather weather);
	Weather getWeather(Long pinCode);
	List<Weather> getAllWeatherDetails();
	void deleteWeatherDetails(Long pinCode);
	
}
