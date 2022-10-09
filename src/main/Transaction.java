package main;

import db.Connect;

public class Transaction {
	private String transactionId;
	private String groceryId;
	private int quantity;
	
	public Transaction(String transactionId, String groceryId, int quantity) {
		super();
		this.transactionId = transactionId;
		this.groceryId = groceryId;
		this.quantity = quantity;
	}
	public void insert() {
		Connect con = Connect.getConnection();
		String query = String.format("INSERT INTO transaction VALUES('%s', '%s', '%d')", transactionId,groceryId,quantity);
		con.executeUpdate(query);
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getGroceryId() {
		return groceryId;
	}
	public void setGroceryId(String groceryId) {
		this.groceryId = groceryId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
