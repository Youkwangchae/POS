package sw.pos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class DisposalRegister {
	
	ScreenClear sc = new ScreenClear();
	Scanner scan = new Scanner(System.in);
	Db db;
	FileIO fileio = new FileIO();
	Set<String> set; 														//Ű ���� �����ϴ� set
	Iterator<String> it; 													//set �˻��� ���� iterator
	String key, today;														//Set���� �˻��� ���� ��Ʈ�� ����, ���� ��¥
	boolean check;															//�˻� ���� ���� Ȯ��
	
	public DisposalRegister(Db db) throws InterruptedException, IOException
	{
		this.db = db;
		today = db.getLast_date();
		ShowDisposal(today);
		System.out.println("��� ������ ���ư��ϴ�.");
	}
	
	public void ShowDisposal(String today) throws InterruptedException, IOException
	{
		ArrayList<Product> products = new ArrayList();
		HashMap<String, CategoryInfo> Category = fileio.readCategory();	//
		String filename, answer;
		
		System.out.println(Category.size());
		do
		{
			sc.ScreenClear();
			System.out.println("- ������� ���� ��ǰ�˻� -");
			for(int i=0; i < Category.size() ; i++)					//ū ī�װ� ���� ��ŭ�� ��ǰ ������ �а� ������� ���� ��ǰ ���
			{
				filename = String.valueOf((char) ('A' + i/26) + String.valueOf((char) ('A' + i%26))) + ".txt";
				products = fileio.readProduct(filename);
				
				for(int j = 0; j<products.size(); j++)
				{
					if(Integer.parseInt(products.get(j).getEpdate()) < Integer.parseInt(today))		//������� < ���� ��¥
					{
						check = true;
						System.out.println(products.get(j).getCode() + "/" + products.get(j).getName() + "/" + products.get(j).getEpdate() + "/" + products.get(j).getPrice());
					}
					else
						check = false;
				}
			}
			
			 if(!check)
				 System.out.println("\n������� ���� ��ǰ���� �����ϴ�..");
			 else
			 {
				 System.out.println("\n\n\n");
				 System.out.println("����� ��ǰ�� ��ǰ�ڵ带 �Է����ּ���");
				 System.out.println("�����Ͻ÷��� \"�Ϸ�\"�� �Է��ϼ���");
				 
				
			 }
			 
			 answer = scan.next();
			 
			 if(!answer.equals("�Ϸ�"))		//�Ϸᰡ �ƴϸ�
			 {
				 while( (!answer.matches(".*[A-Z]+.*")) || answer.length() != 6 )	//�߰� ���� ����ó��
				 {
					 System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY �θ��� �빮��, 6����): ");
					 answer = scan.next();
					 if(answer.equals("�Ϸ�"))		
						 break;
				 }
				 if(!answer.equals("�Ϸ�"))		//�Ϸᰡ �ƴϸ�
					 CheckDisposal(answer);
			 }
		}while(!answer.equals("�Ϸ�"));
	
	}
	
	public void CheckDisposal(String procode)
	{
		 ArrayList<Product> products = new ArrayList();
		 ArrayList<String> contents = new ArrayList();
		 String filename, yn;
			
		 System.out.print("�ش� ��ǰ(" + procode + ")�� ���� ����Ͻðڽ��ϱ�?(Y/N) ");
		 yn = scan.next();
		 
		 while(!( yn.equals("Y") || yn.equals("N") ))
		 {
			 System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY �θ��� �빮��, 6����): ");
			 procode = scan.next();
		 }
		 
		 if(yn.equals("N"))
			 return;
		 else if(yn.equals("Y"))
		 {
			 filename = procode.substring(0, 2) + ".txt";		//ū ī�װ� ����
			 products = fileio.readProduct(filename);
			 int ps = products.size();
			 
			 for(int i = 0; i < ps; i++)
			 {
				if(procode.equals(products.get(i).getCode()))
				{
					products.remove(i);
					ps--;
					i--;
				}
				else
					contents.add(products.get(i).getCode() + "/" + products.get(i).getName() + "/" + products.get(i).getEpdate() + "/" + products.get(i).getPrice()+"\n");
			 }
			 
			 fileio.writeFile(filename, contents);
		 }
			 
	}
}