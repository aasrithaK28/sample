package com.concentrix.demo.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import com.concentrix.demo.model.Weather;
//import com.concentrix.demo.service.WeatherServiceImpl;
//
//@ExtendWith(MockitoExtension.class)
//class WeatherControllerTest {
//
//    @Mock
//    private WeatherServiceImpl weatherServiceImpl;
//
//    @InjectMocks
//    private WeatherController weatherController;
//
//
//    @Test
//    void testCreateWeather() {
//        Weather weather = new Weather();
//        when(weatherServiceImpl.createWeather(any())).thenReturn(weather);
//        ResponseEntity<Weather> responseEntity = weatherController.createWeather(weather);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(weather, responseEntity.getBody());
//    }
//
//    @Test
//    void testUpdateWeather() {
//        long pinCode = 12345L;
//        Weather weather = new Weather();
//        when(weatherServiceImpl.updateWeather(eq(pinCode), any())).thenReturn(weather);
//        ResponseEntity<Weather> responseEntity = weatherController.updateWeather(pinCode, weather);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(weather, responseEntity.getBody());
//    }
//
//    @Test
//    void testGetWeather() {
//        long pinCode = 12345L;
//        Weather weather = new Weather();
//        when(weatherServiceImpl.getWeather(eq(pinCode))).thenReturn(weather);
//        ResponseEntity<Weather> responseEntity = weatherController.getWeather(pinCode);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(weather, responseEntity.getBody());
//    }
//
//    @Test
//    void testGetAllWeatherDetails() {
//        List<Weather> weatherList = new ArrayList<>();
//        when(weatherServiceImpl.getAllWeatherDetails()).thenReturn(weatherList);
//        ResponseEntity<List<Weather>> responseEntity = weatherController.getAllWeatherDetails();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(weatherList, responseEntity.getBody());
//    }
//
//    @Test
//    void testDeleteWeatherDetails() {
//        long pinCode = 12345L;
//        ResponseEntity<Void> responseEntity = weatherController.deleteWeatherDetails(pinCode);
//        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
//        verify(weatherServiceImpl, times(1)).deleteWeatherDetails(pinCode);
//    }
//}

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.concentrix.demo.model.Weather;
import com.concentrix.demo.service.WeatherService;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testCreateWeather() throws Exception {
        Weather weather = new Weather();
        weather.setPinCode(123456L);

        when(weatherService.createWeather(any(Weather.class))).thenReturn(weather);

        mockMvc.perform(MockMvcRequestBuilders.post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pinCode\": 123456}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testUpdateWeather() throws Exception {
        Weather weather = new Weather();
        weather.setPinCode(123456L);

        when(weatherService.updateWeather(eq(123456L), any(Weather.class))).thenReturn(weather);

        mockMvc.perform(MockMvcRequestBuilders.put("/update/123456")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pinCode\": 123456}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetWeather() throws Exception {
        Weather weather = new Weather();
        weather.setPinCode(123456L);

        when(weatherService.getWeather(eq(123456L))).thenReturn(weather);

        mockMvc.perform(MockMvcRequestBuilders.get("/123456"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAllWeatherDetails() throws Exception {
        List<Weather> weatherList = new ArrayList<>();
        Weather weather1 = new Weather();
        weather1.setPinCode(123456L);
        weatherList.add(weather1);

        when(weatherService.getAllWeatherDetails()).thenReturn(weatherList);

        mockMvc.perform(MockMvcRequestBuilders.get("/all"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteWeatherDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/delete/123456"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}



