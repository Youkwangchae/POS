package sw.pos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {
	Scanner scan = new Scanner(System.in);
	String date;//��¥ �Է¹���.
	String m_date; //20XX_YY_ZZ
	Db db;
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
			Db db = new Db();
			if(db.isPossible(date, 2))//���� ��¥���� Ȯ��.
			{
				m_date = db.setLast_date(date);//�ùٸ� ��¥���� Ȯ�������� Date.txt�� �ְ�, ���θ޴��� ����.
				start(db);//���� �޴� �����ֱ�.
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
	
	
	
	public void start(Db db) throws InterruptedException, IOException{
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
				HashMap<Integer, Integer>cash = db.getCash();
				String money ="";
				int count = 0;
				do {
					if(count!=0)
						System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
					System.out.print("������ ����(����)�� �������ּ��� : ");
					money = scan.next();
				}
				while(!money.matches("[1-8]"));
					if(db.isPossible(Integer.parseInt(money))) {
						
						String insert = "";
						int num = 0;
						do {
							if(insert.length()>=3)
								System.out.println("���̰� �ʹ� ��ϴ�. �ٽ� �Է����ּ���. ");
						else if(num!=0)
								System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
							System.out.print("�� �� ���� ���� �߰��Ͻ� �ǰ��� ? : ");
							insert=scan.next();
						}while(!insert.matches("[1-9]{1,2}|[1-9]"));
						db.setCash(cash.get(db.key[Integer.parseInt(money)-1]), Integer.parseInt(insert), false);
					}
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
	
	