package sw.pos;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Refund {
	private int index;// 환불 가능한 상품의 인덱스 번호를 받는 변수
	private String PayCode;// 결제 코드
	private String product_code;// 상품코드
	private Scanner scan;// 스캐너 객체
	private int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }; // 유통기한, 결제일자를 계산하기 위해서 만든 배열
	private int today_year; // 현재 년도
	private int today_month; // 현재 달
	private int today_day; // 현재 일
	private Db date;// 현재날짜를 가져오기 위해 선언한 Db객체
	private HashMap<String, ArrayList<Product>> Product_list; // 앞에는 상품코드, 뒤에는 상품코드 포함한 상품 정보들
	private ArrayList<Product> list;
	private CashManager cm;
	String isCashCharge; // 현금충전 여부 체크하기 위한 변수

	public Refund(Db date) {
		this.scan = new Scanner(System.in);
		this.date = date;
		Product_list = new HashMap<String, ArrayList<Product>>();
		Product_list = date.getPayments();
		cm = new CashManager(date);
		this.isCashCharge = "N";
	}

	public void RefundS() { // 환불 진행하는 함수

		do { // 문법 형식에 맞는 문자열을 입력받을구입 날짜 때 까지 입력받는 do-while문
			System.out.print("결제 코드를 입력하세요: ");
			PayCode = scan.nextLine();
		} while (!(checkL_PayCode(PayCode) & checkI_PayCode(PayCode) & checkBlank(PayCode) & checkR_PayCode(PayCode)));

		String r_possible_Pd = PayCode.substring(0, 6);// 환불 가능한 날짜인지 판단을 위한 추출-결제 날짜
		int year1 = Integer.parseInt(r_possible_Pd.substring(0, 2));// 구매날짜 년 뒤에 두자리
		int month1 = Integer.parseInt(r_possible_Pd.substring(2, 4)); // 구매날짜 월
		int day1 = Integer.parseInt(r_possible_Pd.substring(4, 6)); // 구매날짜 일

		String today = date.getLast_date();// 현재 날짜 가지고 오기
		today_year = Integer.parseInt(today.substring(2, 4));// 현재날짜 년 뒤에 두자리
		today_month = Integer.parseInt(today.substring(4, 6));// 현재날짜 월
		today_day = Integer.parseInt(today.substring(6, 8));// 현재날짜 일

		int day_term1 = checkD(year1, month1, day1);// 구매날짜와 현재 날짜 차이 계산
		if (day_term1 >= 7) {// 여기서 현재 날짜와 구매날짜 비교 7일 넘을시 환불 불가 판정후 메인메뉴로 복귀
			System.out.println("구입 후 일주일이 지났기 때문에 환불이 불가능합니다.\n메인메뉴로 돌아갑니다.");
			return; // 종료분기
		}
		int count = 0;// while빠져나오게 하기 위해 만든 변수
		while (true) {
			list = Product_list.get(PayCode); // Paycode를 key값으로 하는 value를 받는 ArrayList<Product>
			System.out.println("          결제 리스트            ");
			System.out.println("-------------------------");
			for (Product i : list) {
				System.out.print(i);
			}
			System.out.println("      환불 가능 상품 리스트       ");
			System.out.println("-------------------------");

			for (Product i : list) { // list의 모든 상품들을 출력하는 for문
				String ep = i.getEpdate(); // 해당 상품의 유통기한을 점검하기 위해서 Product의 유통기한 값을 가져와서 현재 시간과 비교한다.
				int day_term2 = ep_date(ep);// 유통기한 체크
				if (day_term2 < 0) {// 만약 날짜 차이가 30일 미만 즉, 유통기한이 아직 지나지 않았다면
					System.out.print(i);// 해당상품정보를 출력
				}
			}
			// 상품 리스트 출력 구문 넣기
			System.out.print("환불한 상품 코드를 입력해주세요(환불을 멈추고싶다면 완료를  입력해주세요): ");
			product_code = scan.nextLine();
			if ((checkL_Product_code(product_code)) & (checkC_Product_code(product_code) & checkBlank(product_code))) {// 상품코드																														// 문법규칙																														// 검사
				for (Product i : list) {
					String ep = i.getEpdate();
					int day_term = ep_date(ep);
					if ((i.getCode().equals(product_code)) & (day_term < 0)) {// 상품코드와 일치하는 상품의 인덱스 번호 찾기
						index = list.indexOf(i);// 일치한다면, 해당 상품의 인덱스를 위에서 선언했던 index에 호출한다
						break;
					} else
						index = -1;
				}
				if (index == -1) { // indexOf는 값을 찾지 못했다면 -1을 리턴하기 때문에 예외처리
					System.out.println("환불 가능리스트에 존재하지 않는 상품코드 입니다.");
					continue;// 다음 while문으로 진행
				} else {
					while (true) {
						System.out.print(list.get(index).getName() + "을(를) 환불하시겠습니까?(Y/N): ");
						String YN = scan.nextLine(); // 환불여부 YN으로 재차 묻기
						if ((checkL_YN(YN)) & (checkC_YN(YN)) & (checkBlank(YN))) {
							if (YN.equals("N")) {
								System.out.println("상품코드 입력으로 넘어갑니다.");
								break;
							} else {// 해당 // 결제목록에서 // 결제한 // 상품 삭제
								int P_price = list.get(index).getPrice();// 상품 가격 가져오기
								if (list.get(index).getIsPayByCash()) {// 현금 결제인 경우
									System.out.println(P_price + "원의 현금 환불을 진행합니다.\n......\n");
									for (int i = 7; i >= 0; i--) {
										int mok = P_price / cm.getKeyindex(i);
										if (mok == 0)
											continue;
										else {
											P_price -= cm.getKeyindex(i) * mok;
											if (date.getCash().get(cm.getKeyindex(i)) < mok) {
												System.out.print("잔돈이 부족합니다." + cm.getKeyindex(i) + "원이 "
														+ (mok - date.getCash().get(cm.getKeyindex(i)))
														+ "개 더 필요합니다. 현금 충전을 진행하시겠습니까? : ");
												String a = scan.next();
												setIsCashCharge(a);
												if (isCashCharge.equals("Y")) {
													cm.ManageCash();
												} else if (isCashCharge.equals("N")) {
													System.out.println("상품 코드 입력 부분으로 넘어갑니다.");
													break;
												} else {
													System.out.println("올바르지 않은 입력입니다.");
												}
											} else {
												setIsCashCharge("Y");
												date.setCash(cm.getKeyindex(i), mok, true);
											}
										}

									}
								} else // 카드 결제인 경우
									System.out.println(P_price + "원의 카드 환불을 진행합니다\n....\n");
								if (isCashCharge.equals("Y") && YN.equals("Y") && (P_price == 0)) {
									list.remove(index);
									date.removePayment(PayCode, product_code);
									System.out.println("환불이 완료됐습니다");
								}
								count = 1;
								break;
							}
						}
						else {
							System.out.println("환불여부질문으로 되돌아갑니다.");
							continue;
						}
					}
				}
			} else if (product_code.equals("완료")) {
				System.out.println("완료를 입력했습니다. 메인메뉴로 돌아갑니다.");
				return;// 종료 분기
			} else {
				continue;
			}
			if (count != 0) {
				count = 0;
				break;
			}
		}
	}

	public void setIsCashCharge(String isCashCharge) {
		this.isCashCharge = isCashCharge;
	}

	public int checkD(int year, int month, int day) {// 날짜 차이 계산하는 함수
		int day_term = (today_year - year) * 365;// 현재 날짜와 입력받은 년도 계산
		for (int i = 1; i <= today_month; i++) { // 현재 날짜까지 일 수 더하기
			if (i == today_month)
				day_term += today_day;
			else
				day_term += days[i - 1];
		}
		for (int i = 1; i <= month; i++) {// 입력받은 날짜 일 수 까지 빼기
			if (i == month)
				day_term -= (day - 1);
			else
				day_term -= days[i - 1];
		}
		return day_term;
	}

	public boolean checkL_PayCode(String PayCode) {// 길이 체크하는 함수
		if (PayCode.length() == 9)
			return true;
		else if (PayCode.length() > 9) {
			System.out.println("잘못된 입력 입니다-길이가 9 초과입니다");
			return false;
		} else {
			System.out.println("잘못된 입력입니다-길이가 9 미만입니다.");
			return false;
		}
	}

	public boolean checkI_PayCode(String PayCode) {// 숫자인지 체크하는 함수
		for (char c : PayCode.toCharArray()) {
			if ((c >= 48) && (c <= 57)) {
				continue;
			} else {
				System.out.println("잘못된 입력 입니다-숫자가 아닌게 들어있습니다");
				return false;
			}
		}
		return true;
	}

	public boolean checkBlank(String PayCode) {// 선후 공백 체크
		for (int i = 0; i < PayCode.length(); i++) {
			if (PayCode.charAt(i) == ' ') {
				System.out.println("잘못된 입력 입니다-공백이 들어있습니다");
				return false;
			}
		}
		return true;
	}

	public boolean checkR_PayCode(String PayCode) { // 실제로 존재하는 결제코드인지 체크
		String NonBPC = new String(PayCode);
		NonBPC = NonBPC.replace(" ", "");// 실제 체크를 위해서 공백 체크
		for (String key : Product_list.keySet()) {
			if (key.equals(NonBPC))
				return true;
		}
		System.out.println("잘못된 입력입니다-존재하지 않는 결제코드 입니다.");
		return false;
	}

	public boolean checkL_Product_code(String Pc) {// 상품코드 문법규칙 판별-길이가 6인지
		if (Pc.equals("완료"))
			return false;
		if (Pc.length() == 6)
			return true;
		else if (Pc.length() > 6) {
			System.out.println("올잘못된 입력입니다-길이가 6 초과입니다");
			return false;
		} else {
			System.out.println("잘못된 입력입니다-길이가 6 미만입니다");
			return false;
		}
	}

	public boolean checkC_Product_code(String Pc) {// 상품코드 문법규칙 판별하는 함수 -알파벳대문자로 이루어져 있는지
		if (Pc.equals("완료"))
			return false;
		for (char c : Pc.toCharArray()) {
			if ((c >= 65) && (c <= 90))
				continue;
			else {
				System.out.println("잘못된 입력입니다-대문자 알파벳이 아닌게 들어있습니다");
				return false;
			}
		}
		return true;
	}

	public int ep_date(String ep) {// 유통기한 경과날짜 체크
		int year2 = Integer.parseInt(ep.substring(2, 4));// 유통기한 년도
		int month2 = Integer.parseInt(ep.substring(4, 6));// 유통기한 월
		int day2 = Integer.parseInt(ep.substring(6, 8));// 유통기한 일
		int epdate = checkD(year2, month2, day2);// 유통기한 체크
		return epdate;
	}

	public boolean checkL_YN(String YN) {
		if (YN.length() == 1) {
			return true;
		}
		if (YN.length() > 1)
			System.out.println("잘못된 입력 입니다-길이가 1 초과입니다");
		else
			System.out.println("잘못된 입력 입니다-길이가 1 미만입니다");
		return false;
	}

	public boolean checkC_YN(String YN) {
		for (char c : YN.toCharArray()) {
			if ((c == 78) || (c == 89)) {
				continue;
			} else {
				System.out.println("잘못된 입력입니다-YN외에 다른 문자가 있습니다");
				return false;
			}
		}
		return true;
	}
}