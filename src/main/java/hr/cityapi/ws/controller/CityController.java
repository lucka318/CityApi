package hr.cityapi.ws.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import hr.cityapi.ws.model.City;
import hr.cityapi.ws.model.User;
import hr.cityapi.ws.service.CityService;
import hr.cityapi.ws.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityController {

	@Autowired
	private CityService cityService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/cities", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<City>> listCities() {

		HttpHeaders headers = new HttpHeaders();
		List<City> cities = cityService.listAllCitiesByDate();

		if (cities == null) {
			return new ResponseEntity<List<City>>(HttpStatus.NOT_FOUND);
		}
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		return new ResponseEntity<List<City>>(cities, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/cities", method = RequestMethod.GET, params = { "sort" }, produces = "application/json")
	public ResponseEntity<List<City>> listCities(HttpServletResponse response,
			@RequestParam("sort") String sort) {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

		List<City> cities = null;
		if (sort != null && sort.equals("count")) {
			cities = cityService.listAllCitiesByFavouriteCount();
		} else {
			return new ResponseEntity<List<City>>(headers,
					HttpStatus.BAD_REQUEST);
		}

		if (cities == null) {
			return new ResponseEntity<List<City>>(HttpStatus.NOT_FOUND);
		}
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
		return new ResponseEntity<List<City>>(cities, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<User> createUser(@RequestBody User user) {

		String error = "";
		HttpHeaders headers = new HttpHeaders();

		if (user == null || user.getEmail().isEmpty()
				|| user.getPassword().isEmpty()) {
			error = "Creation of a new user failed";
			headers.add("error", error);
			return new ResponseEntity<User>(headers, HttpStatus.NOT_ACCEPTABLE);
		}

		error = userService.createUser(user);
		if(error.isEmpty()) {
			user.setPassword(""); // we should hash password
			headers.add("User created: ", String.valueOf(user.getEmail()));
			return new ResponseEntity<User>(user, headers, HttpStatus.CREATED);
		} else {
			headers.add("error", error);
			return new ResponseEntity<User>(headers,
					HttpStatus.NOT_ACCEPTABLE);
		}
		

	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<User> loginUser(@RequestBody User user) {

		String error = "";
		HttpHeaders headers = new HttpHeaders();

		if (user == null || user.getEmail() == null || user.getEmail().isEmpty()
				|| user.getPassword() == null || user.getPassword().isEmpty()) {
			error = "Login failed";
			headers.add("error", error);
			return new ResponseEntity<User>(headers, HttpStatus.NOT_ACCEPTABLE);
		}

		user = userService.getUser(user);
		if(user == null) {
			error = "Login failed";
			headers.add("error", error);
			return new ResponseEntity<User>(headers, HttpStatus.NOT_ACCEPTABLE);
		} else {
			user.setPassword(""); // we should hash password
			headers.add("User login: ", String.valueOf(user.getEmail()));
			return new ResponseEntity<User>(user, headers, HttpStatus.ACCEPTED);
		}
	}

	@RequestMapping(value = "/city", method = RequestMethod.POST, params = { "token" }, produces = "application/json")
	public ResponseEntity<City> createCity(@RequestBody City city,
			@RequestParam("token") String token) {

		String error = "";
		HttpHeaders headers = new HttpHeaders();

		User user = userService.getUser(token);
		if (user == null) {
			error = "Authentication failed";
			headers.add("error", error);
			return new ResponseEntity<City>(city, headers, HttpStatus.FORBIDDEN);
		}

		if (checkCity(city)) {
			return new ResponseEntity<City>(HttpStatus.BAD_REQUEST);
		}

		error = cityService.createCity(city);
		if (!error.isEmpty()) {
			headers.add("error", error);
			return new ResponseEntity<City>(city, headers,
					HttpStatus.NOT_ACCEPTABLE);
		}
		headers.add("City created: ", String.valueOf(city.getName()));
		return new ResponseEntity<City>(city, headers, HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/addToFavourites", method = RequestMethod.POST, params = {"city", "token" }, produces = "application/json")
	public ResponseEntity<City> addToFavourites(@RequestParam("city") String cityName,
			@RequestParam("token") String token) {

		String error = "";
		HttpHeaders headers = new HttpHeaders();

		User user = userService.getUser(token);
		if (user == null) {
			error = "Authentication failed";
			headers.add("error", error);
			return new ResponseEntity<City>(headers, HttpStatus.FORBIDDEN);
		}

		City recievedCity = new City();
		recievedCity.setName(cityName);
		error = cityService.addCityToFavorites(user, recievedCity);

		if (!error.isEmpty()) {
			headers.add("error", error);
			return new ResponseEntity<City>(recievedCity, headers,
					HttpStatus.NOT_ACCEPTABLE);
		}
		headers.add("City added: ", String.valueOf(recievedCity.getName()));
		return new ResponseEntity<City>(headers, HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/removeFromFavourites", method = RequestMethod.POST, params = {"city", "token" }, produces = "application/json")
	public ResponseEntity<City> removeFromFavourites(@RequestParam("city") String cityName,
			@RequestParam("token") String token) {

		String error = "";
		HttpHeaders headers = new HttpHeaders();

		User user = userService.getUser(token);
		if (user == null) {
			error = "Authentication failed";
			headers.add("error", error);
			return new ResponseEntity<City>(headers, HttpStatus.FORBIDDEN);
		}

		City recievedCity = new City();
		recievedCity.setName(cityName);
		error = cityService.deleteCityFromFavourites(user, recievedCity);

		if (!error.isEmpty()) {
			headers.add("error", error);
			return new ResponseEntity<City>(recievedCity, headers,
					HttpStatus.NOT_ACCEPTABLE);
		}
		headers.add("City removed: ", String.valueOf(recievedCity.getName()));
		return new ResponseEntity<City>(headers, HttpStatus.CREATED);

	}

	private boolean checkCity(City city) {
		if (city == null || city.getName() == null
				|| city.getDescription() == null || city.getName().isEmpty()
				|| city.getDescription().isEmpty()
				|| city.getPopulation() == null) {
			return true;
		}
		return false;
	}

}
