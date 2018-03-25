package hr.cityapi.ws.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import hr.cityapi.ws.dao.CityDao;
import hr.cityapi.ws.model.City;
import hr.cityapi.ws.model.FavouriteCity;
import hr.cityapi.ws.model.User;

@Repository
public class CityDaoImpl implements CityDao {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	 public void setDataSource(DataSource dataSource) {
	  this.jdbcTemplate = new JdbcTemplate(dataSource);
	 }

	public City getCity(String name) {
		String sql = "SELECT * FROM city WHERE name='" + name + "'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<City>() {

			public City extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					City city = new City();
					city.setId(rs.getLong("id"));
					city.setName(rs.getString("name"));
					city.setDescription(rs.getString("description"));
					city.setPopulation(rs.getLong("population"));
					city.setCreationDate(rs.getDate("creationdate"));
					city.setFavouriteCount(rs.getLong("favouritecount"));
					return city;
				}

				return null;
			}
		});
	}

	public List<City> getAllCities() {
		String sql = "SELECT * FROM city";
		List<City> cities = jdbcTemplate.query(sql, new RowMapper<City>() {

			public City mapRow(ResultSet rs, int rowNum) throws SQLException {
				City city = new City();
				city.setId(rs.getLong("id"));
				city.setName(rs.getString("name"));
				city.setDescription(rs.getString("description"));
				city.setPopulation(rs.getLong("population"));
				city.setCreationDate(rs.getDate("creationdate"));
				city.setFavouriteCount(rs.getLong("favouritecount"));
				return city;
			}

		});

		return cities;
	}

	public City createCity(City city) {
		String sql = "INSERT INTO city (name, description, population, creationdate, favouritecount)"
				+ " VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, city.getName(), city.getDescription(),
				city.getPopulation(), city.getCreationDate(),
				city.getFavouriteCount());
		return city;

	}

	public City updateCity(City city) {
		String sql = "UPDATE city SET favouritecount=?" + " WHERE name=?";
		jdbcTemplate.update(sql, city.getFavouriteCount(), city.getName());
		return city;
	}

	public boolean cityExists(City city) {
		City cityToCheck = getCity(city);
		return cityToCheck != null;
	}

	private City getCity(City city) {
		String sql = "SELECT * FROM city WHERE name='" + city.getName() + "'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<City>() {

			public City extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					City city = new City();
					city.setId(rs.getLong("id"));
					city.setName(rs.getString("name"));
					city.setDescription(rs.getString("description"));
					city.setPopulation(rs.getLong("population"));
					city.setCreationDate(rs.getDate("creationdate"));
					city.setFavouriteCount(rs.getLong("favouritecount"));
					return city;
				}

				return null;
			}

		});
	}

	public void addCityToFavouritesForUser(User user, City city) {
		
		String sql = "INSERT INTO favouritecities (user_id, city_id)"
				+ " VALUES (?, ?)";
		jdbcTemplate.update(sql, user.getId(), city.getId());
		return;

	}

	public void removeCityFromFavouritesForUser(User user, City city) {
		String sql = "DELETE FROM favouritecities WHERE user_id=? AND city_id=?;";
		jdbcTemplate.update(sql, user.getId(), city.getId());
		return;

	}

	public void updateCityCounter(City city) {
		String sql = "UPDATE city SET favouritecount=?" + " WHERE id=?";
		jdbcTemplate.update(sql, city.getFavouriteCount(), city.getId());
		return;
	}
	
	private FavouriteCity getFavouriteCity(User user, City city) {
		String sql = "SELECT * FROM favouritecities WHERE user_id=" + user.getId() + " AND city_id=" + city.getId() + ";";
		return jdbcTemplate.query(sql, new ResultSetExtractor<FavouriteCity>() {

			public FavouriteCity extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					FavouriteCity city = new FavouriteCity();
					city.setUserId(rs.getLong("user_id"));
					city.setCityId(rs.getLong("city_id"));
					return city;
				}

				return null;
			}

		});
	}

	public boolean isCityAFavourite(User user, City city) {
		return getFavouriteCity(user, city) != null;
	}

	public List<City> getAllCitiesByDate() {
		String sql = "SELECT * FROM city ORDER BY creationdate;";
		List<City> cities = jdbcTemplate.query(sql, new RowMapper<City>() {

			public City mapRow(ResultSet rs, int rowNum) throws SQLException {
				City city = new City();
				city.setId(rs.getLong("id"));
				city.setName(rs.getString("name"));
				city.setDescription(rs.getString("description"));
				city.setPopulation(rs.getLong("population"));
				city.setCreationDate(rs.getDate("creationdate"));
				city.setFavouriteCount(rs.getLong("favouritecount"));
				return city;
			}

		});

		return cities;
	}

	public List<City> getAllCitiesByFavouriteCount() {
		String sql = "SELECT * FROM city ORDER BY favouritecount;";
		List<City> cities = jdbcTemplate.query(sql, new RowMapper<City>() {

			public City mapRow(ResultSet rs, int rowNum) throws SQLException {
				City city = new City();
				city.setId(rs.getLong("id"));
				city.setName(rs.getString("name"));
				city.setDescription(rs.getString("description"));
				city.setPopulation(rs.getLong("population"));
				city.setCreationDate(rs.getDate("creationdate"));
				city.setFavouriteCount(rs.getLong("favouritecount"));
				return city;
			}

		});

		return cities;
	}

}
