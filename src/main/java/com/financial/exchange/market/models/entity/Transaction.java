package com.financial.exchange.market.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Data
public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "time_range")
	private Date timeRange;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_type_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private TransactionType transactionType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_status_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private TransactionStatus transactionStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operation_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Operation operation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Account account;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commission_type_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private CommissionType commissionType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commission_id")
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	private Commission commission;

	private String commision;

	private Float amount;
	private String notes;
	private String other;

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

	private Integer state;

}
