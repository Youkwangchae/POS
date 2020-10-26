package sw.pos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AddInventory {
	
	ScreenClear sc = new ScreenClear();
	Scanner scan = new Scanner(System.in);
	Db db;
	FileIO fileio = new FileIO();
	Set<String> set; 														//키 값을 저장하는 set
	Iterator<String> it; 													//set 검색을 위한 iterator
	String key, proname;													//Set에서 검색을 위한 스트링 변수, 상품이름
	String catecode, procode, today, ep_date, price;						//상품의 카테고리 코드, 상품코드, 오늘 날짜, 유통기한, 가격
	int procount, last_num, epd_value;   									//추가할 개수생성된 개수, 유통기한 설정 값
	boolean check;															//검색 성공 여부 확인
	private HashMap<String, NameInfo> products;								//상품이름 저장하는 리스트
	
	public AddInventory(Db db)
	{
		this.db = db;
		products = db.getNames();	
		today = db.getLast_date();
		System.out.print("추가할 상품의 이름을 입력해주세요: ");
		proname = scan.next();
		
		while( (!proname.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) || (proname.length() > 10) )	//상품명 예외처리
		{
			System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 한글, 10글자 이하): ");
			proname = scan.next();
		}
		
		set = products.keySet();			//올바른 상품명을 입력받은 경우 이미 있는 이름인지 검색
		it = set.iterator();
		for(int i=0; i<products.size(); i++) 
		{
			key = it.next();
			if(key.equals(proname))	//있는 경우
			{
				check = true;
				break;
			}
			else	//없는 경우
				check = false;
		}
		
		if(!check)		//검색 실패
		{
			System.out.println("존재하지 않는 상품입니다.");
			return;
		}
		else			//검색 성공
		{
			catecode = products.get(proname).getCate_code();
			last_num = products.get(proname).getLast_num();
			epd_value = products.get(proname).getEpd_value();
			price = Integer.toString(products.get(proname).getPrice());
			ep_date = FindEp_date(today, epd_value);
		}
		
		if(products.get(proname).getLast_num() >= 26*26)		//해당 상품의 재고가 꽉 찬 경우
		{
			System.out.println("해당 상품은 더 이상 재고를 추가할 수 없습니다.");
			System.out.println("재고 관리로 돌아갑니다.");
			return;
		}
			
		
		System.out.print("추가할 개수를 입력해주세요: ");
		String temp = scan.next();
		
		while( (!temp.matches(".*[0-9]+.*")) || (temp.length() > 3) )	//추가 개수 예외처리
		{
			System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 숫자, 3글자 이하): ");
			temp = scan.next();
		}	
		procount = Integer.parseInt(temp);
		if(procount > 676)					//676이상이면 676개만 저장
			procount = 676;
		if(procount + last_num > 676)		//현재 저장된 개수 + 추가할 개수 > 676일때
			procount -= last_num;
	
		for(int i = 0; i < procount; i++)
		{
			procode = catecode + String.valueOf((char) ('A' + products.get(proname).getLast_num()/26) + String.valueOf((char) ('A' + products.get(proname).getLast_num()%26)));
			db.addProduct(new Product(procode, proname, ep_date, price));
			db.addNames(proname);
		}
		
	}
	
	public String FindEp_date(String today, int epd_value)			//유통기한 계산 함수
	{
		String ep_date;
		int[] maxdate = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int value, Cyear, Cmonth, Cday;	//현재 년, 월, 일
		value = epd_value;
		Cyear = Integer.parseInt(today.substring(0, 4));
		Cmonth = Integer.parseInt(today.substring(4, 6));
		Cday = Integer.parseInt(today.substring(6, 8));
		
		while(value > 0)
		{
			value--;
			if(Cday != maxdate[Cmonth-1])	//오늘 일이 월의 최대일이 아닐 때
				Cday++;
			else							//최대 일 일때 다음 달 1일로 변경
			{
				Cday = 1;			
				if(Cmonth == 12)			//12월일때 다음 년 1월로 변경
				{
					Cyear += 1;
					Cmonth = 1;		
				}
				else						//12월이 아니면 다음 달로 변경
					Cmonth++;
			}	
		}
		ep_date = Integer.toString(Cyear);
		if(Integer.toString(Cmonth).length() == 1)	//한 자리 수 월이면
			ep_date += "0" + Integer.toString(Cmonth);
		else										//두 자리 수이면
			ep_date += Integer.toString(Cmonth);
		
		if(Integer.toString(Cday).length() == 1)	//한 자리 수 일이면
			ep_date += "0" + Integer.toString(Cday);
		else										//두 자리 수이면
			ep_date += Integer.toString(Cday);
		
		
		return ep_date;
	}
	
}

