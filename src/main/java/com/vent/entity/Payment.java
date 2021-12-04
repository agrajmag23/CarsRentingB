package com.vent.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Payment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int pid;
	
	private Double amount;
	private String payment_type;
	private String payment_info;
	private String pstatus;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getPayment_info() {
		return payment_info;
	}
	public void setPayment_info(String payment_info) {
		this.payment_info = payment_info;
	}
	public String getPstatus() {
		return pstatus;
	}
	public void setPstatus(String pstatus) {
		this.pstatus = pstatus;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((payment_info == null) ? 0 : payment_info.hashCode());
		result = prime * result + ((payment_type == null) ? 0 : payment_type.hashCode());
		result = prime * result + pid;
		result = prime * result + ((pstatus == null) ? 0 : pstatus.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (payment_info == null) {
			if (other.payment_info != null)
				return false;
		} else if (!payment_info.equals(other.payment_info))
			return false;
		if (payment_type == null) {
			if (other.payment_type != null)
				return false;
		} else if (!payment_type.equals(other.payment_type))
			return false;
		if (pid != other.pid)
			return false;
		if (pstatus == null) {
			if (other.pstatus != null)
				return false;
		} else if (!pstatus.equals(other.pstatus))
			return false;
		return true;
	}
	
	
	
}
