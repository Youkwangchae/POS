package sw.pos;

import java.util.Scanner;

public class Manager {
	Scanner scan = new Scanner(System.in);
	public void menu() {
		String date;//��¥ �Է¹���.
		
		while(true) {
		System.out.println("�����Ͻ÷��� \"����\"�� �Է����ּ���");
		System.out.print("��¥�Է� : ");
		date = scan.next();
		if(date.equals("����")) {
			//System.out.println("POS�� �۵� ����");
			return;
		}
		
		else if(isPossible(date))	
		{
			Db db = new Db(date);
			if(db.isPossible(date))//���� ��¥���� Ȯ��.
			{
				System.out.println(db.setLast_date(date));//�ùٸ� ��¥���� Ȯ�������� Date.txt�� �ְ�, ���θ޴��� ����.
				start(db);//���� �޴� �����ֱ�.
			}
		}
		
		}
		
		//Db db = new Db(date);
		//db.addProduct(new Product("BBAAAA", "��ī�ݶ�", "20201101", "2000"));
		//db.removeProduct("BBAAAA");
		//db.addNames("������");
		//db.addNames("��ī�ݶ�", "BBAA", 20);
		//db.setCash(50000, 10, false);
		//db.removePayment("201025001", "AAAAAC");
		//System.out.println("Done");
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
	
	public boolean isPossible(int i, int begin, int end) {
		if(i>=begin&&i<=end)
			return true;
		else {
			System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
			return false;
		}
	}
	
	public void start(Db db) {
		boolean con = true;
		
		while(con) {
		System.out.println("1. �����ϱ�");
		System.out.println("2. ȯ���ϱ�");
		System.out.println("3. ������");
		System.out.println("4. ���ݰ���");
		System.out.println("5. ����Ȯ��");
		System.out.println("6. ����");
		int select = scan.nextInt();
		if(isPossible(select,1,6)) {
			switch(select) {
			case 1: //1. �����ϱ�	
			case 2: //2. ȯ���ϱ�
			case 3: //3. ������	
			case 4: //4. ���ݰ���
				
			case 5: //5. ����Ȯ��	
			case 6: //6. ����
				con = false;
			}
		}
		
		}
	}
	
	
	
}
