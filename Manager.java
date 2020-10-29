package sw.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

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
		date = scan.nextLine();
		if(date.equals("종료")) {
			return;
		}
		
		else if(isPossible(date))	
		{
			last_date = db.getLast_date();
			if(isPossible(date, 2))//이전 날짜인지 확인.
			{	
				db.setLast_date(date);//올바른 날짜임을 확인했으니 Date.txt에 넣고, 메인메뉴로 들어옴.
				m_date=date.substring(0,4)+"_"+date.substring(4,6)+"_"+date.substring(6);
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
					System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
					return false;
				}
			}
		}
	
		public boolean isPossible(String date, int i) {
			if(i==8)//같은 날짜.
				return true;
			if(date.charAt(i)>last_date.charAt(i))//이후 날짜.
				return true;
			else if(date.charAt(i)==last_date.charAt(i))
				return isPossible(date,i+1);
			else//이전 날짜.
			{
				System.out.println("이전의 날짜를 입력하시면 안됩니다. 다시 입력해주세요.");
				return false;
			}
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
			select = scan.nextLine();
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
				Refund r=new Refund(db);
				r.RefundS();
				break;
			case 3: //3. 재고관리	
				InventoryManager im = new InventoryManager(db);
				break;
			case 4: //4. 현금관리
				this.c_Manager = new CashManager(db);
				c_Manager.ManageCash(false);
				break;
			case 5: //5. 매출확인
				totalCash();
				break;
			case 6: //6. 종료
				con = false;
			}
		}
		
		}
	}
	
	public void totalCash() {
		//입력한 날짜의 PaymentList에 있는 결제 값들만 더해주면 됨.
		//종료 입력하면 다시 돌아감. @로 나누고 맨 앞 substring.
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
		
		System.out.println("오늘 "+m_date+"의 매출현황입니다.");
		System.out.println(total);
		System.out.println("메인 메뉴로 돌아가시려면 \"완료\"를 입력해주세요. :");
		String temp = scan.nextLine();
		
		while( (!temp.matches("완료")))	//추가 개수 예외처리
		{
				System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 완료): ");
				temp = scan.nextLine();
		}	
		System.out.println("메인 메뉴로 돌아갑니다.");
	}
	
}
	
	