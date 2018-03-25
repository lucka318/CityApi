package hr.cityapi.ws.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.cityapi.ws.dao.UserDao;
import hr.cityapi.ws.model.User;
import hr.cityapi.ws.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public String createUser(User user) {
		String error = "";
		if (user != null) {
			boolean check = userDao.userExists(user);
			if (!check) {
				String token = UUID.randomUUID().toString().toUpperCase() + System.currentTimeMillis();
				user.setToken(token);
				userDao.createUser(user);
			} else {
				error = "User exists";
			}
		}
		return error;
	}

	public User getUser(String token) {
		return userDao.getUser(token);
	}

	public User getUser(User user) {
		if (user != null) {
			user = userDao.getUser(user);
		}
		return user;
	}

}
