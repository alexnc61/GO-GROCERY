package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import Model.Food;
import Model.Processed;
import Model.Unprocessed;
import db.Connect;

public class Main {
	Scanner scanner = new Scanner(System.in);
	Connect con = Connect.getConnection();
	Vector<Food> listF = new Vector<>();
	Vector<Transaction> listTrans = new Vector<>();
	public void mainMenu() {
		System.out.println("Go-Grocery");
		System.out.println("============");
		System.out.println("1. Buy Grocery");
		System.out.println("2. View Transaction History");
		System.out.println("3. Delete Transaction");
		System.out.println("4. Exit");
	}
	public void loadFood() {
		loadProcessed();
		loadUnprocessed();
	}
	public void loadProcessed() {
		String id,name,date;
		int price;
		String query = "SELECT * FROM `processed food`";
		ResultSet rs = con.executeQuery(query);
		try {
			while(rs.next()) {
				id = rs.getString("FoodId");
				name = rs.getString("FoodName");
				price = rs.getInt("FoodPrice");
				date = rs.getString("ExpiredDate");
				listF.add(new Processed(id, name, price, date));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	public void loadUnprocessed() {	
		String id,name;
		int price,weight;
		String query = "SELECT * FROM `unprocessed food`";
		ResultSet rs = con.executeQuery(query);
		try {
			while(rs.next()) {
				id = rs.getString("FoodId");
				name = rs.getString("FoodName");
				price = rs.getInt("FoodPrice");
				weight = rs.getInt("FoodWeight");
				listF.add(new Unprocessed(id, name, price, weight));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadTrans() {
		String transactionId,groceryId;
		int quantity;
		String query = "SELECT * FROM transaction";
		ResultSet rs = con.executeQuery(query);
		
		try {
			while(rs.next()) {
				transactionId = rs.getString("TransactionId");
				groceryId = rs.getString("GroceryId");
				quantity = rs.getInt("Quantity");
				listTrans.add(new Transaction(transactionId, groceryId, quantity));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void viewProcessed() {

		for (Food food : listF) {
			if(food.getId().startsWith("PF")) {
				System.out.println(food.getId() + " " + food.getName() + " " + "Rp. " + food.getPrice() + " " + ((Processed)food).getDate());				
			}
			
		}
	}
	
	public void viewUnprocessed() {
		for (Food food : listF) {
			if(food.getId().startsWith("UF")) {
				System.out.println(food.getId() + " " + food.getName() + " " + "Rp. " + food.getPrice() + " " + ((Unprocessed)food).getWeight());				
			}
		}
	}
	
	public boolean isValidType(String type) {
		if(type.equals("processed")|| type.equals("unprocessed")) {
			return true;
		}
		return false;
	}
	public void buyGrocery() {
		String type="";
		do {
			System.out.print("Input food type [processed | unprocessed](case sensitive): ");
			type = scanner.nextLine();
		} while (!isValidType(type));
		if(type.equals("processed")) {
			System.out.println(" ID | NAME | PRICE | Expired Date");
			
			viewProcessed();
				
			
			String foodId= "";
		
				System.out.print("Input Food id: ");
				foodId = scanner.nextLine();
				
				int quantity = 0;
				do {
					System.out.print("Input quantity[1-50](inclusive): ");
					quantity = scanner.nextInt();
				} while (quantity < 1 || quantity > 50);
				
				Transaction transaction = new Transaction(genTransId(),foodId,quantity);
				transaction.insert();
				for(Food food : listF) {
					if(foodId.equals(food.getId())) {
						System.out.println("Transaction Detail");
						System.out.println("Food's name: "+ food.getName());
						System.out.println("Food's base price: "+ food.getPrice());
						System.out.println("Quantity : "+ quantity);
						System.out.println("Total Price: "+ food.getPrice()* quantity);
					}
					
					
					
				}
				
	
			
		}else if(type.equals("unprocessed")) {
			System.out.println(" ID | NAME | PRICE | Weight");
			viewUnprocessed();
			
			String foodId= "";
			
			System.out.print("Input Food id: ");
			foodId = scanner.nextLine();
			
			int quantity = 0;
			do {
				System.out.print("Input quantity[1-50](inclusive): ");
				quantity = scanner.nextInt();
			} while (quantity < 1 || quantity > 50);
			
			Transaction transaction = new Transaction(genTransId(),foodId,quantity);
			transaction.insert();
			for(Food food : listF)
				if(foodId.equals(food.getId())) {
			System.out.println("Transaction Detail");
			System.out.println("Food's name: "+ food.getName());
			System.out.println("Food's base price: "+ food.getPrice());
			System.out.println("Quantity : "+ quantity);
			System.out.println("Total Price: "+ food.getPrice()* quantity);
				}
		}

			
		}
	
	
	public String genTransId() {
		Random rand = new Random();
		return "TR" + rand.nextInt(10) + rand.nextInt(10) + rand.nextInt(10);
	}
	
	public void viewTrans() {
		listTrans.clear();
		
		if(!listTrans.isEmpty()) {
			
			System.out.println(" NO | Transaction Id | Grocery Name | Quantity |");
			
				
				for(int i = 0 ; i < listTrans.size(); i++){
					for(Food food:listF) {
					if(listTrans.get(i).getGroceryId().equals(food.getId()))
					System.out.printf(" %d | %s | %s | %d |\n",i+1,listTrans.get(i).getTransactionId(),
							food.getName(),listTrans.get(i).getQuantity());
			}
				
			}	
				listTrans.clear();
		}
		else {
			System.out.println("There are no data");
		}
	}
	public void deleteTrans() {
		if(listTrans.isEmpty()) {
			System.out.println("No transaction yet");
		}
		else {
			viewTrans();
			int input = 0;
			System.out.print("Choose transaction to be deleted[1-"+listTrans.size()+"]: ");
			input = scanner.nextInt();
			input-=1;
			
			scanner.nextLine();
			String query = String.format("DELETE FROM transaction WHERE TransactionId = '%s'", listTrans.get(input).getTransactionId());
			con.executeUpdate(query);
			System.out.println("Successfully deleted");
			
		}
	}
	
	public Main() {
		// TODO Auto-generated constructor stub
		boolean loop = true;
		
		int menu = 0;
		
			do {
				mainMenu();
				loadFood();
				loadTrans();
				do {
					try {
						System.out.print(">> ");
						menu = scanner.nextInt();
					} catch (Exception e) {
						// TODO: handle exception
						menu = -1;
					}
					scanner.nextLine();
					
					switch (menu) {
					case 1:
						buyGrocery();
						break;
					case 2:
						viewTrans();
						break;
					case 3:
						deleteTrans();
						break;
					
					}
				} while (menu < 1 || menu > 4);
				
			} while (loop == true );
		
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
