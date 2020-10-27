package sw.pos;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.io.*;

public class Refund {
	private int index;//환불 가능한 상품의 인덱스 번호를 받는 변수
	private String PayCode;//결제 코드
	private String product_code;//상품코드
	private Scanner scan;//스캐너 객체
	private int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //유통기한, 결제일자를 계산하기 위해서 만든 배열
	private int today_year; //현재 년도
	private int today_month; //현재 달
	private int today_day; //현재 일
	private Db date;// 현재날짜를 가져오기 위해 선언한 Db객체
	private HashMap<String,ArrayList<Product>> Product_list; //앞에는 상품코드, 뒤에는 상품코드 포함한 상품 정보들
	private FileIO fileio; //결제 파일을 읽고 수정하기 위해 선언한 FileIO객체
	private ArrayList<Product> list;
	public Refund() {
		super();
		this.scan = new Scanner(System.in);
		date=new Db();
		Product_list=new HashMap<String, ArrayList<Product>>();
		fileio=new FileIO();
		Product_list=fileio.readPayment();
	}
	
	public void RefundS() { //환불 진행하는 함수
		
		do { //문법 형식에 맞는 문자열을 입력받을구입 날짜 때 까지 입력받는 do-while문
			System.out.print("결제 코드를 입력하세요: ");
			PayCode=scan.next();
		}while(!(checkL_PayCode(PayCode)&checkI_PayCode(PayCode)));
		
		String r_possible_Pd=PayCode.substring(0,6);//환불 가능한 날짜인지 판단을 위한 추출-결제 날짜
		int year1=Integer.parseInt(r_possible_Pd.substring(0,2));//구매날짜 년 뒤에 두자리
		int month1=Integer.parseInt(r_possible_Pd.substring(2, 4)); //구매날짜 월
		int day1=Integer.parseInt(r_possible_Pd.substring(4, 6)); //구매날짜 일
		
		String today=date.getLast_date();//현재 날짜 가지고 오기
		today_year=Integer.parseInt(today.substring(2,4));//현재날짜 년 뒤에 두자리
		today_month=Integer.parseInt(today.substring(4,6));//현재날짜 월
		today_day=Integer.parseInt(today.substring(6,8));//현재날짜 일

		int day_term1=checkD(year1,month1,day1);//구매날짜와 현재 날짜 차이 계산
		System.out.println(day_term1);
		if(day_term1>=7) {//여기서 현재 날짜와 구매날짜 비교 7일 넘을시 환불 불가 판정후 메인메뉴로 복귀
			System.out.println("구입 후 일주일이 지났기 때문에 환불이 불가능합니다. 프로그램을 종료합니다");
			return; //종료분기
		}
		while(true){
			list=Product_list.get(PayCode); //Paycode를 key값으로 하는 value를 받는 ArrayList<Product>
			System.out.println("          결제 리스트            ");
			System.out.println("-------------------------");
			for(Product i:list) {
				System.out.print(i);
			}
			System.out.println("      환불 가능 상품 리스트       ");
			System.out.println("-------------------------");
			
			for(Product i:list) { //list의 모든 상품들을 출력하는 for문
				String ep=i.getEpdate(); //해당 상품의 유통기한을 점검하기 위해서 Product의 유통기한 값을 가져와서 현재 시간과 비교한다.
				int year2=Integer.parseInt(ep.substring(2, 4));//유통기한 년도
				int month2=Integer.parseInt(ep.substring(4, 6));//유통기한 월
				int day2=Integer.parseInt(ep.substring(6, 8));//유통기한 일
				int day_term2=checkD(year2,month2,day2);//유통기한 체크
				if(day_term2<30) {//만약 날짜 차이가 30일 미만 즉, 유통기한이 아직 지나지 않았다면
					System.out.print(i);//해당상품정보를 출력
				}
			}
			//상품 리스트 출력 구문 넣기
			System.out.print("환불한 상품 코드를 입력해주세요(환불을 멈추고싶다면 완료를  입력해주세요): "); 
			product_code=scan.next();
			if((checkL_Product_code(product_code))&&(checkC_Product_code(product_code))) {//상품코드 문법규칙 검사
				for(Product i:list) {
					if(i.getCode().equals(product_code)) {//상품코드와 일치하는 상품의 인덱스 번호 찾기
						index=list.indexOf(i);//일치한다면, 해당 상품의 인덱스를 위에서 선언했던 index에 호출한다
					}
				}
				if(index==-1) { //indexOf는 값을 찾지 못했다면 -1을 리턴하기 때문에 예외처리
					System.out.println("환불 가능리스트에 존재하지 않는 상품코드 입니다.");
					continue;//다음 while문으로 진행
				}else {
					System.out.print(list.get(index).getName()+"을(를) 환불하시겠습니까?: ");
					String YN=scan.next(); //환불여부 YN으로 재차 묻기
					if(YN.equals("N")) {
						System.out.println("상품코드 입력으로 넘어갑니다.");
						continue; //while문으로 넘어가기
					}else if(YN.equals("Y")) {//해당 결제목록에서 결제한 상품 삭제
						list.remove(index);//해당 리스트에서 상품정보 삭제
						File file=new File("PaymentList.txt");//파일 선언
						FileWriter fw=null;
						BufferedWriter bw=null;
						try {
							fw=new FileWriter(file);
							bw=new BufferedWriter(fw);
							bw.write("");
							Set<String> set=Product_list.keySet();
							int size=set.size();
							int count=0;
							Iterator<String> iterator=set.iterator();
							while(iterator.hasNext()) {
								String key=(String)iterator.next();
								bw.write(key+"\r\n");
								for(int i=0;i<Product_list.get(key).size();i++) {
									Product product=Product_list.get(key).get(i);
									bw.write(product.toString());
								}
								count++;
								if(size>count)//마지막에도 \n을 한다면 한 칸의 잉여공간이 생기기때문에 마지막 결제목록이라면 \n을 빼준다.
									bw.write("@"+"\n");
								else
									bw.write("@");
							}
							bw.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//밑에 두개는 아직 PaymentList.txt에 없는거 같아서 작성 안함
						//현금일경우 환불 알고리즘 넣기 부족한경우에는 현금충전 가져오기
						//카드일경우 카드환불 문구 넣기
						System.out.println("환불이 완료됐습니다");
					}else {
						System.out.println("올바르지 않은 입력입니다.");
					}
				}
			}else if(product_code.equals("완료")) {
				System.out.println("완료를 입력했습니다. 메인메뉴로 돌아갑니다.");
				return;//종료 분기
			}
			else {
				continue;
			}
		}
	}
	
	public int checkD(int year, int month, int day) {//날짜 차이 계산하는 함수
		int day_term=(today_year-year)*365;//현재 날짜와 입력받은 년도 계산
		for(int i=1;i<=today_month;i++) { //현재 날짜까지 일 수 더하기
			if(i==today_month)
				day_term+=today_day;
			else
				day_term+=days[i-1];
		}
		for(int i=1;i<=month;i++) {//입력받은 날짜 일 수 까지 빼기
			if(i==month)
				day_term-=(day-1);
			else
				day_term-=days[i-1];
		}
		return day_term;
	}
	public boolean checkL_PayCode(String PayCode) {//길이 체크하는 함수
		if(PayCode.length()==9)
			return true;
		else {
			System.out.println("올바르지 않은 결제 코드 입니다.");
			return false;
		}
	}
	
	public boolean checkI_PayCode(String PayCode) {//숫자인지 체크하는 함수
		for(char c:PayCode.toCharArray()) {
			if((c>=48)&&(c<=57)) {
				continue;
			}else {
				System.out.println("올바르지 않은 결제 코드 입니다.");
				return false;
			}
		}
		return true;
	}
	public boolean checkL_Product_code(String Pc) {//상품코드 문법규칙 판별-길이가 6인지
		if(Pc.equals("완료"))
			return false;
		if(Pc.length()==6)
			return true;
		else {
			System.out.println("올바르지 않은 상품코드 입니다.-length");
			return false;
		}
	}
	public boolean checkC_Product_code(String Pc) {//상품코드 문법규칙 판별하는 함수 -알파벳대문자로 이루어져 있는지
		if(Pc.equals("완료"))
			return false;
		for(char c:Pc.toCharArray()) {
			if((c>=65)&&(c<=90))
				continue;
			else {
				System.out.println("올바르지 않은 상품코드 입니다.-non-alphabet");
				return false;
			}
		}
		return true;
	}
}

