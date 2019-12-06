package edu.emmerson.camel.bill.camel_bill_compute_discount;

public class Bill {
	
	private double amount;
	private String customerType;
	private double amountAfterDiscount;
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public double getAmountAfterDiscount() {
		return amountAfterDiscount;
	}
	public void setAmountAfterDiscount(double amountAfterDiscount) {
		this.amountAfterDiscount = amountAfterDiscount;
	}
	
}