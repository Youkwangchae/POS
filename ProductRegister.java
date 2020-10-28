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
	Set<String> set; 		//Ű ���� �����ϴ� set
	Iterator<String> it; 	//set �˻��� ���� iterator
	private HashMap<String, NameInfo> products;		//��ǰ�̸� �����ϴ� ����Ʈ
	private HashMap<String, CategoryInfo> categorys;	//ī�װ��� �����ϴ� ����Ʈ
	String key, proname, catename, code, ep_value, price;		//HashMap �˻��� ���� ��Ʈ��, �Է¹޴� ��ǰ��, �Է¹޴� ī�װ���, ������ ī�װ� �ڵ�, ������� ���� ��, ����
	boolean check;						//�˻� �����ϸ� true, �ƴϸ� false
	ScreenClear sc = new ScreenClear();
	
	public ProductRegister(Db db) throws InterruptedException, IOException
	{
		this.db = db;
		products = db.getNames();
		categorys = db.getCategorys();
		System.out.print("����� ��ǰ���� �Է����ּ���: ");
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
				System.out.println("�̹� �����ϴ� ��ǰ���Դϴ�. ����� �����մϴ�.");
				return;
			}
			else	//���� ���
				continue;
		}
		
		System.out.print("�ش� ��ǰ�� ī�װ��� �Է����ּ���: ");	//ī�װ��� �Է�
		catename = scan.next();
		
		while( (!catename.matches(".*[��-����-�Ӱ�-�R]+.*")) || (catename.length() > 10) )	//ī�װ��� ����ó��
		{
			System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY �ѱ�, 10���� ����): ");
			catename = scan.next();
		}
		
		set = categorys.keySet();			//�ùٸ� ī�װ����� �Է¹��� ��� �̹� �ִ� �̸����� �˻�
		it = set.iterator();
		for(int i=0; i<categorys.size(); i++) 
		{
			key = it.next();
			if(key.equals(catename))	//�ִ� ���
			{
				check = true;
				break;
			}
			else	//���� ���
				check = false;
		}
			
		if(!check)	//���ٸ� ū ī�װ� ����
		{
			if(categorys.size() < 26*26)	//ū ī�װ� ���� 26*26�� ���� ���� �� 
			{
				String file_name = String.valueOf((char) ('A' + categorys.size()/26) + String.valueOf((char) ('A' + categorys.size()%26)));
				db.addCategory(catename, file_name );
			}
			else
			{
				System.out.println("�� �̻� ī�װ��� ������ �� �����ϴ�.");
				System.out.println("��� ������ ���ư��ϴ�.");
				return;
			}
		}
		
		CategoryInfo ci = categorys.get(catename);	//���� ī�װ� ����
		code = ci.getCategory_code();
		int last = ci.getLast_num();
		if(categorys.get(catename).getLast_num() < 26*26)	//���� ī�װ� ���� 26*26���� ���� ��
			code += String.valueOf((char) ('A' + categorys.get(catename).getLast_num()/26) + String.valueOf((char) ('A' + categorys.get(catename).getLast_num()%26)));
		else
		{
			System.out.println("�ش� ī�װ��� �� �̻� ��ǰ�� ����� �� �����ϴ�.");
			System.out.println("��� ������ ���ư��ϴ�.");
			return;
		}
		
		
		
		System.out.print("�ش� ��ǰ�� ������� ���� ���� �Է����ּ���: ");	//������� ���� �� �Է�
		ep_value = scan.next();
		
		while( (!ep_value.matches(".*[0-9]+.*")) || ep_value.length() < 1 || ep_value.length() > 3 || ep_value.substring(0, 1).equals("0"))	//������� ���� �� ����ó��
		{
			System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY ����, 1�ڸ� �̻� 3�ڸ� ����): ");
			ep_value = scan.next();
		}
		
		
		System.out.print("�ش� ��ǰ�� ������ �Է����ּ���: ");	//���� �Է�
		price = scan.next();
		
		while( (!price.matches(".*[0-9]+.*")) || price.length() < 1 || price.length() > 8  || price.substring(0, 1).equals("0"))	//���� ����ó��
		{
			System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY ����, 1�ڸ� �̻� 8�ڸ� ����): ");
			price = scan.next();
		}
		
		sc.ScreenClear();
		
		System.out.println("��ǰ��: " + proname);			//��ǰ��� ���� ����
		System.out.println("ī�װ���: " + catename);
		System.out.println("������� ���� ��: " + ep_value);
		System.out.println("����: " + price);
		System.out.print("\n�ش� ��ǰ�� ������ �߰��Ͻðڽ��ϱ�?(Y/N) ");
		String answer = scan.next();
		
		while(!(answer.equals("Y") || answer.equals("N")))
		{
			System.out.print("�߸��� �Է��Դϴ�. �ٽ� �Է����ּ���(Y/N) ");
			answer = scan.next();
		}
		
		if(answer.equals("Y"))
		{
	
			db.addNames(proname, code, Integer.parseInt(ep_value), Integer.parseInt(price));
			db.addCategory(catename);
			System.out.println("��ǰ����� �Ϸ�Ǿ����ϴ�.");
			System.out.println("��� ������ ���ư��ϴ�.");
		}
		else if(answer.equals("N"))
			System.out.println("��� ������ ���ư��ϴ�.");
		
	
		
	
	}
	
}