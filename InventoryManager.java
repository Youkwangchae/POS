package sw.pos;

import java.io.IOException;
import java.util.Scanner;

public class InventoryManager {
	
	Scanner scan = new Scanner(System.in);
	ScreenClear sc = new ScreenClear();
	String menu;
	public InventoryManager(Db db) throws InterruptedException, IOException	//������
	{
		do
		{	
			System.out.println("1. ��ǰ���");
			System.out.println("2. ����߰�");
			System.out.println("3. �����");
			System.out.println("4. ����\n");
			System.out.print("�޴� ����: ");
			menu = scan.next();
			
			while( (!menu.matches(".*[0-9]+.*")) || (menu.length() > 1) )	//��ɾ� ����ó��
			{
				System.out.print("�߸��� �Է�, �ٽ� �Է����ּ���(ONLY ����, 1����): ");
				menu = scan.next();
			}
			
			switch(menu)	//�Է¹��� ��ɾ ���� ����
			{
			case "1":			//1. ��ǰ���
				sc.ScreenClear();
				ProductRegister pr = new ProductRegister(db);
				break;
			case "2":			//2. ����߰�
				sc.ScreenClear();
				AddInventory ai = new AddInventory(db);
				break;
			case "3":			//3. �����
				sc.ScreenClear();
				DisposalRegister dr = new DisposalRegister(db);
				break;
			case "4":			//4. ����
				System.out.println("�������� �����մϴ�.");
				break;		
			default:		//�߸��� ��ɾ���  ���
				System.out.println("��ɾ ���� �ʽ��ϴ�.\n");
			}
		}while(menu != "4");
	}
	
}