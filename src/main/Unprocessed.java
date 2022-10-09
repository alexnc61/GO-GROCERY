package main;

public class Unprocessed extends Food{
	private int weight;
	
	public Unprocessed(String id, String name, int price, int weight) {
		super(id, name, price);
		this.weight = weight;
	}
public void display() {
		
		System.out.println(id + " | "+name + " | "+ price + " | " + weight);
	}
	
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	

}
