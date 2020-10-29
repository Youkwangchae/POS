package sw.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Manager {
	Scanner scan = new Scanner(System.in);
	private String date;//��¥ �Է¹���.
	private String last_date;//�ֱ� ��¥.
	private String m_date; //20XX_YY_ZZ
	private Db db;
	private CashManager c_Manager;
	
	public Manager() {
		this.db = new Db();
		
	}
	
	public void menu() throws InterruptedException, IOException{
		while(true) {
		System.out.println("�����Ͻ÷��� \"����\"�� �Է����ּ���");
		System.out.print("��¥�Է� : ");
		date = scan.nextLine();
		if(date.equals("����")) {
			return;
		}
		
		else if(isPossible(date))	
		{
			last_date = db.getLast_date();
			if(isPossible(date, 2))//���� ��¥���� Ȯ��.
			{	
				db.setLast_date(date);//�ùٸ� ��¥���� Ȯ�������� Date.txt�� �ְ�, ���θ޴��� ����.
				m_date=date.substring(0,4)+"_"+date.substring(4,6)+"_"+date.substring(6);
				start();//���� �޴� �����ֱ�.
			}
		}
		
		}
		
	}
	
	//�ùٸ� ��¥ �Է����� �Ǵ�. ���ڰ� �ƴ� �Էµ� �ɷ�����.
		public boolean isPossible(String date) {
			int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //1��~12�� ���� ��.
			int month;
			if(date.length()<8){
				System.out.println("�Է±��̰� 8���� �۽��ϴ�. �ٽ� �Է����ּ���.");
				return false;
			}else if(date.length()>8){
				System.out.println("�Է±��̰� 8���� Ů�ϴ�. �ٽ� �Է����ּ���.");
				return false;
			}
			else {
				boolean possible = false;
				if(date.charAt(0)=='2'&&date.charAt(1)=='0') {
					if( (date.charAt(2)-'0') >=0 && (date.charAt(2)-'0')<=9) {
						if( (date.charAt(3)-'0') >=0 && (date.charAt(3)-'0')<=9) {
							month = date.charAt(5)-'0';
							switch(date.charAt(4)-'0') {
							case 0:
								if(month>=1 && month<=9) {
									if(Integer.parseInt(date.substring(6)) <= days[month-1]&&Integer.parseInt(date.substring(6))>0)
										possible = true;
								}
								break;
							case 1:
								if(month>=0&&month<=2)
									if(Integer.parseInt(date.substring(6)) <= days[10+month-1]&&Integer.parseInt(date.substring(6))>0)
										possible = true;
							}
						}
					}
				}
				if(possible)
					return true;
				else {
					System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
					return false;
				}
			}
		}
	
		public boolean isPossible(String date, int i) {
			if(i==8)//���� ��¥.
				return true;
			if(date.charAt(i)>last_date.charAt(i))//���� ��¥.
				return true;
			else if(date.charAt(i)==last_date.charAt(i))
				return isPossible(date,i+1);
			else//���� ��¥.
			{
				System.out.println("������ ��¥�� �Է��Ͻø� �ȵ˴ϴ�. �ٽ� �Է����ּ���.");
				return false;
			}
		}
		
	public void start() throws InterruptedException, IOException{
		boolean con = true;
		
		while(con) {
		System.out.println(m_date);
		System.out.println("1. �����ϱ�");
		System.out.println("2. ȯ���ϱ�");
		System.out.println("3. ������");
		System.out.println("4. ���ݰ���");
		System.out.println("5. ����Ȯ��");
		System.out.println("6. ����");
		String select = "";
		int cnt = 0;
		do {
			if(cnt!=0)
				System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
			System.out.print("\n�޴� ����: ");
			select = scan.nextLine();
			cnt++;
		}
		while(!select.matches("[1-6]"));
		{
			switch(Integer.parseInt(select)) {
			case 1: //1. �����ϱ�	
				Pay pay = new Pay(db, date);
				pay.startPay();
				break;
			case 2: //2. ȯ���ϱ�
				Refund r=new Refund(db);
				r.RefundS();
				break;
			case 3: //3. ������	
				InventoryManager im = new InventoryManager(db);
				break;
			case 4: //4. ���ݰ���
				this.c_Manager = new CashManager(db);
				c_Manager.ManageCash(false);
				break;
			case 5: //5. ����Ȯ��
				totalCash();
				break;
			case 6: //6. ����
				con = false;
			}
		}
		
		}
	}
	
	public void totalCash() {
		//�Է��� ��¥�� PaymentList�� �ִ� ���� ���鸸 �����ָ� ��.
		//���� �Է��ϸ� �ٽ� ���ư�. @�� ������ �� �� substring.
		int total = 0;
		HashMap<String, ArrayList<Product>> pays = db.getPayments();
		Set <String>keys = pays.keySet();
		Iterator<String> it = keys.iterator();
		String key = "";
		String date = "";
		while(it.hasNext()) {
			key = it.next();
			date = "20"+key.substring(0,6);
			if(date.equals(last_date)) {
				for(int j=0;j<pays.get(key).size();j++)
				total += pays.get(key).get(j).getPrice();
			}
			
		}
		
		System.out.println("���� "+m_date+"�� ������Ȳ�Դϴ�.");
		System.out.println(total);
		System.out.println("���� �޴��� ���ư��÷��� \"�Ϸ�\"�� �Է����ּ���. :");
		String temp = scan.nextLine();
		
		while( (!temp.matches("�Ϸ�")))	//�߰� ���� ����ó��
		{
				System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY �Ϸ�): ");
				temp = scan.nextLine();
		}	
		System.out.println("���� �޴��� ���ư��ϴ�.");
	}
	
}
	
	