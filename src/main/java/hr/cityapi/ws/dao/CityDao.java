package hr.cityapi.ws.dao;

import hr.cityapi.ws.model.City;
import hr.cityapi.ws.model.User;

import java.util.List;

public interface CityDao {
	
	public City getCity(String name);
	
	public List<City> getAllCities();
	
	public City createCity(City city);
	
	public City updateCity(City city);

	public boolean cityExists(City city);

	public void addCityToFavouritesForUser(User user, City city);

	public void updateCityCounter(City city);

	public void removeCityFromFavouritesForUser(User user, City city);

	public boolean isCityAFavourite(User user, City city);

	public List<City> getAllCitiesByDate();

	public List<City> getAllCitiesByFavouriteCount();
}
