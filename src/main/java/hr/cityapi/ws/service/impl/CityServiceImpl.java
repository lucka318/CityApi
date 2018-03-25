package hr.cityapi.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.cityapi.ws.dao.CityDao;
import hr.cityapi.ws.dao.UserDao;
import hr.cityapi.ws.model.City;
import hr.cityapi.ws.model.User;
import hr.cityapi.ws.service.CityService;

@Service
public class CityServiceImpl implements CityService {

	@Autowired
	private CityDao cityDao;

	@Autowired
	private UserDao userDao;

	public String createCity(City city) {
		String error = "";
		if (city != null) {
			boolean check = cityDao.cityExists(city);
			if (!check) {
				city = cityDao.createCity(city);
			} else {
				error = "City already exists in database";
			}
		}
		return error;

	}

	public String addCityToFavorites(User user, City city) {
		String error = "";
		boolean check = cityDao.cityExists(city);
		if (city != null && check) {
			city = cityDao.getCity(city.getName());
			boolean exists = cityDao.isCityAFavourite(user, city);
			if (!exists) {
				city = cityDao.getCity(city.getName());
				city.setFavouriteCount(city.getFavouriteCount() + 1);
				cityDao.addCityToFavouritesForUser(user, city);
				cityDao.updateCityCounter(city);
			} else {
				error = "City doesn't exist.";
			}
		}
		return error;
	}

	public String deleteCityFromFavourites(User user, City city) {
		String error = "";
		boolean check = cityDao.cityExists(city);
		if (city != null && check) {
			city = cityDao.getCity(city.getName());
			boolean exists = cityDao.isCityAFavourite(user, city);
			if (exists) {
				city.setFavouriteCount(city.getFavouriteCount() - 1);
				cityDao.updateCityCounter(city);
				cityDao.removeCityFromFavouritesForUser(user, city);
			} else {
				error = "The city was not a favourite.";
			}
		}
		return error;
	}

	public List<City> listAllCities() {
		List<City> cities = cityDao.getAllCities();
		return cities;

	}

	public List<City> listAllCitiesByDate() {
		List<City> cities = cityDao.getAllCitiesByDate();
		return cities;
	}

	public List<City> listAllCitiesByFavouriteCount() {
		List<City> cities = cityDao.getAllCitiesByFavouriteCount();
		return cities;
	}

}
