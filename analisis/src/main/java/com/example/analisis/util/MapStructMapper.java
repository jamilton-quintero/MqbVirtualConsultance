package com.example.analisis.util;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.example.analisis.entidad.User;
import com.example.analisis.to.UserCompleteReportTO;
import com.example.analisis.to.UserGetTO;
import com.example.analisis.to.UserPostTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
	
	MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

	UserGetTO userToUserGetTO(User user);
	
	User userGetTOToUser(UserGetTO userGetTO);
	
	UserPostTO userToUserPostTO(User user);
	
	User userPostTOToUser(UserPostTO userGetTO);
	
	UserCompleteReportTO userToUserReportTO(User user);
	
	User userReportTOToUser(UserCompleteReportTO userGetTO);
	
	List<UserGetTO> usersToUsersGetTOs(List<User> users);
	
	User userDetailToUser (UserDetails userDetails);


}
