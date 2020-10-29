package sw.pos;

import java.util.HashMap;
import java.util.Scanner;

public class CashManager {
	Scanner scan = new Scanner(System.in);
	private Db db;
	private HashMap<Integer, Integer> cash;
	private int [] key = {10, 50, 100, 500, 1000, 5000, 10000, 50000};//현금 종류.
	public CashManager(Db db) {
		this.db = db;
		this.cash = db.getCash();
	}
	
	public void ManageCash(boolean charge) {
		boolean con = true;
		while(con) {
		HashMap<Integer, Integer>cash = db.getCash();
		for(int i=0;i<8;i++) 
			System.out.println((i+1)+"."+key[i]+"원 "+cash.get(key[i])+"개");
		System.out.println();
		
		for(int i=0;i<8;i++) 
			System.out.println((i+1)+"."+key[i]+"원");
		
		String money ="";
		int count = 0;
		do {
			if(count!=0)
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			System.out.print("충전할 지폐(동전)를 선택해주세요 : ");
			money = scan.nextLine();
			count++;
		}
		while(!money.matches("[1-8]|완료"));
		if(money.equals("완료")) {
			if(!charge)
				System.out.println("메인 메뉴로 돌아갑니다.");
			con = false;
		}	
		else {
			if(isPossible(Integer.parseInt(money))) {
				
				String insert = "";
				int num = 0;
				do {
					if(insert.length()>=3)
						System.out.println("길이가 너무 깁니다. 다시 입력해주세요. ");
				else if(num!=0)
						System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					System.out.print("총 몇 개의 지폐를 추가하실 건가요 ? : ");
					insert=scan.nextLine();
					num++;
				}while(!insert.matches("[1-9][0-9]|[1-9]"));
				System.out.println("정말 이렇게 추가하실 건가요? (Y 또는 N 입력)");
				String ans = "";
				ans = scan.nextLine();
				while(!ans.matches("Y|N")) {
					System.out.println("잘못된 입력입니다. Y 또는 N만 입력해주세요.");
				}
				if(ans.equals("Y"))
					setCash(key[Integer.parseInt(money)-1], Integer.parseInt(insert), false);
				
			}
		}
		}
	}
	
	public boolean isPossible(int i) {
		if(cash.get(key[i-1])==99) {
			System.out.println("해당 지폐의 개수가 99개라 더 이상 추가가 불가합니다.");
			return false;
		}else 
			return true;
	}
	
	public void setCash(int unit, int count, boolean isNegative) {
		int num = cash.get(unit);
		
		if(isNegative) {//잔량 감소.
			if(num-count>0)
			{	System.out.println(unit+"원 "+count+" 개 감소 완료되었습니다.");
				db.setCash(unit, count, isNegative);
			}else
			{
				System.out.println(unit+"원 지폐 개수가 0개가 되었습니다.");
				db.setCash(unit, num, isNegative);
			}
		}
		else//잔량 증가.
			{
			if(num+count<=99) {
			System.out.println(unit+"원 "+count+" 개 증가 완료되었습니다.");
			db.setCash(unit, count, isNegative);
			}
			else {
				System.out.println("최대 99개까지만 추가가 가능해 "+unit+"원 지폐는 총 99개가 되었습니다.");
				db.setCash(unit, 99-num, isNegative);
			}
			}
	}
	
	public int getKeyindex(int i) {
		return key[i];
	}
}
