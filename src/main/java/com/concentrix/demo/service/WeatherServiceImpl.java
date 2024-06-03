package com.concentrix.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concentrix.demo.exception.WeatherException;
import com.concentrix.demo.model.Weather;
import com.concentrix.demo.repository.WeatherRepository;

@Service
public class WeatherServiceImpl implements WeatherService{
	
	@Autowired
	private WeatherRepository weatherRepository;
	
	@Override
	public void createWeatherTable() {
		weatherRepository.createTable();		
	}
	

	@Override
	public boolean validateWeatherDetails(Weather weather) {
		
		boolean pinCodeValidation=(10000<= weather.getPinCode())&&(weather.getPinCode()<=99999);
		boolean areaNameValidation=weather.getAreaName().length()<=30;
		boolean TemperatureValidation=weather.getMinTemp()>0.0 && weather.getMaxTemp()>0.0;
		if(pinCodeValidation && areaNameValidation && TemperatureValidation) {
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public Weather createWeather(Weather weather) {
	    
		
		createWeatherTable();
		
		if(!validateWeatherDetails(weather)) {
			throw new WeatherException("Please enter proper weather details i.e Invalid Weather Details Exception");
		}

		if (!weatherRepository.existsByPinCode(weather.getPinCode())) {
			
	        return weatherRepository.save(weather);
	        
	    } else {
	        throw new WeatherException("Weather for pin code " + weather.getPinCode() + " already exists i.e DuplicateWeatherException.");
	    }
		
	}
	
	
	
	@Override
	public Weather updateWeather(Long pinCode, Weather weather) {
	    Weather existingWeather = weatherRepository.findByPinCode(pinCode);
	    if (existingWeather == null) {
	        throw new WeatherException("Weather not found for pin code: " + pinCode+" i.e Weather not found Exception");
	    }

	    if (!validateWeatherDetails(weather)) {
	        throw new WeatherException("Please enter proper weather details i.e Invalid Weather Details Exception");
	    }

	    
	    existingWeather.setAreaName(weather.getAreaName());
	    existingWeather.setMaxTemp(weather.getMaxTemp());
	    existingWeather.setMinTemp(weather.getMinTemp());
	    existingWeather.setHumidity(weather.getHumidity());
	    existingWeather.setRainFall(weather.getRainFall());
	    existingWeather.setWind(weather.getWind());

	    return weatherRepository.save(existingWeather);
	}

	


	@Override
	public Weather getWeather(Long pinCode) {
		Weather weather = weatherRepository.findByPinCode(pinCode);
	    if (weather == null) {
	        throw new WeatherException("Weather not found for pin code: " + pinCode+" i.e Weather not found Exception");
	    }else if (weather.getMaxTemp() > 35.00) {
	        throw new WeatherException("Maximum temperature exceeds 35°C. Danger!");
	    }
	    return weather;
	}

	@Override
	public List<Weather> getAllWeatherDetails() {
		return weatherRepository.findAll();
	}

	@Override
	public void deleteWeatherDetails(Long pinCode) {
		
		if (!weatherRepository.existsByPinCode(pinCode)) {
            throw new WeatherException("Weather not found for pin code: " + pinCode+" i.e Weather not found Exception");
        }
		weatherRepository.deleteByPinCode(pinCode);
	}


}
