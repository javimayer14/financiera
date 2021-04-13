package com.financial.exchange.market.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.UserDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Role;
import com.financial.exchange.market.models.entity.User;
import com.financial.exchange.market.models.service.IUserService;
import com.financial.exchange.market.utils.StringCustomUtils;

@RestController
@RequestMapping("/api")
public class UserRestController {

	public static final Integer ESTADO_ACTIVO = 1;
	public static final Integer ESTADO_INACTIVO = 0;

	@Autowired
	IUserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/users/page/{page}/limit/{limit}")
	public Page<User> index(@PathVariable Integer page, @PathVariable Integer limit) {
		return userService.findAll(PageRequest.of(page, limit));
	}

	@GetMapping("/active-users")
	public List<User> actives(@RequestParam(value = "state", required = false) String state) {
		return userService.findAllInactives();
	}

	@GetMapping("/users/{id}")
	public UserDTO show(@PathVariable Long id) {
		return userService.findById(id);
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/users")
	public User create(@RequestBody User user) {
		if (user.getPassword() == null || "".equals(user.getPassword()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario debe tener una contraseña");
		String passwordBcrypt = passwordEncoder.encode(user.getPassword());
		user.setPassword(passwordBcrypt);
		return userService.save(user);
	}

	// Consultar sobre este EP
	@PostMapping("/users/search")
	public List<User> findMultipleVar(@RequestBody User user) {
		String username = StringCustomUtils.trimString(user.getUsername());
		String name = StringCustomUtils.trimString(user.getName());
		String surname = StringCustomUtils.trimString(user.getSurname());
		Integer state = user.getState();
		List<Role> roles = user.getRoles();
		return userService.findByUserParamsMult(roles, username, name, surname, state);
	}

	@GetMapping("/users/filter/page/{page}/limit/{limit}")
	public Page<UserDTO> find(@RequestParam(value = "var", required = false) String var, @PathVariable Integer page, @PathVariable Integer limit) {
		return userService.findByUserParam(StringCustomUtils.trimString(var), PageRequest.of(page, limit));
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/users/{id}")
	public User update(@RequestBody User user, @PathVariable Long id) throws Exception {
		User currentUser = userService.findEntityById(id);
		if (currentUser == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el usuario con id " + id);

		if (user.getPassword() != null && !"".equals(user.getPassword())) {
			String passwordBcrypt = passwordEncoder.encode(user.getPassword());
			currentUser.setPassword(passwordBcrypt);
		}

		return userService.update(currentUser, user);

	}

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("users-state/{id}")
	public void destroy(@RequestBody User user, @PathVariable Long id) {
		User currentUser = userService.findEntityById(id);

		if (currentUser == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra el usuario con id " + id);
		}

		if (user.getState() == ESTADO_INACTIVO)
			currentUser.setState(ESTADO_INACTIVO);
		if (user.getState() == ESTADO_ACTIVO)
			currentUser.setState(ESTADO_ACTIVO);
		userService.save(currentUser);
	}

	@GetMapping("/users/{idUser}/accounts")
	public List<Account> allUserAccount(@PathVariable Long idUser) {
		return userService.findAllAccountUserById(idUser);
	}

	@GetMapping("/users/{idUser}/accounts/{idAcount}")
	public Account userAccount(@PathVariable Long idUser, @PathVariable Long idAcount) {
		return userService.findAccountUserById(idUser, idAcount);
	}

	@GetMapping("/users/{idUser}/operations")
	public OperationDetailDTO currentOperation(@PathVariable Long idUser) {
		return userService.findCurrentOperation(idUser);
	}

}
