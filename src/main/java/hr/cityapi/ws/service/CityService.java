package hr.cityapi.ws.service;

import hr.cityapi.ws.model.City;
import hr.cityapi.ws.model.User;

import java.util.List;

public interface CityService {

	public String createCity(City city);
	
	public String addCityToFavorites(User user, City city);
	
	public String deleteCityFromFavourites(User user, City city);
	
	public List<City> listAllCities();
	
	public List<City> listAllCitiesByDate();
	
	public List<City> listAllCitiesByFavouriteCount();
	
}
