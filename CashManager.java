package sw.pos;

import java.util.HashMap;
import java.util.Scanner;

public class CashManager {
	Scanner scan = new Scanner(System.in);
	private Db db;
	private HashMap<Integer, Integer> cash;
	private int [] key = {10, 50, 100, 500, 1000, 5000, 10000, 50000};//���� ����.
	public CashManager(Db db) {
		this.db = db;
		cash = this.db.getCash();
	}
	
	public void ManageCash() {
		for(int i=0;i<8;i++) {
			System.out.println((i+1)+"."+key[i]+"��");
		}
		String money ="";
		int count = 0;
		do {
			if(count!=0)
				System.out.println("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���.");
			System.out.print("������ ����(����)�� �������ּ��� : ");
			money = scan.next();
		}
		while(!money.matches("[1-8]"));
			if(isPossible(Integer.parseInt(money))) {
				
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
				setCash(key[Integer.parseInt(money)-1], Integer.parseInt(insert), false);
			}
	}
	
	public boolean isPossible(int i) {
		if(cash.get(key[i-1])==99) {
			System.out.println("�ش� ������ ������ 99���� �� �̻� �߰��� �Ұ��մϴ�.");
			return false;
		}else 
			return true;
	}
	
	public void setCash(int unit, int count, boolean isNegative) {
		int num = cash.get(unit);
		
		if(isNegative) {//�ܷ� ����.
			if(num-count>0)
			{	System.out.println(unit+"�� "+count+" �� ���� �Ϸ�Ǿ����ϴ�.");
				db.setCash(unit, count, isNegative);
			}else
			{
				System.out.println(unit+"�� ���� ������ 0���� �Ǿ����ϴ�.");
				db.setCash(unit, num, isNegative);
			}
		}
		else//�ܷ� ����.
			{
			if(num+count<=99) {
			System.out.println(unit+"�� "+count+" �� ���� �Ϸ�Ǿ����ϴ�.");
			db.setCash(unit, count, isNegative);
			}
			else {
				System.out.println("�ִ� 99�������� �߰��� ������ "+unit+"�� ����� �� 99���� �Ǿ����ϴ�.");
				db.setCash(unit, 99-num, isNegative);
			}
			}
	}
	public int getKeyindex(int i) {
		return key[i];
	}
}
