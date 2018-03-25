package hr.cityapi.ws.dao;

import hr.cityapi.ws.model.User;

public interface UserDao {
	
	public void createUser(User user);
	
	public User getUser(String token);

	public boolean userExists(User user);

	public User getUser(User user);

}
