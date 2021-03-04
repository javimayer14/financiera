package com.financial.exchange.market.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.UserDTO;
import com.financial.exchange.market.models.entity.Account;
import com.financial.exchange.market.models.entity.Role;
import com.financial.exchange.market.models.entity.User;

public interface IUserService {

	public Page<User> findAll(Pageable pageable);

	public UserDTO findById(Long id);

	public User findEntityById(Long id);

	public User save(User user);

	public User findByUsername(String username);

	public void delete(Long id);

	public List<Account> findAllAccountUserById(Long id);

	public List<User> findAllActives();

	public List<User> findAllInactives();

	public Account findAccountUserById(Long idUser, Long idAccount);

	public List<User> findByUserParamsMult(List<Role> roles, String username, String name, String surname);

	public Page<UserDTO> findByUserParam(String param, Pageable pageable);

	public OperationDetailDTO findCurrentOperation(Long idUser);

	public void setUserAddresses(User currentUser, User user);

	public User update(User currentUser, User user);

}
