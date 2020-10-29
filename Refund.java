package sw.pos;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;

public class Refund {
	private int index;// ȯ�� ������ ��ǰ�� �ε��� ��ȣ�� �޴� ����
	private String PayCode;// ���� �ڵ�
	private String product_code;// ��ǰ�ڵ�
	private Scanner scan;// ��ĳ�� ��ü
	private int[] days = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 }; // �������, �������ڸ� ����ϱ� ���ؼ� ���� �迭
	private int today_year; // ���� �⵵
	private int today_month; // ���� ��
	private int today_day; // ���� ��
	private Db date;// ���糯¥�� �������� ���� ������ Db��ü
	private HashMap<String, ArrayList<Product>> Product_list; // �տ��� ��ǰ�ڵ�, �ڿ��� ��ǰ�ڵ� ������ ��ǰ ������
	private ArrayList<Product> list;
	private CashManager cm;
	String isCashCharge; // �������� ���� üũ�ϱ� ���� ����

	public Refund(Db date) {
		this.scan = new Scanner(System.in);
		this.date = date;
		Product_list = new HashMap<String, ArrayList<Product>>();
		Product_list = date.getPayments();
		cm = new CashManager(date);
		this.isCashCharge = "N";
	}

	public void RefundS() { // ȯ�� �����ϴ� �Լ�

		do { // ���� ���Ŀ� �´� ���ڿ��� �Է¹������� ��¥ �� ���� �Է¹޴� do-while��
			System.out.print("���� �ڵ带 �Է��ϼ���: ");
			PayCode = scan.nextLine();
		} while (!(checkL_PayCode(PayCode) & checkI_PayCode(PayCode) & checkBlank(PayCode) & checkR_PayCode(PayCode)));

		String r_possible_Pd = PayCode.substring(0, 6);// ȯ�� ������ ��¥���� �Ǵ��� ���� ����-���� ��¥
		int year1 = Integer.parseInt(r_possible_Pd.substring(0, 2));// ���ų�¥ �� �ڿ� ���ڸ�
		int month1 = Integer.parseInt(r_possible_Pd.substring(2, 4)); // ���ų�¥ ��
		int day1 = Integer.parseInt(r_possible_Pd.substring(4, 6)); // ���ų�¥ ��

		String today = date.getLast_date();// ���� ��¥ ������ ����
		today_year = Integer.parseInt(today.substring(2, 4));// ���糯¥ �� �ڿ� ���ڸ�
		today_month = Integer.parseInt(today.substring(4, 6));// ���糯¥ ��
		today_day = Integer.parseInt(today.substring(6, 8));// ���糯¥ ��

		int day_term1 = checkD(year1, month1, day1);// ���ų�¥�� ���� ��¥ ���� ���
		if (day_term1 >= 7) {// ���⼭ ���� ��¥�� ���ų�¥ �� 7�� ������ ȯ�� �Ұ� ������ ���θ޴��� ����
			System.out.println("���� �� �������� ������ ������ ȯ���� �Ұ����մϴ�.\n���θ޴��� ���ư��ϴ�.");
			return; // ����б�
		}
		int count = 0;// while���������� �ϱ� ���� ���� ����
		while (true) {
			list = Product_list.get(PayCode); // Paycode�� key������ �ϴ� value�� �޴� ArrayList<Product>
			System.out.println("          ���� ����Ʈ            ");
			System.out.println("-------------------------");
			for (Product i : list) {
				System.out.print(i);
			}
			System.out.println("      ȯ�� ���� ��ǰ ����Ʈ       ");
			System.out.println("-------------------------");

			for (Product i : list) { // list�� ��� ��ǰ���� ����ϴ� for��
				String ep = i.getEpdate(); // �ش� ��ǰ�� ��������� �����ϱ� ���ؼ� Product�� ������� ���� �����ͼ� ���� �ð��� ���Ѵ�.
				int day_term2 = ep_date(ep);// ������� üũ
				if (day_term2 < 0) {// ���� ��¥ ���̰� 30�� �̸� ��, ��������� ���� ������ �ʾҴٸ�
					System.out.print(i);// �ش��ǰ������ ���
				}
			}
			// ��ǰ ����Ʈ ��� ���� �ֱ�
			System.out.print("ȯ���� ��ǰ �ڵ带 �Է����ּ���(ȯ���� ���߰�ʹٸ� �ϷḦ  �Է����ּ���): ");
			product_code = scan.nextLine();
			if ((checkL_Product_code(product_code)) & (checkC_Product_code(product_code) & checkBlank(product_code))) {// ��ǰ�ڵ�																														// ������Ģ																														// �˻�
				for (Product i : list) {
					String ep = i.getEpdate();
					int day_term = ep_date(ep);
					if ((i.getCode().equals(product_code)) & (day_term < 0)) {// ��ǰ�ڵ�� ��ġ�ϴ� ��ǰ�� �ε��� ��ȣ ã��
						index = list.indexOf(i);// ��ġ�Ѵٸ�, �ش� ��ǰ�� �ε����� ������ �����ߴ� index�� ȣ���Ѵ�
						break;
					} else
						index = -1;
				}
				if (index == -1) { // indexOf�� ���� ã�� ���ߴٸ� -1�� �����ϱ� ������ ����ó��
					System.out.println("ȯ�� ���ɸ���Ʈ�� �������� �ʴ� ��ǰ�ڵ� �Դϴ�.");
					continue;// ���� while������ ����
				} else {
					while (true) {
						System.out.print(list.get(index).getName() + "��(��) ȯ���Ͻðڽ��ϱ�?(Y/N): ");
						String YN = scan.nextLine(); // ȯ�ҿ��� YN���� ���� ����
						if ((checkL_YN(YN)) & (checkC_YN(YN)) & (checkBlank(YN))) {
							if (YN.equals("N")) {
								System.out.println("��ǰ�ڵ� �Է����� �Ѿ�ϴ�.");
								break;
							} else {// �ش� // ������Ͽ��� // ������ // ��ǰ ����
								int P_price = list.get(index).getPrice();// ��ǰ ���� ��������
								if (list.get(index).getIsPayByCash()) {// ���� ������ ���
									System.out.println(P_price + "���� ���� ȯ���� �����մϴ�.\n......\n");
									for (int i = 7; i >= 0; i--) {
										int mok = P_price / cm.getKeyindex(i);
										if (mok == 0)
											continue;
										else {
											P_price -= cm.getKeyindex(i) * mok;
											if (date.getCash().get(cm.getKeyindex(i)) < mok) {
												System.out.print("�ܵ��� �����մϴ�." + cm.getKeyindex(i) + "���� "
														+ (mok - date.getCash().get(cm.getKeyindex(i)))
														+ "�� �� �ʿ��մϴ�. ���� ������ �����Ͻðڽ��ϱ�? : ");
												String a = scan.next();
												setIsCashCharge(a);
												if (isCashCharge.equals("Y")) {
													cm.ManageCash();
												} else if (isCashCharge.equals("N")) {
													System.out.println("��ǰ �ڵ� �Է� �κ����� �Ѿ�ϴ�.");
													break;
												} else {
													System.out.println("�ùٸ��� ���� �Է��Դϴ�.");
												}
											} else {
												setIsCashCharge("Y");
												date.setCash(cm.getKeyindex(i), mok, true);
											}
										}

									}
								} else // ī�� ������ ���
									System.out.println(P_price + "���� ī�� ȯ���� �����մϴ�\n....\n");
								if (isCashCharge.equals("Y") && YN.equals("Y") && (P_price == 0)) {
									list.remove(index);
									date.removePayment(PayCode, product_code);
									System.out.println("ȯ���� �Ϸ�ƽ��ϴ�");
								}
								count = 1;
								break;
							}
						}
						else {
							System.out.println("ȯ�ҿ����������� �ǵ��ư��ϴ�.");
							continue;
						}
					}
				}
			} else if (product_code.equals("�Ϸ�")) {
				System.out.println("�ϷḦ �Է��߽��ϴ�. ���θ޴��� ���ư��ϴ�.");
				return;// ���� �б�
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

	public int checkD(int year, int month, int day) {// ��¥ ���� ����ϴ� �Լ�
		int day_term = (today_year - year) * 365;// ���� ��¥�� �Է¹��� �⵵ ���
		for (int i = 1; i <= today_month; i++) { // ���� ��¥���� �� �� ���ϱ�
			if (i == today_month)
				day_term += today_day;
			else
				day_term += days[i - 1];
		}
		for (int i = 1; i <= month; i++) {// �Է¹��� ��¥ �� �� ���� ����
			if (i == month)
				day_term -= (day - 1);
			else
				day_term -= days[i - 1];
		}
		return day_term;
	}

	public boolean checkL_PayCode(String PayCode) {// ���� üũ�ϴ� �Լ�
		if (PayCode.length() == 9)
			return true;
		else if (PayCode.length() > 9) {
			System.out.println("�߸��� �Է� �Դϴ�-���̰� 9 �ʰ��Դϴ�");
			return false;
		} else {
			System.out.println("�߸��� �Է��Դϴ�-���̰� 9 �̸��Դϴ�.");
			return false;
		}
	}

	public boolean checkI_PayCode(String PayCode) {// �������� üũ�ϴ� �Լ�
		for (char c : PayCode.toCharArray()) {
			if ((c >= 48) && (c <= 57)) {
				continue;
			} else {
				System.out.println("�߸��� �Է� �Դϴ�-���ڰ� �ƴѰ� ����ֽ��ϴ�");
				return false;
			}
		}
		return true;
	}

	public boolean checkBlank(String PayCode) {// ���� ���� üũ
		for (int i = 0; i < PayCode.length(); i++) {
			if (PayCode.charAt(i) == ' ') {
				System.out.println("�߸��� �Է� �Դϴ�-������ ����ֽ��ϴ�");
				return false;
			}
		}
		return true;
	}

	public boolean checkR_PayCode(String PayCode) { // ������ �����ϴ� �����ڵ����� üũ
		String NonBPC = new String(PayCode);
		NonBPC = NonBPC.replace(" ", "");// ���� üũ�� ���ؼ� ���� üũ
		for (String key : Product_list.keySet()) {
			if (key.equals(NonBPC))
				return true;
		}
		System.out.println("�߸��� �Է��Դϴ�-�������� �ʴ� �����ڵ� �Դϴ�.");
		return false;
	}

	public boolean checkL_Product_code(String Pc) {// ��ǰ�ڵ� ������Ģ �Ǻ�-���̰� 6����
		if (Pc.equals("�Ϸ�"))
			return false;
		if (Pc.length() == 6)
			return true;
		else if (Pc.length() > 6) {
			System.out.println("���߸��� �Է��Դϴ�-���̰� 6 �ʰ��Դϴ�");
			return false;
		} else {
			System.out.println("�߸��� �Է��Դϴ�-���̰� 6 �̸��Դϴ�");
			return false;
		}
	}

	public boolean checkC_Product_code(String Pc) {// ��ǰ�ڵ� ������Ģ �Ǻ��ϴ� �Լ� -���ĺ��빮�ڷ� �̷���� �ִ���
		if (Pc.equals("�Ϸ�"))
			return false;
		for (char c : Pc.toCharArray()) {
			if ((c >= 65) && (c <= 90))
				continue;
			else {
				System.out.println("�߸��� �Է��Դϴ�-�빮�� ���ĺ��� �ƴѰ� ����ֽ��ϴ�");
				return false;
			}
		}
		return true;
	}

	public int ep_date(String ep) {// ������� �����¥ üũ
		int year2 = Integer.parseInt(ep.substring(2, 4));// ������� �⵵
		int month2 = Integer.parseInt(ep.substring(4, 6));// ������� ��
		int day2 = Integer.parseInt(ep.substring(6, 8));// ������� ��
		int epdate = checkD(year2, month2, day2);// ������� üũ
		return epdate;
	}

	public boolean checkL_YN(String YN) {
		if (YN.length() == 1) {
			return true;
		}
		if (YN.length() > 1)
			System.out.println("�߸��� �Է� �Դϴ�-���̰� 1 �ʰ��Դϴ�");
		else
			System.out.println("�߸��� �Է� �Դϴ�-���̰� 1 �̸��Դϴ�");
		return false;
	}

	public boolean checkC_YN(String YN) {
		for (char c : YN.toCharArray()) {
			if ((c == 78) || (c == 89)) {
				continue;
			} else {
				System.out.println("�߸��� �Է��Դϴ�-YN�ܿ� �ٸ� ���ڰ� �ֽ��ϴ�");
				return false;
			}
		}
		return true;
	}
}