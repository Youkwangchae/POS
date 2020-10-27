package sw.pos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {
	Scanner scan = new Scanner(System.in);
	String date;//날짜 입력받음.
	String m_date; //20XX_YY_ZZ
	Db db;
	public void menu() throws InterruptedException, IOException{
		while(true) {
		System.out.println("종료하시려면 \"종료\"를 입력해주세요");
		System.out.print("날짜입력 : ");
		date = scan.next();
		if(date.equals("종료")) {
			return;
		}
		
		else if(isPossible(date))	
		{
			Db db = new Db();
			if(db.isPossible(date, 2))//이전 날짜인지 확인.
			{
				m_date = db.setLast_date(date);//올바른 날짜임을 확인했으니 Date.txt에 넣고, 메인메뉴로 들어옴.
				start(db);//메인 메뉴 보여주기.
			}
		}
		
		}
		
	}
	//올바른 날짜 입력인지 판단. 숫자가 아닌 입력도 걸러줬음.
	public boolean isPossible(String date) {
		int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //1월~12월 직접 비교.
		int month;
		if(date.length()<8){
			System.out.println("입력길이가 8보다 작습니다. 다시 입력해주세요.");
			return false;
		}else if(date.length()>8){
			System.out.println("입력길이가 8보다 큽니다. 다시 입력해주세요.");
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
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
				return false;
			}
		}
	}
	
	
	
	public void start(Db db) throws InterruptedException, IOException{
		boolean con = true;
		
		while(con) {
		System.out.println(m_date);
		System.out.println("1. 결제하기");
		System.out.println("2. 환불하기");
		System.out.println("3. 재고관리");
		System.out.println("4. 현금관리");
		System.out.println("5. 매출확인");
		System.out.println("6. 종료");
		String select = "";
		int cnt = 0;
		do {
			if(cnt!=0)
				System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			System.out.print("\n메뉴 선택: ");
			select = scan.next();
			cnt++;
		}
		while(!select.matches("[1-6]"));
		{
			switch(Integer.parseInt(select)) {
			case 1: //1. 결제하기	
				Pay pay = new Pay(db, date);
				pay.startPay();
				break;
			case 2: //2. 환불하기
				break;
			case 3: //3. 재고관리	
				InventoryManager im = new InventoryManager(db);
				break;
			case 4: //4. 현금관리
				HashMap<Integer, Integer>cash = db.getCash();
				String money ="";
				int count = 0;
				do {
					if(count!=0)
						System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					System.out.print("충전할 지폐(동전)를 선택해주세요 : ");
					money = scan.next();
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
							insert=scan.next();
						}while(!insert.matches("[1-9]{1,2}|[1-9]"));
						db.setCash(cash.get(db.key[Integer.parseInt(money)-1]), Integer.parseInt(insert), false);
					}
				break;
			case 5: //5. 매출확인
				break;
			case 6: //6. 종료
				con = false;
			}
		}
		
		}
	}
}
	
	