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
	Set<String> set; 														//Ű ���� �����ϴ� set
	Iterator<String> it; 													//set �˻��� ���� iterator
	String key, proname;													//Set���� �˻��� ���� ��Ʈ�� ����, ��ǰ�̸�
	String catecode, procode, today, ep_date, price;						//��ǰ�� ī�װ� �ڵ�, ��ǰ�ڵ�, ���� ��¥, �������, ����
	int procount, last_num, epd_value;   									//�߰��� ���������� ����, ������� ���� ��
	boolean check;															//�˻� ���� ���� Ȯ��
	private HashMap<String, NameInfo> products;								//��ǰ�̸� �����ϴ� ����Ʈ
	
	public AddInventory(Db db)
	{
		this.db = db;
		products = db.getNames();	
		today = db.getLast_date();
		System.out.print("�߰��� ��ǰ�� �̸��� �Է����ּ���: ");
		proname = scan.next();
		
		while( (!proname.matches(".*[��-����-�Ӱ�-�R]+.*")) || (proname.length() > 10) )	//��ǰ�� ����ó��
		{
			System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY �ѱ�, 10���� ����): ");
			proname = scan.next();
		}
		
		set = products.keySet();			//�ùٸ� ��ǰ���� �Է¹��� ��� �̹� �ִ� �̸����� �˻�
		it = set.iterator();
		for(int i=0; i<products.size(); i++) 
		{
			key = it.next();
			if(key.equals(proname))	//�ִ� ���
			{
				check = true;
				break;
			}
			else	//���� ���
				check = false;
		}
		
		if(!check)		//�˻� ����
		{
			System.out.println("�������� �ʴ� ��ǰ�Դϴ�.");
			return;
		}
		else			//�˻� ����
		{
			catecode = products.get(proname).getName_code();
			last_num = products.get(proname).getLast_num();
			epd_value = products.get(proname).getEpd_value();
			price = Integer.toString(products.get(proname).getPrice());
			ep_date = FindEp_date(today, epd_value);
		}
		
		if(products.get(proname).getLast_num() >= 26*26)		//�ش� ��ǰ�� ��� �� �� ���
		{
			System.out.println("�ش� ��ǰ�� �� �̻� ��� �߰��� �� �����ϴ�.");
			System.out.println("��� ������ ���ư��ϴ�.");
			return;
		}
			
		
		System.out.print("�߰��� ������ �Է����ּ���: ");
		String temp = scan.next();
		
		while( (!temp.matches(".*[0-9]+.*")) || (temp.length() > 3) )	//�߰� ���� ����ó��
		{
			System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY ����, 3���� ����): ");
			temp = scan.next();
		}	
		procount = Integer.parseInt(temp);
		if(procount > 676)					//676�̻��̸� 676���� ����
			procount = 676;
		if(procount + last_num > 676)		//���� ����� ���� + �߰��� ���� > 676�϶�
			procount -= last_num;
	
		for(int i = 0; i < procount; i++)
		{
			procode = catecode + String.valueOf((char) ('A' + products.get(proname).getLast_num()/26) + String.valueOf((char) ('A' + products.get(proname).getLast_num()%26)));
			db.addProduct(new Product(procode, proname, ep_date, price));
			db.addNames(proname);
		}
		
	}
	
	public String FindEp_date(String today, int epd_value)			//������� ��� �Լ�
	{
		String ep_date;
		int[] maxdate = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int value, Cyear, Cmonth, Cday;	//���� ��, ��, ��
		value = epd_value;
		Cyear = Integer.parseInt(today.substring(0, 4));
		Cmonth = Integer.parseInt(today.substring(4, 6));
		Cday = Integer.parseInt(today.substring(6, 8));
		
		while(value > 0)
		{
			value--;
			if(Cday != maxdate[Cmonth-1])	//���� ���� ���� �ִ����� �ƴ� ��
				Cday++;
			else							//�ִ� �� �϶� ���� �� 1�Ϸ� ����
			{
				Cday = 1;			
				if(Cmonth == 12)			//12���϶� ���� �� 1���� ����
				{
					Cyear += 1;
					Cmonth = 1;		
				}
				else						//12���� �ƴϸ� ���� �޷� ����
					Cmonth++;
			}	
		}
		ep_date = Integer.toString(Cyear);
		if(Integer.toString(Cmonth).length() == 1)	//�� �ڸ� �� ���̸�
			ep_date += "0" + Integer.toString(Cmonth);
		else										//�� �ڸ� ���̸�
			ep_date += Integer.toString(Cmonth);
		
		if(Integer.toString(Cday).length() == 1)	//�� �ڸ� �� ���̸�
			ep_date += "0" + Integer.toString(Cday);
		else										//�� �ڸ� ���̸�
			ep_date += Integer.toString(Cday);
		
		
		return ep_date;
	}
	
}