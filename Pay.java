package sw.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
	
public class Pay {
	private Db db;
	private Scanner sc;
	private HashMap<String, ArrayList<Product>> products;
	private HashMap<Integer, Integer> cash;
	private ArrayList<Product> products_payment;
	private ScreenClear screen;
	private int count;
	private String date;
	
	public Pay(Db db, String date) {
		this.date = date;
		this.count = 0;
		this.db = db;
		this.sc = new Scanner(System.in);
		this.products = db.getProducts();
		this.cash = db.getCash();
		this.products_payment = new ArrayList();
	}
		
	public void startPay() {
		String answer = "";
		
		while(true) {
			printProducts();
			System.out.print("상품코드를 입력해주세요(마치려면 완료):");
			answer = sc.next();
			if(answer.equals("완료")) {
				break;
			}
			
			if(answer.length() != 6) {
				System.out.println("상품코드의 길이는 6이어야 합니다.");
				continue;
			}
			int check = 0;
			for (int i = 0; i < answer.length(); i++) {
				if (answer.charAt(i) >= 'a' && answer.charAt(i) <= 'z' || answer.charAt(i) >= 'A' && answer.charAt(i) <= 'Z')
					check++;
			}
			if(check != 6) {
				System.out.println("상품코드는 영어로 이루어져있어야 합니다.");
				continue;
			}
			answer = answer.toUpperCase();
			
			
				
			if(isExistProduct(answer)) {
				boolean exist = false;
				String file_name = answer.substring(0, 2);
				for(int i = 0; i < this.products.get(file_name).size(); i++) {
					if(this.products.get(file_name).get(i).getCode().indexOf(answer.substring(0, 4)) == 0) {
						
						if(this.products.get(file_name).get(i).getCode().equals(answer)&&this.isEdatePassed(this.products.get(file_name).get(i).getEpdate())) {
							System.out.println("유통기한이 지난 상품입니다.");
							exist = true;
							break;
						}
						
						for(int j = 0; j < products_payment.size(); j++) {
							if(this.products_payment.get(j).getCode().equals(answer)) {
								System.out.println("이미 존재하는 상품입니다.");
								exist = true;
								break;
							}
							
						}
						if(!exist) {
							System.out.println("상품이 추가되었습니다.");
							products_payment.add(this.products.get(file_name).get(i));
							break;
						}						
					}
				}
				if(exist)
					continue;
			}
			else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				continue;
			}

		}
		choose();
	}
	
	public void choose() { //결제수단선택
		int answer;
		while(true) {
			try {
				System.out.print("결제수단을 선택해주세요(1.현금, 2.카드)");
				answer = sc.nextInt();
				if(answer == 1) {
					purchase(1);
					break;
				}
				else if(answer == 2) {
					purchase(2);
					break;
				}
				else {
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				}
			}
			
			catch(InputMismatchException E) {
				System.out.println("정수만 입력해주세요.");
				sc.next();
				continue;
			}
		}
	}
	
	public void purchase(int type) { //결제할지 묻기
		String answer = "";
		while(true) {
			printProducts();
			System.out.print("결제 수단 : ");
			if(type == 1)
				System.out.println("현금");
			else
				System.out.println("카드");
			System.out.print("결제하시겠습니까?(Y/N)");
			answer = sc.next();
			
			if(answer.equals("Y")) {
				if(type == 1) {
					sendMoney();
					break;
				}
				else {
					for(int k = 0; k < products_payment.size(); k++)
						products_payment.get(k).setPayByCash(false);
					successPay();
					System.out.println("카드 결제가 완료되었습니다.");
					break;
				}
			}
			else if(answer.equals("N")) {
				
				return;
			}
			else {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
			
		}
	}
	
	public void sendMoney() { //금액입력
		//5만원, 만원, 오천원, 천원, 오백원, 백원, 오십원, 십원
		int answer = 0;
		int[] money_type = {50000, 10000, 5000, 1000, 500, 100, 50, 10};
		HashMap<Integer, Integer> charge = new HashMap<>();
		charge.put(50000, 0);
		charge.put(10000, 0);
		charge.put(5000, 0);
		charge.put(1000, 0);
		charge.put(500, 0);
		charge.put(100, 0);
		charge.put(50, 0);
		charge.put(10, 0);
		while(true) {
			try {
				System.out.print("받은 금액을 입력해주세요 : ");
				answer = sc.nextInt();
				if(answer < 0) {
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					continue;
				}
				int total = getTotalPay();
				if(answer < total) {
					System.out.println("받은 금액이 부족합니다. 다시 입력해주세요.");
					continue;
				}
				
				
				boolean flag = false;
				for(int j = 0; j < money_type.length; j++) {
					int m = money_type[j];
					charge.put(m, 0);
					for(int i = 0; i < cash.get(m); i++) {
						if(total / m > 0) {
							charge.put(m, charge.get(m)+1);
							total -= m;
						}
						else {
							break;
						}
					}
					if(total == 0) {
						flag = true;
						break;
					}
				}
				if(flag) {
					System.out.println("현금 결제가 완료되었습니다.");
					for(int k = 0; k < products_payment.size(); k++)
						products_payment.get(k).setPayByCash(true);
					successPay();
					for(int j = 0; j < money_type.length; j++) {
						int m = money_type[j];
						db.setCash(m, charge.get(m), true);
					}
					break;
				}
				else {
					System.out.println("현금이 부족합니다.");	//현금충전 프롬프트로 넘어가기(아직 미완)
					adCash();
				}
				
				
				
			}
			catch(InputMismatchException E) {
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				sc.next();
				continue;
			}
		}
	}
	
	public void successPay() {
		String payCode = createPayCode();
		db.addPayment(payCode, products_payment);
		
		for(int i = 0; i < products_payment.size(); i++) {
			db.removeProduct(products_payment.get(i).getCode());
		}
		
	}
	
	public String createPayCode() {
		String date = this.date.substring(2);
		count++;
		if(count / 100 == 0) {
			date+="0";
		}
		if(count / 10 == 0) {
			date+="0";
		}
		date += count;
		return date;
	}
	
	public boolean isEdatePassed(String Epdate) {
		int year = Integer.parseInt(Epdate.substring(0, 4));
		int month = Integer.parseInt(Epdate.substring(4, 6));
		int date = Integer.parseInt(Epdate.substring(6, 8));
		
		int year_last = Integer.parseInt(this.date.substring(0, 4));
		int month_last = Integer.parseInt(this.date.substring(4, 6));
		int date_last = Integer.parseInt(this.date.substring(6, 8));
		
		
		if(year < year_last) {
			return true;
		}
		if(year == year_last && month < month_last) {
			return true;
		}
		if(year == year_last && month == month_last && date < date_last) {
			return true;
		}
		return false;
	}
	
	public boolean isExistProduct(String code) {
		
		String file_name = code.substring(0, 2);
		try {
			for(int i=0; i<products.get(file_name).size(); i++) {
				if(products.get(file_name).get(i).getCode().equals(code)) {
					return true;
				}
			}
			
		}
		catch(NullPointerException e) {
			return false;
		}
		return false;
	}
	
	public void printProducts() {
		int total = 0;
		HashMap<String, Integer> names = new HashMap<>();
		for(int i = 0; i < this.products_payment.size(); i++) {
			if(names.containsKey(products_payment.get(i).getName())) {
				names.put(products_payment.get(i).getName(), names.get(products_payment.get(i).getName())+1);
			}
			else {
				names.put(products_payment.get(i).getName(), 1);
			}
		}
		
		Set<String> keys = names.keySet();
		
		Iterator<String> it = keys.iterator();
		System.out.println("==========상품목록==========");
		while(it.hasNext()) {
			String key = it.next();
			System.out.print("상품명 : " + key + "/" + names.get(key) + "개/");
			for(int i = 0; i < this.products_payment.size(); i++) {
				if(products_payment.get(i).getName().equals(key)) {
					System.out.println(products_payment.get(i).getPrice()*names.get(key)+"원");
					total += products_payment.get(i).getPrice()*names.get(key);
					break;
				}
			}
		}
		System.out.println("==========================");
		System.out.println("총 지불할 금액 : " + total + "원");
		
	}
	
	public int getTotalPay() {
		int total = 0;
		HashMap<String, Integer> names = new HashMap<>();
		for(int i = 0; i < this.products_payment.size(); i++) {
			if(names.containsKey(products_payment.get(i).getName())) {
				names.put(products_payment.get(i).getName(), names.get(products_payment.get(i).getName())+1);
			}
			else {
				names.put(products_payment.get(i).getName(), 1);
			}
		}
		Set<String> keys = names.keySet();
		Iterator<String> it = keys.iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			System.out.print("상품명 : " + key + "/" + names.get(key) + "개/");
			for(int i = 0; i < this.products_payment.size(); i++) {
				if(products_payment.get(i).getName().equals(key)) {
					
					total += products_payment.get(i).getPrice()*names.get(key);
					break;
				}
			}
		}
		return total;
	}
	
	public void adCash() {
		HashMap<Integer, Integer>cash = db.getCash();
		String money ="";
		int count = 0;
		do {
			if(count!=0)
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			System.out.print("충전할 지폐(동전)를 선택해주세요 : ");
			money = sc.next();
		}
		while(!money.matches("[1-8]"));
			if(db.isPossible(Integer.parseInt(money))) {
				
				String insert = "";
				int num = 0;
				do {
					if(insert.length()>=3)
						System.out.println("길이가 너무 깁니다. 다시 입력해주세요. ");
				else if(num!=0)
						System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					System.out.print("총 몇 개의 지폐를 추가하실 건가요 ? : ");
					insert=sc.next();
				}while(!insert.matches("[1-9]{1,2}|[1-9]"));
				db.setCash(cash.get(db.key[Integer.parseInt(money)-1]), Integer.parseInt(insert), false);
			}
	
	}

}
