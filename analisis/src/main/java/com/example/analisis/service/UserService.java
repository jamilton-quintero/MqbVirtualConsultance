package com.example.analisis.service;


import com.example.analisis.domain.entity.User;
import com.example.analisis.to.UserPostTO;
import com.example.analisis.to.UserPutTO;

import java.util.List;

public interface UserService {
	
	void saveUser(UserPostTO userPostTO);
	
	void updateUser(Long id, UserPutTO userPutTO);
	
	User getUserByUserId(Long id);
	
	User getUserByUsername(String name);
	
	List<User> getUsersByAutority(Long autorityId);
	
	void changeStateByUserId(Long id,int newState);
	
	void updateDateOfLastEntryByUser(String username);
	
	List<User> getAllUsers();

}
