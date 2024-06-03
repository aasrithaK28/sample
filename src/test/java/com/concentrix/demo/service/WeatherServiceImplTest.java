package com.concentrix.demo.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.concentrix.demo.exception.WeatherException;
import com.concentrix.demo.model.Weather;
import com.concentrix.demo.repository.WeatherRepository;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherServiceImpl weatherServiceImpl;

    
    
    @Test
    public void testCreateWeatherTable() {
        weatherServiceImpl.createWeatherTable();
        verify(weatherRepository).createTable();
    }
    
    @Test
    public void testValidateWeatherDetails_Positive() {
    	
        Weather validWeather = new Weather(12345L,"Raidurg",10.0,20.0,"low","low","low");
        assertTrue(weatherServiceImpl.validateWeatherDetails(validWeather));
    }

    @Test
    public void testValidateWeatherDetails_Negative() {
        Weather invalidWeather = new Weather(999,"Area name with more than 30 charecters is used here",-5.0,-1.0,"low","low","low");
        assertFalse(weatherServiceImpl.validateWeatherDetails(invalidWeather));
    }

    @Test
    public void testCreateWeather_Success() {
        Weather weather = new Weather(12345L,"Raidurg",12.0,24.5,"low","low","low");
        
        when(weatherRepository.existsByPinCode(weather.getPinCode())).thenReturn(false);
        when(weatherRepository.save(weather)).thenReturn(weather);

        assertEquals(weather, weatherServiceImpl.createWeather(weather));
    }

    @Test
    public void testCreateWeather_InvalidDetails() {
        Weather invalidWeather = new Weather(999,"Area name with more than 30 charecters is used here",-5.0,-1.0,"low","low","low");
        assertThrows(WeatherException.class, () -> weatherServiceImpl.createWeather(invalidWeather),
                "Please enter proper weather details i.e Invalid Weather Details Exception");
    }

    @Test
    public void testCreateWeather_Duplicate() {
        Weather weather = new Weather(12345L,"Raidurg",12.0,24.5,"low","low","low");
        when(weatherRepository.existsByPinCode(weather.getPinCode())).thenReturn(true);

        assertThrows(WeatherException.class, () -> weatherServiceImpl.createWeather(weather),
                "Weather for pin code " + weather.getPinCode() + " already exists i.e DuplicateWeatherException.");
    }

    @Test
    public void testUpdateWeather_Success() {
        long pinCode=12345L;
        Weather existingWeather = new Weather(12345L,"Raidurg",12.0,24.5,"low","low","low");
        
        Weather updatedWeather = new Weather(12345L,"DurgamCheruvu",15.0,25.0,"low","low","low");


        when(weatherRepository.findByPinCode(pinCode)).thenReturn(existingWeather);
        when(weatherRepository.save(existingWeather)).thenReturn(existingWeather);

        assertEquals(existingWeather, weatherServiceImpl.updateWeather(pinCode, updatedWeather));
        assertEquals(updatedWeather.getAreaName(), existingWeather.getAreaName());
        assertEquals(updatedWeather.getMinTemp(), existingWeather.getMinTemp());
        assertEquals(updatedWeather.getMaxTemp(), existingWeather.getMaxTemp());
    }

    @Test
    public void testUpdateWeather_NotFound() {
        Long pinCode = 12345L;
        assertThrows(WeatherException.class, () -> weatherServiceImpl.updateWeather(pinCode, new Weather()),
                "Weather not found for pin code: " + pinCode + " i.e Weather not found Exception");
    }

    @Test
    public void testUpdateWeather_InvalidDetails() {
        Long pinCode = 12345L;
        Weather existingWeather = new Weather();
        
        when(weatherRepository.findByPinCode(pinCode)).thenReturn(existingWeather);
        assertThrows(WeatherException.class, () -> weatherServiceImpl.updateWeather(pinCode, new Weather(12345L,"Area name with more than 30 charecters is used here",-5.0,-1.0,"low","low","low")),
                "Please enter proper weather details i.e Invalid Weather Details Exception");
    }

    @Test
    public void testGetWeather_Success() {
        Long pinCode = 12345L;
        Weather weather = new Weather(12345L,"Raidurg",12.0,24.5,"low","low","low");
        
        when(weatherRepository.findByPinCode(pinCode)).thenReturn(weather);

        assertEquals(weather, weatherServiceImpl.getWeather(pinCode));
    }

    @Test
    public void testGetWeather_NotFound() {
        Long pinCode = 12345L;
        when(weatherRepository.findByPinCode(pinCode)).thenReturn(null);
        assertThrows(WeatherException.class, () -> weatherServiceImpl.getWeather(pinCode),
                "Weather not found for pin code: " + pinCode + " i.e Weather not found Exception");
    }

    @Test
    public void testGetWeather_MaxTempExceeds() {
        Long pinCode = 12345L;
        Weather weather = new Weather();
        weather.setPinCode(pinCode);
        weather.setMaxTemp(40.0);

        when(weatherRepository.findByPinCode(pinCode)).thenReturn(weather);
        assertThrows(WeatherException.class, () -> weatherServiceImpl.getWeather(pinCode),
                "Maximum temperature exceeds 35°C. Danger!");
    }

    @Test
    public void testGetAllWeatherDetails() {
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(new Weather(12345L,"Raidurg",12.0,24.5,"low","low","low"));
        weatherList.add(new Weather(12346L,"DurgamCheruvu",12.0,24.5,"low","low","low"));

        when(weatherRepository.findAll()).thenReturn(weatherList);

        assertEquals(weatherList, weatherServiceImpl.getAllWeatherDetails());
    }

    @Test
    public void testDeleteWeatherDetails_NotFound() {
        Long pinCode = 12345L;
        when(weatherRepository.existsByPinCode(pinCode)).thenReturn(false);
        assertThrows(WeatherException.class, () -> weatherServiceImpl.deleteWeatherDetails(pinCode),
                "Weather not found for pin code: " + pinCode + " i.e Weather not found Exception");
    }

    @Test
    public void testDeleteWeatherDetails_Success() {
        Long pinCode = 12345L;
        when(weatherRepository.existsByPinCode(pinCode)).thenReturn(true);
        weatherServiceImpl.deleteWeatherDetails(pinCode);
        verify(weatherRepository).deleteByPinCode(pinCode);
    }
}


