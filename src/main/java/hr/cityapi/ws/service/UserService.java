package hr.cityapi.ws.service;

import hr.cityapi.ws.model.User;

public interface UserService {
	
	public String createUser(User user);
	
	public User getUser(String token);

	public User getUser(User user);

}
