package com.concentrix.demo.repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.concentrix.demo.exception.WeatherException;
import com.concentrix.demo.model.Weather;

import jakarta.annotation.PostConstruct;

@Repository
public class WeatherRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public WeatherRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}


	@PostConstruct
    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS weather (" +
                "pin_code BIGINT PRIMARY KEY," +
                "area_name VARCHAR(255)," +
                "max_temp DOUBLE," +
                "min_temp DOUBLE," +
                "humidity VARCHAR(255)," +
                "rain_fall VARCHAR(255)," +
                "wind VARCHAR(255)" +
                ")";
        jdbcTemplate.execute(sql);
    }

    
    
    public Weather findByPinCode(Long pinCode) {
        String sql = "SELECT * FROM weather WHERE pin_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new WeatherRowMapper(),new Object[]{pinCode} );
        } catch (EmptyResultDataAccessException e) {
            throw new WeatherException("Weather not found for pin code: " + pinCode);
        }
    }

    public List<Weather> findAll() {
        String sql = "SELECT * FROM weather";
        return jdbcTemplate.query(sql, new WeatherRowMapper());
    }

    
    public Weather save(Weather weather) {
        if (existsByPinCode(weather.getPinCode())) {
            String sql = "UPDATE weather SET area_name = ?, max_temp = ?, min_temp = ?, humidity = ?, rain_fall = ?, wind = ? WHERE pin_code = ?";
            jdbcTemplate.update(sql, weather.getAreaName(), weather.getMaxTemp(), weather.getMinTemp(),
                    weather.getHumidity(), weather.getRainFall(), weather.getWind(), weather.getPinCode());
        } else {
            String sql = "INSERT INTO weather (pin_code, area_name, max_temp, min_temp, humidity, rain_fall, wind) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, weather.getPinCode(), weather.getAreaName(), weather.getMaxTemp(), weather.getMinTemp(),
                    weather.getHumidity(), weather.getRainFall(), weather.getWind());
        }
        return weather;
    }

    public void deleteByPinCode(Long pinCode) {
        String sql = "DELETE FROM weather WHERE pin_code = ?";
        jdbcTemplate.update(sql, pinCode);
    }

    public boolean existsByPinCode(Long pinCode) {
        String sql = "SELECT COUNT(*) FROM weather WHERE pin_code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, pinCode);

        return count != null && count > 0;
    }

    private static class WeatherRowMapper implements RowMapper<Weather> {
        @Override
        public Weather mapRow(ResultSet rs, int rowNum) throws SQLException {
            Weather weather = new Weather();
            weather.setPinCode(rs.getLong("pin_code"));
            weather.setAreaName(rs.getString("area_name"));
            weather.setMaxTemp(rs.getDouble("max_temp"));
            weather.setMinTemp(rs.getDouble("min_temp"));
            weather.setHumidity(rs.getString("humidity"));
            weather.setRainFall(rs.getString("rain_fall"));
            weather.setWind(rs.getString("wind"));
            return weather;
        }
    }
}


