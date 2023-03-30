package com.example.analisis.controller;

import com.example.analisis.service.UserService;
import com.example.analisis.to.UserCompleteReportTO;
import com.example.analisis.to.UserGetTO;
import com.example.analisis.to.UserPostTO;
import com.example.analisis.to.UserPutTO;
import com.example.analisis.util.MapStructMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(path = "/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

	private static final Logger _logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
	UserService userService;
    
    @Autowired(required=false)
	MapStructMapper mapStructMapper;

	@CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody() UserPostTO userPostTO) {
    	try {
    		userService.saveUser(userPostTO);
    	    return new ResponseEntity<>(HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(), HttpStatus.BAD_REQUEST);
    	}
    }
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody() UserPutTO userPutTO) {
    	try {
    		userService.updateUser(id, userPutTO);
    	    return new ResponseEntity<>(HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
    	try {
    		UserGetTO userById = mapStructMapper.userToUserGetTO(userService.getUserByUserId(id));
    	    return new ResponseEntity<>(userById, HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping(path = "/{id}/complete-report")
    public ResponseEntity<?> getUserByIdForCompleteReport(@PathVariable Long id) {
    	try {
    		UserCompleteReportTO userById = mapStructMapper.userToUserReportTO(userService.getUserByUserId(id));
    	    return new ResponseEntity<>(userById, HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping(path = "/username/{userName}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String userName) {
    	try {
    		UserGetTO userByUsername = mapStructMapper.userToUserGetTO(userService.getUserByUsername(userName));
    	    return new ResponseEntity<>(userByUsername,HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping()
    public ResponseEntity<?> getAllUsers() {
    	try {
    		List<UserGetTO>  lAllUsers = mapStructMapper.usersToUsersGetTOs(userService.getAllUsers());
    	    return new ResponseEntity<>(lAllUsers,HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }
    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping(path = "/autority-id/{autorityId}")
    public ResponseEntity<?> getUsersByAutorityId(@RequestParam(required = true) Long autorityId) {
    	try {
    		List<UserGetTO> usersByAutorityId =mapStructMapper.usersToUsersGetTOs(userService.getUsersByAutority(autorityId));
    	    return new ResponseEntity<>(usersByAutorityId,HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }

    
    @CrossOrigin(origins = "*", maxAge = 3600)
    @PostMapping(path = "/{id}/change-state")
    public ResponseEntity<?> changeStateUserById(@PathVariable Long id, @RequestParam(required = true) int newState) {
    	try {
    		userService.changeStateByUserId(id, newState);
    	    return new ResponseEntity<>(HttpStatus.OK);
    	} catch (Exception ex1) {
    	    _logger.error(ex1.toString());
    	    return new ResponseEntity<>(ex1.toString(),HttpStatus.BAD_REQUEST);
    	}
    }

}
