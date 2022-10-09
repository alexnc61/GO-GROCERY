package main;

public class Processed extends Food {
	private String date;
	
	public Processed(String id, String name, int price, String date) {
		super(id, name, price);
		this.date = date;
	}
	
	public void display() {
		
		System.out.println(id + " | "+name + " | "+ price + " | " + date);
	}
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
