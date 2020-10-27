package sw.pos;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.io.*;

public class Refund {
	private int index;
	private String PayCode;//결제 코드
	private String product_code;//상품코드
	private Scanner scan;
	private int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	private int today_year; //현재 년도
	private int today_month; //현재날짜 달
	private int today_day; //현재 날짜 일
	private Db date;
	private HashMap<String,ArrayList<Product>> Product_list; //앞에는 상품코드, 뒤에는 상품코드 포함한 상품 정보들
	private FileIO fileio;
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
		//구매날짜와 현재 날짜 차이 계산
		int day_term1=checkD(year1,month1,day1);
		if(day_term1>=7) {//여기서 현재 날짜와 구매날짜 비교 7일 넘을시 환불 불가 판정후 메인메뉴로 복귀
			System.out.println("구입 후 일주일이 지났기 때문에 환불이 불가능합니다. 프로그램을 종료합니다");
			return; //종료분기
		}
		while(true){
			ArrayList<Product> list=Product_list.get(PayCode); //상품 리스트들
			System.out.println("      환불 가능 상품 리스트       ");
			System.out.println("-------------------------");
			for(Product i:list) {
				String ep=i.getEpdate();
				int year2=Integer.parseInt(ep.substring(2, 4));
				int month2=Integer.parseInt(ep.substring(4, 6));
				int day2=Integer.parseInt(ep.substring(6, 8));
				int day_term2=checkD(year2,month2,day2);
				if(day_term2<30) {
					System.out.print(i);
				}
			}
			//상품 리스트 출력 구문 넣기
			System.out.print("환불한 상품 코드를 입력해주세요(환불을 멈추고싶다면 완료를  입력해주세요): ");
			product_code=scan.next();
			if((checkL_Product_code(product_code))&&(checkC_Product_code(product_code))) {
				for(Product i:list) {
					if(i.getCode().equals(product_code)) {
						index=list.indexOf(i);
					}
				}
				if(index==-1) {
					System.out.println("환불 가능리스트에 존재하지 않는 상품코드 입니다.");
					continue;
				}else {
					System.out.print(list.get(index).getName()+"을(를) 환불하시겠습니까?: ");
					String YN=scan.next();
					if(YN.equals("N")) {
						System.out.println("상품코드 입력으로 넘어갑니다.");
						continue;
					}else if(YN.equals("Y")) {//해당 결제목록에서 결제한 상품 삭제
						list.remove(index);
						File file=new File("PaymentList.txt");
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
								if(size>count)
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

