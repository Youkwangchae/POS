package sw.pos;

import java.io.IOException;
import java.util.Scanner;

public class InventoryManager {
	
	Scanner scan = new Scanner(System.in);
	ScreenClear sc = new ScreenClear();
	int menu;
	public InventoryManager() throws InterruptedException, IOException	//생성자
	{
		do
		{	
			System.out.println("1. 상품등록");
			System.out.println("2. 재고추가");
			System.out.println("3. 폐기등록");
			System.out.println("4. 종료\n");
			System.out.print("메뉴 선택: ");
			menu = scan.nextInt();
			
			switch(menu)	//입력받은 명령어에 따른 동작
			{
			case 1:			//1. 상품등록
				sc.ScreenClear();
				ProductRegister pr = new ProductRegister();
				break;
			case 2:			//2. 재고추가
				sc.ScreenClear();
				AddInventory ai = new AddInventory();
				break;
			case 3:			//3. 폐기등록
				sc.ScreenClear();
				DisposalRegister dr = new DisposalRegister();
				break;
			case 4:			//4. 종료
				System.out.println("재고관리을 종료합니다.");
				break;		
			default:		//잘못된 명령어인  경우
				System.out.println("명령어가 옳지 않습니다.\n");
			}
		}while(menu != 4);
	}
	
}
