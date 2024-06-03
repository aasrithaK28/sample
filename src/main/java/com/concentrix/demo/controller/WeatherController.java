package com.concentrix.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concentrix.demo.model.Weather;
import com.concentrix.demo.service.WeatherService;

@RestController

public class WeatherController {
	
    @Autowired
    private WeatherService weatherService;

    @PostMapping("/create")
    public ResponseEntity<Weather> createWeather(@ModelAttribute Weather weather) {
        
        Weather createdWeather = weatherService.createWeather(weather);
        return new ResponseEntity<>(createdWeather, HttpStatus.CREATED);
    }

    @PutMapping("/update/{pinCode}")
    public ResponseEntity<Weather> updateWeather(@PathVariable Long pinCode, @ModelAttribute Weather weather) {
        
        Weather updatedWeather = weatherService.updateWeather(pinCode, weather);
        return new ResponseEntity<>(updatedWeather, HttpStatus.OK);
    }

    @GetMapping("/{pinCode}")
    public ResponseEntity<Weather> getWeather(@PathVariable Long pinCode) {
        
        Weather weather = weatherService.getWeather(pinCode);
        return new ResponseEntity<>(weather, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Weather>> getAllWeatherDetails() {
        
        List<Weather> allWeatherDetails = weatherService.getAllWeatherDetails();
        return new ResponseEntity<>(allWeatherDetails, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{pinCode}")
    public ResponseEntity<Void> deleteWeatherDetails(@PathVariable Long pinCode) {
        
        weatherService.deleteWeatherDetails(pinCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

  
    
}
