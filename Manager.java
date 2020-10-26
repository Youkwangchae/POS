package sw.pos;

import java.util.Scanner;

public class Manager {
	Scanner scan = new Scanner(System.in);
	String date;//날짜 입력받음.
	public void menu() {
		while(true) {
		System.out.println("종료하시려면 \"종료\"를 입력해주세요");
		System.out.print("날짜입력 : ");
		date = scan.next();
		if(date.equals("종료")) {
			//System.out.println("POS기 작동 종료");
			return;
		}
		
		else if(isPossible(date))	
		{
			Db db = new Db(date);
			if(db.isPossible(date))//이전 날짜인지 확인.
			{
				System.out.println(db.setLast_date(date));//올바른 날짜임을 확인했으니 Date.txt에 넣고, 메인메뉴로 들어옴.
				start(db);//메인 메뉴 보여주기.
			}
		}
		
		}
		
		//Db db = new Db(date);
		//db.addProduct(new Product("BBAAAA", "코카콜라", "20201101", "2000"));
		//db.removeProduct("BBAAAA");
		//db.addNames("조리퐁");
		//db.addNames("코카콜라", "BBAA", 20);
		//db.setCash(50000, 10, false);
		//db.removePayment("201025001", "AAAAAC");
		//System.out.println("Done");
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
	
	public boolean isPossible(int i, int begin, int end) {
		if(i>=begin&&i<=end)
			return true;
		else {
			System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			return false;
		}
	}
	
	public void start(Db db) {
		boolean con = true;
		
		while(con) {
		System.out.println("1. 결제하기");
		System.out.println("2. 환불하기");
		System.out.println("3. 재고관리");
		System.out.println("4. 현금관리");
		System.out.println("5. 매출확인");
		System.out.println("6. 종료");
		int select = scan.nextInt();
		if(isPossible(select,1,6)) {
			switch(select) {
			case 1: //1. 결제하기	
			case 2: //2. 환불하기
			case 3: //3. 재고관리	
			case 4: //4. 현금관리
				
			case 5: //5. 매출확인	
			case 6: //6. 종료
				con = false;
			}
		}
		
		}
	}
	
	
	
}
