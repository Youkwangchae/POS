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
	Set<String> set; 														//키 값을 저장하는 set
	Iterator<String> it; 													//set 검색을 위한 iterator
	String key, today;														//Set에서 검색을 위한 스트링 변수, 오늘 날짜
	boolean check = false;													//검색 성공 여부 확인
	int dis;
	
	public DisposalRegister(Db db) throws InterruptedException, IOException
	{
		this.db = db;
		today = db.getLast_date();
		ShowDisposal(today);
		System.out.println("재고 관리로 돌아갑니다.");
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
			System.out.println("- 유통기한 지난 상품검색 -");
			for(int i=0; i < Category.size() ; i++)					//큰 카테고리 개수 만큼의 상품 파일을 읽고 유통기한 지난 상품 출력
			{
				filename = String.valueOf((char) ('A' + i/26) + String.valueOf((char) ('A' + i%26))) + ".txt";
				products = fileio.readProduct(filename);
				
				for(int j = 0; j<products.size(); j++)
				{
					if(Integer.parseInt(products.get(j).getEpdate()) < Integer.parseInt(today))		//유통기한 < 오늘 날짜
					{
						check = true;
						System.out.println(products.get(j).getCode() + "/" + products.get(j).getName() + "/" + products.get(j).getEpdate() + "/" + products.get(j).getPrice());
					}
				}
			}
			
			 if(!check)
				 System.out.println("\n유통기한 지난 상품들이 없습니다.");
			 else
			 {
				 System.out.println("\n\n폐기할 상품의 상품코드를 입력해주세요");	
			 }
			 System.out.println("종료하시려면 \"완료\"를 입력하세요");	
			 	
			do
			{
				answer = scan.nextLine();
				if(!answer.equals("완료"))		//완료가 아니면
				{
					while( !checkBlank(answer) || answer.matches(".*[^a-z A-Z]+.*") || answer.length() != 6 )	//폐기 상품코드 예외처리
					{
						System.out.print("잘못된 입력, 다시 입력해주세요(\"완료\" 또는 ONLY 영어, 6글자, 공백제외): ");
						answer = scan.nextLine();
						if(answer.equals("완료"))		
							break;
					}
					if(!answer.equals("완료"))		//완료가 아니면
					{
						for(int i=0; i < Category.size() ; i++)					//큰 카테고리 개수 만큼의 상품 파일을 읽기
						{
							filename = String.valueOf((char) ('A' + i/26) + String.valueOf((char) ('A' + i%26))) + ".txt";
							products = fileio.readProduct(filename);
									
							dis = 3;	//어떠한 상품과 코드가 맞지 않는 상태
							for(int j = 0; j<products.size(); j++)
							{
								if(Integer.parseInt(products.get(j).getEpdate()) < Integer.parseInt(today))		//유통기한 < 오늘 날짜(폐기 대상 상품)
								{
									if(products.get(j).getCode().equals(answer.toUpperCase()))	//입력한 상품과 같다면 대소문자가 달라도 문자열이 같다면
									{
										dis = 1;
										break;
									}																				
								}
								else if(products.get(j).getCode().equals(answer))	//폐기 대상은 아닌 상품과 같다면
								{
									dis = 2;
									break;
								}	
							}
									
						}							
						if(dis == 1)
						{
							answer = answer.toUpperCase();
							CheckDisposal(answer);
						}
						else if(dis == 2)
							System.out.println("폐기 대상 상품이 아닙니다. 다시 입력해주세요.");
						else
							System.out.println("존재하지 않는 상품입니다. 다시 입력해주세요.");
					}
					else	//완료면
						break;
				}
				else		//완료입력
					break;
			}while(dis != 1);
		}while(!answer.equals("완료"));
	}
	

	
	public void CheckDisposal(String procode)
	{
		 ArrayList<Product> products = new ArrayList();
		 ArrayList<String> contents = new ArrayList();
		 String filename, yn;
			
		 System.out.print("해당 상품(" + procode + ")을 정말 폐기하시겠습니까?(Y/N): ");
		 yn = scan.nextLine();
		 
		 while(!( yn.equals("Y") || yn.equals("N") ) || !checkBlank(yn))
		 {
			 System.out.print("잘못된 입력, 다시 입력해주세요(ONLY Y/N 1글자, 공백제외): ");
			 yn = scan.nextLine();
		 }
		 
		 if(yn.equals("N"))
			 return;
		 else if(yn.equals("Y"))
		 {
			 filename = procode.substring(0, 2) + ".txt";		//큰 카테고리 추출
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
	
	public boolean checkBlank(String PayCode)	// 선후 공백 체크
	{
	      String B_PayCode=PayCode.replaceAll("\\s+", "");
	     
	      if(B_PayCode.equals(PayCode)) 		//공백이 없는 거
	         return true;
	      else									//공백이 있는 거
	    	  return false;
	}
}
