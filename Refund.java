package sw.pos;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.io.*;

public class Refund {
	private int index;//ȯ�� ������ ��ǰ�� �ε��� ��ȣ�� �޴� ����
	private String PayCode;//���� �ڵ�
	private String product_code;//��ǰ�ڵ�
	private Scanner scan;//��ĳ�� ��ü
	private int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31}; //�������, �������ڸ� ����ϱ� ���ؼ� ���� �迭
	private int today_year; //���� �⵵
	private int today_month; //���� ��
	private int today_day; //���� ��
	private Db date;// ���糯¥�� �������� ���� ������ Db��ü
	private HashMap<String,ArrayList<Product>> Product_list; //�տ��� ��ǰ�ڵ�, �ڿ��� ��ǰ�ڵ� ������ ��ǰ ������
	private FileIO fileio; //���� ������ �а� �����ϱ� ���� ������ FileIO��ü
	private ArrayList<Product> list;
	public Refund() {
		super();
		this.scan = new Scanner(System.in);
		date=new Db();
		Product_list=new HashMap<String, ArrayList<Product>>();
		fileio=new FileIO();
		Product_list=fileio.readPayment();
	}
	
	public void RefundS() { //ȯ�� �����ϴ� �Լ�
		
		do { //���� ���Ŀ� �´� ���ڿ��� �Է¹������� ��¥ �� ���� �Է¹޴� do-while��
			System.out.print("���� �ڵ带 �Է��ϼ���: ");
			PayCode=scan.next();
		}while(!(checkL_PayCode(PayCode)&checkI_PayCode(PayCode)));
		
		String r_possible_Pd=PayCode.substring(0,6);//ȯ�� ������ ��¥���� �Ǵ��� ���� ����-���� ��¥
		int year1=Integer.parseInt(r_possible_Pd.substring(0,2));//���ų�¥ �� �ڿ� ���ڸ�
		int month1=Integer.parseInt(r_possible_Pd.substring(2, 4)); //���ų�¥ ��
		int day1=Integer.parseInt(r_possible_Pd.substring(4, 6)); //���ų�¥ ��
		
		String today=date.getLast_date();//���� ��¥ ������ ����
		today_year=Integer.parseInt(today.substring(2,4));//���糯¥ �� �ڿ� ���ڸ�
		today_month=Integer.parseInt(today.substring(4,6));//���糯¥ ��
		today_day=Integer.parseInt(today.substring(6,8));//���糯¥ ��

		int day_term1=checkD(year1,month1,day1);//���ų�¥�� ���� ��¥ ���� ���
		System.out.println(day_term1);
		if(day_term1>=7) {//���⼭ ���� ��¥�� ���ų�¥ �� 7�� ������ ȯ�� �Ұ� ������ ���θ޴��� ����
			System.out.println("���� �� �������� ������ ������ ȯ���� �Ұ����մϴ�. ���α׷��� �����մϴ�");
			return; //����б�
		}
		while(true){
			list=Product_list.get(PayCode); //Paycode�� key������ �ϴ� value�� �޴� ArrayList<Product>
			System.out.println("          ���� ����Ʈ            ");
			System.out.println("-------------------------");
			for(Product i:list) {
				System.out.print(i);
			}
			System.out.println("      ȯ�� ���� ��ǰ ����Ʈ       ");
			System.out.println("-------------------------");
			
			for(Product i:list) { //list�� ��� ��ǰ���� ����ϴ� for��
				String ep=i.getEpdate(); //�ش� ��ǰ�� ��������� �����ϱ� ���ؼ� Product�� ������� ���� �����ͼ� ���� �ð��� ���Ѵ�.
				int year2=Integer.parseInt(ep.substring(2, 4));//������� �⵵
				int month2=Integer.parseInt(ep.substring(4, 6));//������� ��
				int day2=Integer.parseInt(ep.substring(6, 8));//������� ��
				int day_term2=checkD(year2,month2,day2);//������� üũ
				if(day_term2<30) {//���� ��¥ ���̰� 30�� �̸� ��, ��������� ���� ������ �ʾҴٸ�
					System.out.print(i);//�ش��ǰ������ ���
				}
			}
			//��ǰ ����Ʈ ��� ���� �ֱ�
			System.out.print("ȯ���� ��ǰ �ڵ带 �Է����ּ���(ȯ���� ���߰�ʹٸ� �ϷḦ  �Է����ּ���): "); 
			product_code=scan.next();
			if((checkL_Product_code(product_code))&&(checkC_Product_code(product_code))) {//��ǰ�ڵ� ������Ģ �˻�
				for(Product i:list) {
					if(i.getCode().equals(product_code)) {//��ǰ�ڵ�� ��ġ�ϴ� ��ǰ�� �ε��� ��ȣ ã��
						index=list.indexOf(i);//��ġ�Ѵٸ�, �ش� ��ǰ�� �ε����� ������ �����ߴ� index�� ȣ���Ѵ�
					}
				}
				if(index==-1) { //indexOf�� ���� ã�� ���ߴٸ� -1�� �����ϱ� ������ ����ó��
					System.out.println("ȯ�� ���ɸ���Ʈ�� �������� �ʴ� ��ǰ�ڵ� �Դϴ�.");
					continue;//���� while������ ����
				}else {
					System.out.print(list.get(index).getName()+"��(��) ȯ���Ͻðڽ��ϱ�?: ");
					String YN=scan.next(); //ȯ�ҿ��� YN���� ���� ����
					if(YN.equals("N")) {
						System.out.println("��ǰ�ڵ� �Է����� �Ѿ�ϴ�.");
						continue; //while������ �Ѿ��
					}else if(YN.equals("Y")) {//�ش� ������Ͽ��� ������ ��ǰ ����
						list.remove(index);//�ش� ����Ʈ���� ��ǰ���� ����
						File file=new File("PaymentList.txt");//���� ����
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
								if(size>count)//���������� \n�� �Ѵٸ� �� ĭ�� �׿������� ����⶧���� ������ ��������̶�� \n�� ���ش�.
									bw.write("@"+"\n");
								else
									bw.write("@");
							}
							bw.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//�ؿ� �ΰ��� ���� PaymentList.txt�� ���°� ���Ƽ� �ۼ� ����
						//�����ϰ�� ȯ�� �˰��� �ֱ� �����Ѱ�쿡�� �������� ��������
						//ī���ϰ�� ī��ȯ�� ���� �ֱ�
						System.out.println("ȯ���� �Ϸ�ƽ��ϴ�");
					}else {
						System.out.println("�ùٸ��� ���� �Է��Դϴ�.");
					}
				}
			}else if(product_code.equals("�Ϸ�")) {
				System.out.println("�ϷḦ �Է��߽��ϴ�. ���θ޴��� ���ư��ϴ�.");
				return;//���� �б�
			}
			else {
				continue;
			}
		}
	}
	
	public int checkD(int year, int month, int day) {//��¥ ���� ����ϴ� �Լ�
		int day_term=(today_year-year)*365;//���� ��¥�� �Է¹��� �⵵ ���
		for(int i=1;i<=today_month;i++) { //���� ��¥���� �� �� ���ϱ�
			if(i==today_month)
				day_term+=today_day;
			else
				day_term+=days[i-1];
		}
		for(int i=1;i<=month;i++) {//�Է¹��� ��¥ �� �� ���� ����
			if(i==month)
				day_term-=(day-1);
			else
				day_term-=days[i-1];
		}
		return day_term;
	}
	public boolean checkL_PayCode(String PayCode) {//���� üũ�ϴ� �Լ�
		if(PayCode.length()==9)
			return true;
		else {
			System.out.println("�ùٸ��� ���� ���� �ڵ� �Դϴ�.");
			return false;
		}
	}
	
	public boolean checkI_PayCode(String PayCode) {//�������� üũ�ϴ� �Լ�
		for(char c:PayCode.toCharArray()) {
			if((c>=48)&&(c<=57)) {
				continue;
			}else {
				System.out.println("�ùٸ��� ���� ���� �ڵ� �Դϴ�.");
				return false;
			}
		}
		return true;
	}
	public boolean checkL_Product_code(String Pc) {//��ǰ�ڵ� ������Ģ �Ǻ�-���̰� 6����
		if(Pc.equals("�Ϸ�"))
			return false;
		if(Pc.length()==6)
			return true;
		else {
			System.out.println("�ùٸ��� ���� ��ǰ�ڵ� �Դϴ�.-length");
			return false;
		}
	}
	public boolean checkC_Product_code(String Pc) {//��ǰ�ڵ� ������Ģ �Ǻ��ϴ� �Լ� -���ĺ��빮�ڷ� �̷���� �ִ���
		if(Pc.equals("�Ϸ�"))
			return false;
		for(char c:Pc.toCharArray()) {
			if((c>=65)&&(c<=90))
				continue;
			else {
				System.out.println("�ùٸ��� ���� ��ǰ�ڵ� �Դϴ�.-non-alphabet");
				return false;
			}
		}
		return true;
	}
}

