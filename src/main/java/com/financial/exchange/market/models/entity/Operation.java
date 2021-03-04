package com.financial.exchange.market.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Operation implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operation_type_id")
	private OperationType operationType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "operation_status_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private OperationStatus operationStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "broker_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User broker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "enterprise_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private User enterprise;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Address address;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "origin_currency")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Currency originCurrency;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "destination_currency")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Currency destinationCurrency;

	@JsonIgnore
	@OneToMany(mappedBy = "operation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transaction> transactions;

	@Column(name = "time_range")
	@Temporal(TemporalType.DATE)
	private Date timeRange;

	@Column(name = "origin_amount")
	private Float originAmount;

	@Column(name = "destination_amount")
	private Float destinationAmount;

	@Column(name = "convertion_rate")
	private Float convertionRate;

	private String description;
	private String notes;

	@Column(name = "time_since")
	private Date timeSince;

	@Column(name = "time_until")
	private Date timeUntil;

	private Integer state;

	private Integer priority;

	@OneToOne(mappedBy = "operation", orphanRemoval = true, cascade = { CascadeType.ALL })
	private Expense expense;

	@Column(name = "created_date", updatable = false)
	@CreatedDate
	private Date createdDate;

	@Column(name = "modified_date")
	@LastModifiedDate
	private Date modifiedDate;

	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;

	@Column(name = "modified_by")
	@LastModifiedBy
	private String modifiedBy;

	@Column
	private Integer alert;

	@ManyToOne
	@JoinColumn(name = "origin_client_id")
	private Client originClient;

	@ManyToOne
	@JoinColumn(name = "destination_client_id")
	private Client destinationClient;
	
}
