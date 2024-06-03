package com.concentrix.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.TestPropertySource;

import com.concentrix.demo.exception.WeatherException;
import com.concentrix.demo.model.Weather;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class WeatherRepositoryTest {
    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    private IDatabaseConnection dbUnitConnection;

    @BeforeEach
    public void setUp() throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(dataSourceUrl);
        dataSource.setUsername(dataSourceUsername);
        dataSource.setPassword(dataSourcePassword);

        dbUnitConnection = new DatabaseConnection(dataSource.getConnection());
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(getClass().getClassLoader().getResourceAsStream("weather-data.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConnection, dataSet);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (dbUnitConnection != null) {
            dbUnitConnection.close();
        }
    }

    @Test
    public void testFindByPinCode() {
        Weather weather = weatherRepository.findByPinCode(123456L);
        assertNotNull(weather);
        assertEquals("Karimnagar", weather.getAreaName());
        assertEquals(25.5, weather.getMaxTemp());
        assertEquals(15.5, weather.getMinTemp());
        assertEquals("low", weather.getHumidity());
        assertEquals("high", weather.getRainFall());
        assertEquals("high", weather.getWind());
    }

    @Test
    public void testFindByPinCode_NotFound() {
        assertThrows(WeatherException.class, () -> weatherRepository.findByPinCode(999999L));
    }

    @Test
    public void testFindAll() {
        List<Weather> allWeather = weatherRepository.findAll();
        assertEquals(2, allWeather.size()); 
    }


    
    
    @Test
    public void testSave_Update() {
      
        Weather existingWeather =weatherRepository.findByPinCode(123456L);
        existingWeather.setAreaName("Delhi");
        existingWeather.setMaxTemp(20.0);
        existingWeather.setMinTemp(15.0);
        existingWeather.setHumidity("high");
        existingWeather.setRainFall("high");
        existingWeather.setWind("high");
        
        Weather savedWeather=weatherRepository.save(existingWeather);
       
        Weather retreivedWeather=weatherRepository.findByPinCode(123456L);
        
        assertEquals(retreivedWeather.getPinCode(), savedWeather.getPinCode());
        assertEquals(retreivedWeather.getAreaName(), savedWeather.getAreaName());
        assertEquals(retreivedWeather.getMaxTemp(), savedWeather.getMaxTemp(), 0.01);
        assertEquals(retreivedWeather.getMinTemp(), savedWeather.getMinTemp(), 0.01);
        assertEquals(retreivedWeather.getHumidity(), savedWeather.getHumidity());
        assertEquals(retreivedWeather.getRainFall(), savedWeather.getRainFall());
        assertEquals(retreivedWeather.getWind(), savedWeather.getWind());
        
    }

    @Test
    public void testSave_NewWeather() {
 
        Weather newWeather = new Weather(999999L,"Alwal",30.0,5.0,"high","high","low");
        
        Weather savedWeather = weatherRepository.save(newWeather);

        assertEquals(newWeather.getPinCode(), savedWeather.getPinCode());
        assertEquals(newWeather.getAreaName(), savedWeather.getAreaName());
        assertEquals(newWeather.getMaxTemp(), savedWeather.getMaxTemp(), 0.01);
        assertEquals(newWeather.getMinTemp(), savedWeather.getMinTemp(), 0.01);
        assertEquals(newWeather.getHumidity(), savedWeather.getHumidity());
        assertEquals(newWeather.getRainFall(), savedWeather.getRainFall());
        assertEquals(newWeather.getWind(), savedWeather.getWind());
    }


    @Test
    public void testDeleteByPinCode() {
        weatherRepository.deleteByPinCode(123456L);
        assertFalse(weatherRepository.existsByPinCode(123456L));
    }

    @Test
    public void testExistsByPinCode() {
        assertTrue(weatherRepository.existsByPinCode(123456L));
        assertFalse(weatherRepository.existsByPinCode(999999L));
    }
   
    
}



