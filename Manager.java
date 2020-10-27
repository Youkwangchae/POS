package sw.pos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

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
		date = scan.next();
		if(date.equals("����")) {
			return;
		}
		
		else if(isPossible(date))	
		{
			if(isPossible(date, 2))//���� ��¥���� Ȯ��.
			{
				db.setLast_date(date);//�ùٸ� ��¥���� Ȯ�������� Date.txt�� �ְ�, ���θ޴��� ����.
				last_date = db.getLast_date();
				m_date=last_date.substring(0,4)+"_"+last_date.substring(4,6)+"_"+last_date.substring(6);
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
									if(Integer.parseInt(date.substring(6)) <= days[month-1])
										possible = true;
								}
							case 1:
								if(month>=0&&month<=2)
									if(Integer.parseInt(date.substring(6)) <= days[10+month-1])
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
			if(date.charAt(i)>date.charAt(i))//���� ��¥.
				return true;
			else if(date.charAt(i)==date.charAt(i))
				return isPossible(date,i+1);
			else//���� ��¥.
				return false;
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
			select = scan.next();
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
				break;
			case 3: //3. ������	
				InventoryManager im = new InventoryManager(db);
				break;
			case 4: //4. ���ݰ���
				this.c_Manager = new CashManager(db);
				c_Manager.ManageCash();
				break;
			case 5: //5. ����Ȯ��
				break;
			case 6: //6. ����
				con = false;
			}
		}
		
		}
	}
}
	
	