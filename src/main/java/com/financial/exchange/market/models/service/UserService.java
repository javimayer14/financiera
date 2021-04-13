package com.financial.exchange.market.models.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.financial.exchange.market.models.dao.IAccountDao;
import com.financial.exchange.market.models.dao.IUserDao;
import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.UserDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Address;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.Role;
import com.financial.exchange.market.models.entity.User;

@Service
public class UserService implements UserDetailsService, IUserService {

	private Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IAccountDao accountDao;

	@Autowired
	private MapperService mapperService;

	private static final String DELIVERY_NAME = "ROLE_DELIVERY";
	private static final Long DELIVERY_ID = 5l;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUsername(username);

		if (user == null) {
			logger.error("eerror no existe el usuario '" + username + " en el sistema !'");
			throw new UsernameNotFoundException("Error en el login: no existe el usuario:'" + username + "'");
		}

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.peek(authority -> logger.info("Role" + authority.getAuthority())).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return userDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAllActives() {
		List<User> users = (List<User>) userDao.findAllActives();
		return users;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAllInactives() {
		List<User> users = (List<User>) userDao.findAllInactives();
		return users;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		User user = userDao.findById(id).orElse(null);
		return mapperService.mapUserToDTO(user);
	}

	@Override
	@Transactional(readOnly = true)
	public User findEntityById(Long id) {
		return userDao.findById(id).orElse(null);
	}

	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findByUserParamsMult(List<Role> roles, String username, String name, String surname, Integer state) {
		List<User> result = userDao.findUsersbyMultipleVar(roles, username, name, surname, state);
		for (User u : result) {
			List<Account> onlyCash = new ArrayList<>();
			if (isDelivery(u.getRoles()) && isDelivery(roles)) {
				for (Account a : u.getAccounts()) {
					if (a.getAccountType().getId() == EAccountType.CASH.getState())
						onlyCash.add(a);
				}
				u.setAccounts(onlyCash);
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserDTO> findByUserParam(String param, Pageable pageable) {
		Page<User> users = userDao.findUsersbyVar(param, pageable);
		Page<UserDTO> dtos = users.map(this::mapUserToDto);
		return dtos;
	}

	private UserDTO mapUserToDto(User user) {
		return mapperService.modelMapper().map(user, UserDTO.class);
	}

	private List<UserDTO> mapUsersToDTO(List<User> users) {
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		for (User o : users) {
			userDTOs.add(mapperService.mapUserToDTO(o));
		}
		return userDTOs;
	}

	@Override
	@Transactional
	public User save(User user) {
		List<Address> newAddresses = user.getAddresses();
		for (Address a : newAddresses) {
			a.setUser(user);
		}
		return userDao.save(user);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		userDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Account> findAllAccountUserById(Long id) {
		User currentUser = userDao.findById(id).orElse(null);
		if (currentUser == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el usuario con id " + id);
		List<Account> accountList = accountDao.findAllAccountUserById(id);
		return accountList;
	}

	private Boolean isDelivery(List<Role> roles) {
		Boolean isDelivery = false;
		for (Role r : roles) {
			if (DELIVERY_NAME.equals(r.getName()) || r.getId() == DELIVERY_ID) {
				isDelivery = true;
				return isDelivery;
			}
		}
		return isDelivery;
	}

	@Override
	public Account findAccountUserById(Long idUser, Long idAccount) {
		User currentUser = userDao.findById(idUser).orElse(null);
		List<Account> accountList = accountDao.findAllAccountUserById(idUser);
		Account currentAccount = accountDao.findById(idAccount).orElse(null);
		if (currentUser == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el usuario con id " + idUser);
		if (!accountList.contains(currentAccount))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No existe una cuenta con id " + idAccount + " para el usuario con id " + idUser);
		return currentAccount;
	}

	@Override
	public OperationDetailDTO findCurrentOperation(Long idUser) {
		List<Operation> currentsOperations = userDao.findCurrentOperation(idUser);
		Operation highPriorityOperation = new Operation();
		if (!currentsOperations.isEmpty())
			highPriorityOperation = currentsOperations.get(0);
		else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"El broker con id: " + idUser + " no tiene una operacion asignada ");
		return mapperService.mapOperationToDTO(highPriorityOperation);
	}

	public User update(User currentUser, User user) {
		setUserAddresses(currentUser, user);
		currentUser.setDni(user.getDni());
		currentUser.setEmail(user.getEmail());
		currentUser.setState(user.getState());
		currentUser.setName(user.getName());
		currentUser.setSurname(user.getSurname());
		currentUser.setUsername(user.getUsername());
		currentUser.setRoles(user.getRoles());
		currentUser.setNotes(user.getNotes());

		return userDao.save(currentUser);
	}

	public void setUserAddresses(User currentUser, User user) {
		List<Address> newAddresses = user.getAddresses();
		for (Address a : newAddresses) {
			a.setUser(currentUser);
		}
		if (!newAddresses.isEmpty() && newAddresses != null) {
			if (currentUser.getAddresses() != null) {
				currentUser.getAddresses().clear();
				currentUser.getAddresses().addAll(newAddresses);
			} else {
				currentUser.setAddresses(newAddresses);
			}
		}
	}

}
