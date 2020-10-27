package sw.pos;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {
	Scanner scan = new Scanner(System.in);
	private String date;//날짜 입력받음.
	private String last_date;//최근 날짜.
	private String m_date; //20XX_YY_ZZ
	private Db db;
	private CashManager c_Manager;
	
	public Manager() {
		this.db = new Db();
		
	}
	
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
			if(isPossible(date, 2))//이전 날짜인지 확인.
			{
				db.setLast_date(date);//올바른 날짜임을 확인했으니 Date.txt에 넣고, 메인메뉴로 들어옴.
				last_date = db.getLast_date();
				m_date=last_date.substring(0,4)+"_"+last_date.substring(4,6)+"_"+last_date.substring(6);
				start();//메인 메뉴 보여주기.
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
	
		public boolean isPossible(String date, int i) {
			if(i==8)//같은 날짜.
				return true;
			if(date.charAt(i)>date.charAt(i))//이후 날짜.
				return true;
			else if(date.charAt(i)==date.charAt(i))
				return isPossible(date,i+1);
			else//이전 날짜.
				return false;
		}
		
	public void start() throws InterruptedException, IOException{
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
				this.c_Manager = new CashManager(db);
				c_Manager.ManageCash();
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
	
	