package com.example.analisis.servicio.imlp;

import com.example.analisis.entidad.Authority;
import com.example.analisis.entidad.User;
import com.example.analisis.enums.EState;
import com.example.analisis.repositories.AuthorityRepository;
import com.example.analisis.repositories.UserRepository;
import com.example.analisis.security.JwtUserFactory;
import com.example.analisis.servicio.UserService;
import com.example.analisis.to.UserPostTO;
import com.example.analisis.to.UserPutTO;
import com.example.analisis.util.IMsmStrings;
import com.example.analisis.util.MapStructMapper;
import com.example.analisis.util.UtilDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService, UserDetailsService  {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UtilDate utilDate;

	@Autowired(required=false)
	private MapStructMapper mapStructMapper;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Override
	public void saveUser(UserPostTO userPostTO) {

		Date currentDate = utilDate.getCurrentDate();
		User user = mapStructMapper.userPostTOToUser(userPostTO);
		user.setPassword(encoder.encode(user.getPassword()));
		user.setLastPasswordResetDate(currentDate);
		user.setDateOfCreation(currentDate);
		user.setDateOfLastEntry(currentDate);
		user.setState(EState.ACTIVE.getId());
		save(user);
	}


	@Override
	public void updateUser(Long id, UserPutTO userPutTO) {
		User user = userRepository.findById(id).get();
		user.setFullName(userPutTO.getFullName());
		user.setUsername(userPutTO.getUsername());
		user.setEmail(userPutTO.getEmail());
		save(user);
	}

	@Override
	public User getUserByUserId(Long id) {
		User userById =  findUserById(id, false);
		return userById;
	}

	@Override
	public User getUserByUsername(String name) {
		User userByName =  userRepository.findByUsername(name);
		return userByName;
	}


	@Override
	public List<User> getUsersByAutority(Long autorityId) {
		List<Authority> authorities  =  new ArrayList<>();

		authorityRepository.findById(autorityId).ifPresent(authorities::add);

		List<User> lUsersByAuthority = userRepository.findByAuthoritiesIn(authorities);
		return filterUserDiferentStateInactiveAndEliminated(lUsersByAuthority);
	}

	@Override
	public void changeStateByUserId(Long id, int newState) {
		User userById =  findUserById(id, true);
		userById.setState(newState);
		save(userById);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException(String.format(IMsmStrings.MSM_NOT_FOUND_NAME, username));
		} else {
			return JwtUserFactory.create(user);
		}
	}

	@Override
	public void updateDateOfLastEntryByUser(String username) {
		User user = userRepository.findByUsername(username);
		Date currentDate = utilDate.getCurrentDate();
		user.setDateOfLastEntry(currentDate);
		save(user);
	}

	@Override
	public List<User> getAllUsers() {
		List<User> lAllUsers = userRepository.findAll();
		return filterUserDiferentStateInactiveAndEliminated(lAllUsers);
	}

	private User findUserById(Long id,boolean isChangeState) {
		User userById =  userRepository.findById(id).get();
		if(!isChangeState) {
			if(EState.ELIMINATED.getId() == userById.getState() || EState.INACTIVE.getId() == userById.getState()) {
				throw new UsernameNotFoundException(String.format(IMsmStrings.MSM_NOT_FOUND_INACTIVE_OR_ELIMINATED, id));
			}
		}
		return userById;
	}

	private void save(User user) {

		userRepository.save(user);
	}

	private List<User> filterUserDiferentStateInactiveAndEliminated(List<User> lUsers) {
		return lUsers.stream().filter(user -> user.getState() != EState.ELIMINATED.getId() && user.getState() != EState.INACTIVE.getId())
				.collect(Collectors.toList());
	}

}
