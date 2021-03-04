package com.financial.exchange.market.models.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.financial.exchange.market.models.dto.AddressDTO;
import com.financial.exchange.market.models.dto.AmountDto;
import com.financial.exchange.market.models.dto.ClientDTO;
import com.financial.exchange.market.models.dto.OperationDetailDTO;
import com.financial.exchange.market.models.dto.PersonDTO;
import com.financial.exchange.market.models.dto.TradingOperationDetailDTO;
import com.financial.exchange.market.models.dto.ExpenseOperationDTO;
import com.financial.exchange.market.models.dto.ExpenseTypeDTO;
import com.financial.exchange.market.models.dto.UserDTO;
import com.financial.exchange.market.models.dto.WorkingDayDTO;
import com.financial.exchange.market.models.dto.WorkingDayOperationDetailDTO;
import com.financial.exchange.market.models.entity.Address;
import com.financial.exchange.market.models.entity.Client;
import com.financial.exchange.market.models.entity.Expense;
import com.financial.exchange.market.models.entity.ExpenseType;
import com.financial.exchange.market.models.entity.Operation;
import com.financial.exchange.market.models.entity.Transaction;
import com.financial.exchange.market.models.entity.User;

@Component
public class MapperService {

	@Autowired
	private ModelMapper mapper;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public AddressDTO mapAddressToDTO(Address address) {
		return mapper.map(address, AddressDTO.class);

	}

	public ClientDTO mapClientToDTO(Client client) {
		return mapper.map(client, ClientDTO.class);
	}

	public OperationDetailDTO mapOperationToDTO(Operation operation) {

		// mapper.typeMap(Transaction.class, AmountDto.class)
		// .addMapping(src -> src.getAccount().getCurrency().getId(),
		// AmountDto::setCurrencyId);

		mapper.typeMap(Operation.class, OperationDetailDTO.class).addMappings(mapper -> {
			mapper.using(ctx -> new ArrayList<>()).map(Operation::getTransactions, OperationDetailDTO::setAmounts);
		});

		OperationDetailDTO dto = mapper.map(operation, OperationDetailDTO.class);
		dto.setAmounts(mapTransactionsToAmountDTOs(operation.getTransactions()));
		return dto;
	}

	private List<AmountDto> mapTransactionsToAmountDTOs(List<Transaction> transactions) {
		List<AmountDto> amounts = new ArrayList<>();
		for (Transaction t : transactions) {
			if (t.getTransactionType().getId() == ETransactionType.ENTRY.getValue()) {
				AmountDto amount = new AmountDto();
				amount.setAmount(t.getAmount());
				amount.setCurrencyId(t.getAccount().getCurrency().getId());
				amounts.add(amount);
			}
		}
		return amounts;
	}

	public UserDTO mapUserToDTO(User user) {
		return mapper.map(user, UserDTO.class);
	}

	public OperationDetailDTO mapGenericOperationToDTO(Operation operation) {
		return mapper.map(operation, OperationDetailDTO.class);
	}

	public WorkingDayOperationDetailDTO mapWorkingDayOperationDetailToDTO(Operation operation) {
		return mapper.map(operation, WorkingDayOperationDetailDTO.class);
	}

	public WorkingDayDTO mapWorkingDayToDTO(Operation operation) {
		List<Transaction> transactions = operation.getTransactions();

		List<AmountDto> amountDtos = mapTransactionsToAmountDTOs(transactions);

		WorkingDayDTO workingDayDTO = mapper.map(operation, WorkingDayDTO.class);

		workingDayDTO.setAmounts(amountDtos);

		return workingDayDTO;
	}

	public TradingOperationDetailDTO mapTradingOperationToDTO(Operation operation) {
		return mapper.map(operation, TradingOperationDetailDTO.class);
	}

	public ExpenseTypeDTO mapExpenseTypeToDTO(ExpenseType expenseType) {
		ExpenseTypeDTO expenseTypeDTO = mapper.map(expenseType, ExpenseTypeDTO.class);

		return expenseTypeDTO;
	}

	public ExpenseOperationDTO mapExpenseOperationToDTO(Expense expense) {

		ExpenseOperationDTO expenseDTO = mapper.map(expense, ExpenseOperationDTO.class);
		ExpenseTypeDTO expenseTypeDTO = mapper.map(expense.getExpenseType(), ExpenseTypeDTO.class);
		expenseDTO.setExpenseType(expenseTypeDTO);

		return expenseDTO;
	}

	public PersonDTO mapUserToPersonDTO(User user) {
		PersonDTO person = new PersonDTO();
		if (user != null && user.getId() != null) {
			person.setId(user.getId());
			person.setUserName(user.getUsername());
			person.setFirstName(user.getName());
			person.setLastName(user.getSurname());
		}
		return person;
	}

	public PersonDTO mapClientToPersonDTO(Client client) {
		PersonDTO person = new PersonDTO();
		if (client != null && client.getId() != null) {
			person.setId(client.getId());
			// person.setUserName(client.getUsername());
			person.setFirstName(client.getFirstName());
			person.setLastName(client.getLastName());
		}
		return person;
	}
}
