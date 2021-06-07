package ca.sheridancollege.doricha.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.doricha.beans.Bike;
import ca.sheridancollege.doricha.beans.Manufacturer;
import ca.sheridancollege.doricha.database.DatabaseAccess;

@Controller
public class HomeController {
	
	@Autowired
	DatabaseAccess da;

	// mapping to index (localhost:8080)
	@GetMapping("/")
	public String index(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		model.addAttribute("email", email);
		
		return "index";
	}
	
	// mapping to insert page
	@GetMapping("/insert")
	public String insert(Model model) {
		
		model.addAttribute("bike", new Bike());
		model.addAttribute("manufacturer", new Manufacturer());
		model.addAttribute("manufacturerList", da.getManufacturers());
		
		return "insert";
	}
	
	// handles post request for insertBike
	@PostMapping("/insertBike")
	public String insertBike(Model model, @ModelAttribute Bike bike) {
		
		// add bike to db
		da.insertBike(bike);
		
		model.addAttribute("bike", new Bike());
		model.addAttribute("manufacturer", new Manufacturer());
		model.addAttribute("manufacturerList", da.getManufacturers());
		
		return "insert";
	}
	
	
	// mapping to update page
	@GetMapping("/update")
	public String update() {
		return "update";
	}
	
	// mapping to updateBike
	@PostMapping("/updateBike")
	public String updateBike(Model model, @RequestParam double price, @RequestParam String modelName) {
		
		List<Bike> bikeList = da.getBikeByModel(modelName);
		
		// ensure bike model exists prior to updating
		if (!bikeList.isEmpty()) {
			Bike bike = bikeList.get(0);
			bike.setPrice(price);
			
			// delete old bike entry
			da.deleteBikeById(bike.getBikeID());
			
			// update to new bike
			da.insertBike(bike);
		}
		
		return "update";
	}
	
	// mapping to delete page
	@GetMapping("/secure/delete")
	public String delete(Model model, Authentication authentication) {
		
		model.addAttribute("bikeList", da.getBikes());
		return "/secure/delete";
	}
	
	// handles deleteBike
	@PostMapping("/secure/deleteBike")
	public String deleteBike(Model model, @RequestParam(required = false) Long bikeID){
		
		// if bike exists, delete it
		if (bikeID != null)
			da.deleteBikeById(bikeID);
		
		model.addAttribute("bikeList", da.getBikes());
		return "/secure/delete";
	}
	
	// Mapping for login page
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	// Mapping for register page
	@GetMapping("/register")
	public String getRegister() {
		return "register";
	}
	
	// Handles registration of users.
	// By default users will get USER role.
	@PostMapping("/register")
	public String postRegister(@RequestParam String username, @RequestParam String password) {
		da.addUser(username, password);
		Long userId = da.findUserAccount(username).getUserId();
		da.addRole(userId, Long.valueOf(1)); // admin role
		da.addRole(userId, Long.valueOf(2)); // user role
		return "login";
	}
	
	// permission-denied mapping
	@GetMapping("/permission-denied")
	public String permissionDenied() {
		return "/error/permission-denied";
	}
}
