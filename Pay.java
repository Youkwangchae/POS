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
				System.out.print("��ǰ�ڵ带 �Է����ּ���(��ġ���� �Ϸ�):");
				answer = sc.next();
				if(answer.equals("�Ϸ�")) {
					break;
				}
				
				if(answer.length() != 6) {
					System.out.println("��ǰ�ڵ��� ���̴� 6�̾�� �մϴ�.");
					continue;
				}
				int check = 0;
				for (int i = 0; i < answer.length(); i++) {
					if (answer.charAt(i) >= 'a' && answer.charAt(i) <= 'z' || answer.charAt(i) >= 'A' && answer.charAt(i) <= 'Z')
						check++;
				}
				if(check != 6) {
					System.out.println("��ǰ�ڵ�� ����� �̷�����־�� �մϴ�.");
					continue;
				}
				answer = answer.toUpperCase();
				
				//�����Ǻ�
				//����θ� �����ִ� �ڵ����� �Ǻ�
					
				if(isExistProduct(answer)) {
					boolean exist = false;
					String file_name = answer.substring(0, 2);
					for(int i = 0; i < this.products.get(file_name).size(); i++) {
						if(this.products.get(file_name).get(i).getCode().indexOf(answer.substring(0, 4)) == 0) {
							
							if(this.products.get(file_name).get(i).getCode().equals(answer)&&this.isEdatePassed(this.products.get(file_name).get(i).getEpdate())) {
								System.out.println("��������� ���� ��ǰ�Դϴ�.");
								exist = true;
								break;
							}
							
							for(int j = 0; j < products_payment.size(); j++) {
								if(this.products_payment.get(j).getCode().equals(answer)) {
									System.out.println("�̹� �����ϴ� ��ǰ�Դϴ�.");
									exist = true;
									break;
								}
								
							}
							if(!exist) {
								System.out.println("��ǰ�� �߰��Ǿ����ϴ�.");
								products_payment.add(this.products.get(file_name).get(i));
								break;
							}						
						}
					}
					if(exist)
						continue;
				}
				else {
					System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
					continue;
				}
	
			}
			choose();
		}
		
		public void choose() { //�������ܼ���
			int answer;
			while(true) {
				try {
					System.out.print("���������� �������ּ���(1.����, 2.ī��)");
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
						System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
					}
				}
				
				catch(InputMismatchException E) {
					System.out.println("������ �Է����ּ���.");
					sc.next();
					continue;
				}
			}
		}
		
		public void purchase(int type) { //�������� ����
			String answer = "";
			while(true) {
				printProducts();
				System.out.print("���� ���� : ");
				if(type == 1)
					System.out.println("����");
				else
					System.out.println("ī��");
				System.out.print("�����Ͻðڽ��ϱ�?(Y/N)");
				answer = sc.next();
				
				if(answer.equals("Y")) {
					if(type == 1) {
						sendMoney();
						break;
					}
					else {
						successPay();
						System.out.println("ī�� ������ �Ϸ�Ǿ����ϴ�.");
						break;
					}
				}
				else if(answer.equals("N")) {
					
					return;
				}
				else {
					System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
				}
				
			}
		}
		
		public void sendMoney() { //�ݾ��Է�
			//5����, ����, ��õ��, õ��, �����, ���, ���ʿ�, �ʿ�
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
					System.out.print("���� �ݾ��� �Է����ּ��� : ");
					answer = sc.nextInt();
					if(answer < 0) {
						System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
						continue;
					}
					int total = getTotalPay();
					if(answer < total) {
						System.out.println("���� �ݾ��� �����մϴ�. �ٽ� �Է����ּ���.");
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
						System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
						successPay();
						for(int j = 0; j < money_type.length; j++) {
							int m = money_type[j];
							db.setCash(m, charge.get(m), true);
						}
						break;
					}
					else {
						System.out.println("������ �����մϴ�.");	//�������� ������Ʈ�� �Ѿ��(���� �̿�)
						
					}
					
					
					
				}
				catch(InputMismatchException E) {
					System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
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
			System.out.println("==========��ǰ���==========");
			while(it.hasNext()) {
				String key = it.next();
				System.out.print("��ǰ�� : " + key + "/" + names.get(key) + "��/");
				for(int i = 0; i < this.products_payment.size(); i++) {
					if(products_payment.get(i).getName().equals(key)) {
						System.out.println(products_payment.get(i).getPrice()*names.get(key)+"��");
						total += products_payment.get(i).getPrice()*names.get(key);
						break;
					}
				}
			}
			System.out.println("==========================");
			System.out.println("�� ������ �ݾ� : " + total + "��");
			
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
				System.out.print("��ǰ�� : " + key + "/" + names.get(key) + "��/");
				for(int i = 0; i < this.products_payment.size(); i++) {
					if(products_payment.get(i).getName().equals(key)) {
						
						total += products_payment.get(i).getPrice()*names.get(key);
						break;
					}
				}
			}
			return total;
		}
		
	
	}