package ca.sheridancollege.doricha.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.doricha.beans.Bike;
import ca.sheridancollege.doricha.beans.Manufacturer;
import ca.sheridancollege.doricha.beans.User;


@Repository
public class DatabaseAccess {

	@Autowired
	protected NamedParameterJdbcTemplate jdbc;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	// insert a bike entry into bike table
	public void insertBike(Bike bike) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "INSERT INTO bike(manufacturerID, model, year, color, price) " +
				"VALUES (:manufacturerID, :model, :year, :color, :price)";
		namedParameters.addValue("manufacturerID", bike.getManufacturerID());
		namedParameters.addValue("model", bike.getModel());
		namedParameters.addValue("year", bike.getYear());
		namedParameters.addValue("color", bike.getColor());
		namedParameters.addValue("price", bike.getPrice());
		int rowsAffected = jdbc.update(query,  namedParameters);
		if (rowsAffected > 0)
			System.out.println("Inserted bike into database.");
		
	}
	
	// return list of manufacturers
	public List<Manufacturer> getManufacturers(){
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM manufacturer";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Manufacturer>(Manufacturer.class));
	}

	// return bike based on model
	public List<Bike> getBikeByModel(String model) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bike WHERE model = :model";
		namedParameters.addValue("model", model);
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Bike>(Bike.class));
	}

	// return list of all bikes
	public List<Bike> getBikes() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM bike";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Bike>(Bike.class));
	}

	// delete bike using bikeID from bike table
	public void deleteBikeById(long bikeID) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "DELETE FROM bike WHERE bikeID = :bikeID";
		namedParameters.addValue("bikeID", bikeID);
		int rowsAffected = jdbc.update(query,  namedParameters);
		if (rowsAffected > 0)
			System.out.println("Deleted bikeID " + bikeID + " from database");
		
	}
	
	// find user account
	public User findUserAccount(String email) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		
		String query = "SELECT * FROM sec_user where email=:email";
		parameters.addValue("email", email);
		
		ArrayList<User> users = (ArrayList<User>)jdbc.query(query,  parameters,
				new BeanPropertyRowMapper<User>(User.class));
		
		if (users.size() > 0) return users.get(0);
		else return null;
	}
	
	// find user role by id
	public List<String> getRolesById(Long userId){
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "select user_role.userId, sec_role.roleName "
				+ "FROM user_role, sec_role "
				+ "WHERE user_role.roleId=sec_role.roleId "
				+ "AND userId=:userId";
		
		parameters.addValue("userId", userId);
		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);
		
		for (Map<String, Object> row : rows) {
			roles.add((String)row.get("roleName"));
		}
		
		return roles;
	}
	
	// add user to database
	public void addUser(String email, String password) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO sec_user "
				+ "(email, encryptedPassword, enabled)"
				+ " values (:email, :encryptedPassword, 1)";
		parameters.addValue("email", email);
		parameters.addValue("encryptedPassword", passwordEncoder.encode(password));
		jdbc.update(query, parameters);
	}
	
	// adds role to user
	public void addRole(Long userId, Long roleId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "INSERT INTO user_role (userId, roleId) "
				+ "VALUES (:userId, :roleId)";
		parameters.addValue("userId", userId);
		parameters.addValue("roleId", roleId);
		jdbc.update(query, parameters);
	}
}
