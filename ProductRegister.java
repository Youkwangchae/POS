package sw.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ProductRegister 
{
	
	Scanner scan = new Scanner(System.in);
	Db db;
	FileIO fileio = new FileIO();
	Set<String> set; 		//키 값을 저장하는 set
	Iterator<String> it; 	//set 검색을 위한 iterator
	private HashMap<String, NameInfo> products;		//상품이름 저장하는 리스트
	private HashMap<String, CategoryInfo> categorys;	//카테고리를 저장하는 리스트
	String key, proname, catename, code, ep_value, price;		//HashMap 검색을 위한 스트링, 입력받는 상품명, 입력받는 카테고리명, 생성된 카테고리 코드, 유통기한 설정 값, 가격
	boolean check;						//검색 성공하면 true, 아니면 false
	ScreenClear sc = new ScreenClear();
	
	public ProductRegister(Db db) throws InterruptedException, IOException
	{
		Refund re = new Refund(db);
		this.db = db;
		products = db.getNames();
		categorys = db.getCategorys();
		System.out.print("등록할 상품명을 입력해주세요: ");
		proname = scan.nextLine();
		
		while( (!proname.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) || (proname.length() > 10) )	//상품명 예외처리
		{
			if(re.checkBlank(proname)) 
				System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 한글, 10글자 이하): ");
			proname = scan.nextLine();
		}
		
		set = products.keySet();			//올바른 상품명을 입력받은 경우 이미 있는 이름인지 검색
		it = set.iterator();
		for(int i=0; i<products.size(); i++) 
		{
			key = it.next();
			if(key.equals(proname))	//있는 경우
			{
				System.out.println("이미 존재하는 상품명입니다. 기능을 종료합니다.");
				return;
			}
			else	//없는 경우
				continue;
		}
		
		System.out.print("해당 상품의 카테고리를 입력해주세요: ");	//카테고리명 입력
		catename = scan.nextLine();
		
		while( (!catename.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) || (catename.length() > 10) )	//카테고리명 예외처리
		{
			if(re.checkBlank(catename)) 
				System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 한글, 10글자 이하): ");
			catename = scan.nextLine();
		}
		
		set = categorys.keySet();			//올바른 카테고리명을 입력받은 경우 이미 있는 이름인지 검색
		it = set.iterator();
		for(int i=0; i<categorys.size(); i++) 
		{
			key = it.next();
			if(key.equals(catename))	//있는 경우
			{
				check = true;
				break;
			}
			else	//없는 경우
				check = false;
		}
			
		if(!check)	//없다면 큰 카테고리 생성
		{
			if(categorys.size() < 26*26)	//큰 카테고리 수가 26*26개 보다 적을 때 
			{
				String file_name = String.valueOf((char) ('A' + categorys.size()/26) + String.valueOf((char) ('A' + categorys.size()%26)));
				db.addCategory(catename, file_name );
			}
			else
			{
				System.out.println("더 이상 카테고리를 생성할 수 없습니다.");
				System.out.println("재고 관리로 돌아갑니다.");
				return;
			}
		}
		
		CategoryInfo ci = categorys.get(catename);	//세부 카테고리 생성
		code = ci.getCategory_code();
		int last = ci.getLast_num();
		if(categorys.get(catename).getLast_num() < 26*26)	//세부 카테고리 수가 26*26보다 적을 때
			code += String.valueOf((char) ('A' + categorys.get(catename).getLast_num()/26) + String.valueOf((char) ('A' + categorys.get(catename).getLast_num()%26)));
		else
		{
			System.out.println("해당 카테고리에 더 이상 상품을 등록할 수 없습니다.");
			System.out.println("재고 관리로 돌아갑니다.");
			return;
		}
		
		
		
		System.out.print("해당 상품의 유통기한 설정 값을 입력해주세요: ");	//유통기한 설정 값 입력
		ep_value = scan.nextLine();
		
		while( (!ep_value.matches(".*[0-9]+.*")) || ep_value.length() < 1 || ep_value.length() > 3 || ep_value.substring(0, 1).equals("0"))	//유통기한 설정 값 예외처리
		{
			if(re.checkBlank(ep_value)) 
				System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 숫자, 1자리 이상 3자리 이하): ");
			ep_value = scan.nextLine();
		}
		
		
		System.out.print("해당 상품의 가격을 입력해주세요: ");	//가격 입력
		price = scan.nextLine();
		
		while( (!price.matches(".*[0-9]+.*")) || price.length() < 1 || price.length() > 8  || price.substring(0, 1).equals("0"))	//가격 예외처리
		{
			if(re.checkBlank(price)) 
				System.out.print("잘못된 입력, 다시 입력해주세요(ONLY 숫자, 1자리 이상 8자리 이하): ");
			price = scan.nextLine();
		}
		
		sc.ScreenClear();
		
		System.out.println("상품명: " + proname);			//상품등록 최종 결정
		System.out.println("카테고리명: " + catename);
		System.out.println("유통기한 설정 값: " + ep_value);
		System.out.println("가격: " + price);
		System.out.print("\n해당 상품을 정말로 추가하시겠습니까?(Y/N) ");
		String answer = scan.next();
		
		while(!(answer.equals("Y") || answer.equals("N")))
		{
			System.out.print("잘못된 입력입니다. 다시 입력해주세요(Y/N) ");
			answer = scan.next();
		}
		
		if(answer.equals("Y"))
		{
	
			db.addNames(proname, code, Integer.parseInt(ep_value), Integer.parseInt(price));
			db.addCategory(catename);
			System.out.println("상품등록이 완료되었습니다.");
			System.out.println("재고 관리로 돌아갑니다.");
		}
		else if(answer.equals("N"))
			System.out.println("재고 관리로 돌아갑니다.");
		
	
		
	
	}
	
}
