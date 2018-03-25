package hr.cityapi.ws.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import hr.cityapi.ws.dao.UserDao;
import hr.cityapi.ws.model.City;
import hr.cityapi.ws.model.User;

@Repository
public class UserDaoImpl implements UserDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void createUser(User user) {

		String sql = "INSERT INTO user_ (email, password, token)"
				+ " VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, user.getEmail(), user.getPassword(),
				user.getToken());

		return;

	}

	public User getUser(String token) {
		String sql = "SELECT * FROM user_ WHERE token='" + token + "';";
		return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {

			public User extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					User user = new User();
					user.setId(rs.getLong("id"));
					user.setEmail(rs.getString("email"));
					user.setToken(rs.getString("token"));

					return user;
				}

				return null;
			}

		});
	}

	public boolean userExists(User user) {

		return getUser(user) != null;
	}

	public User getUser(User user) {
		String sql = "SELECT * FROM user_ WHERE email='" + user.getEmail()
				+ "' AND password='" + user.getPassword() + "';";
		return jdbcTemplate.query(sql, new ResultSetExtractor<User>() {

			public User extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				if (rs.next()) {
					User user = new User();
					user.setId(rs.getLong("id"));
					user.setEmail(rs.getString("email"));
					user.setToken(rs.getString("token"));

					return user;
				}

				return null;
			}

		});
	}

}
