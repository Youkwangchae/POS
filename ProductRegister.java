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
	Set<String> set; 		//Å° °ªÀ» ÀúÀåÇÏ´Â set
	Iterator<String> it; 	//set °Ë»öÀ» À§ÇÑ iterator
	private HashMap<String, NameInfo> products;		//»óÇ°ÀÌ¸§ ÀúÀåÇÏ´Â ¸®½ºÆ®
	private HashMap<String, CategoryInfo> categorys;	//Ä«Å×°í¸®¸¦ ÀúÀåÇÏ´Â ¸®½ºÆ®
	String key, proname, catename, code, ep_value, price;		//HashMap °Ë»öÀ» À§ÇÑ ½ºÆ®¸µ, ÀÔ·Â¹Ş´Â »óÇ°¸í, ÀÔ·Â¹Ş´Â Ä«Å×°í¸®¸í, »ı¼ºµÈ Ä«Å×°í¸® ÄÚµå, À¯Åë±âÇÑ ¼³Á¤ °ª, °¡°İ
	boolean check;						//°Ë»ö ¼º°øÇÏ¸é true, ¾Æ´Ï¸é false
	ScreenClear sc = new ScreenClear();
	
	public ProductRegister(Db db) throws InterruptedException, IOException
	{
		this.db = db;
		products = db.getNames();
		categorys = db.getCategorys();
		System.out.print("µî·ÏÇÒ »óÇ°¸íÀ» ÀÔ·ÂÇØÁÖ¼¼¿ä: ");
		proname = scan.next();
		
		while( (!proname.matches(".*[¤¡-¤¾¤¿-¤Ó°¡-ÆR]+.*")) || (proname.length() > 10) )	//»óÇ°¸í ¿¹¿ÜÃ³¸®
		{
			System.out.print("Àß¸øµÈ ÀÔ·Â, ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä(ONLY ÇÑ±Û, 10±ÛÀÚ ÀÌÇÏ): ");
			proname = scan.next();
		}
		
		set = products.keySet();			//¿Ã¹Ù¸¥ »óÇ°¸íÀ» ÀÔ·Â¹ŞÀº °æ¿ì ÀÌ¹Ì ÀÖ´Â ÀÌ¸§ÀÎÁö °Ë»ö
		it = set.iterator();
		for(int i=0; i<products.size(); i++) 
		{
			key = it.next();
			if(key.equals(proname))	//ÀÖ´Â °æ¿ì
			{
				System.out.println("ÀÌ¹Ì Á¸ÀçÇÏ´Â »óÇ°¸íÀÔ´Ï´Ù. ±â´ÉÀ» Á¾·áÇÕ´Ï´Ù.");
				return;
			}
			else	//¾ø´Â °æ¿ì
				continue;
		}
		
		System.out.print("ÇØ´ç »óÇ°ÀÇ Ä«Å×°í¸®¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä: ");	//Ä«Å×°í¸®¸í ÀÔ·Â
		catename = scan.next();
		
		while( (!catename.matches(".*[¤¡-¤¾¤¿-¤Ó°¡-ÆR]+.*")) || (catename.length() > 10) )	//Ä«Å×°í¸®¸í ¿¹¿ÜÃ³¸®
		{
			System.out.print("Àß¸øµÈ ÀÔ·Â, ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä(ONLY ÇÑ±Û, 10±ÛÀÚ ÀÌÇÏ): ");
			catename = scan.next();
		}
		
		set = categorys.keySet();			//¿Ã¹Ù¸¥ Ä«Å×°í¸®¸íÀ» ÀÔ·Â¹ŞÀº °æ¿ì ÀÌ¹Ì ÀÖ´Â ÀÌ¸§ÀÎÁö °Ë»ö
		it = set.iterator();
		for(int i=0; i<categorys.size(); i++) 
		{
			key = it.next();
			if(key.equals(catename))	//ÀÖ´Â °æ¿ì
			{
				check = true;
				break;
			}
			else	//¾ø´Â °æ¿ì
				check = false;
		}
			
		if(!check)	//¾ø´Ù¸é Å« Ä«Å×°í¸® »ı¼º
		{
			if(categorys.size() < 26*26)	//Å« Ä«Å×°í¸® ¼ö°¡ 26*26°³ º¸´Ù ÀûÀ» ¶§ 
			{
				String file_name = String.valueOf((char) ('A' + categorys.size()/26) + String.valueOf((char) ('A' + categorys.size()%26)));
				db.addCategory(catename, file_name );
			}
			else
			{
				System.out.println("´õ ÀÌ»ó Ä«Å×°í¸®¸¦ »ı¼ºÇÒ ¼ö ¾ø½À´Ï´Ù.");
				System.out.println("Àç°í °ü¸®·Î µ¹¾Æ°©´Ï´Ù.");
				return;
			}
		}
		
		CategoryInfo ci = categorys.get(catename);	//¼¼ºÎ Ä«Å×°í¸® »ı¼º
		code = ci.getCategory_code();
		int last = ci.getLast_num();
		if(categorys.get(catename).getLast_num() < 26*26)	//¼¼ºÎ Ä«Å×°í¸® ¼ö°¡ 26*26º¸´Ù ÀûÀ» ¶§
			code += String.valueOf((char) ('A' + categorys.get(catename).getLast_num()/26) + String.valueOf((char) ('A' + categorys.get(catename).getLast_num()%26)));
		else
		{
			System.out.println("ÇØ´ç Ä«Å×°í¸®¿¡ ´õ ÀÌ»ó »óÇ°À» µî·ÏÇÒ ¼ö ¾ø½À´Ï´Ù.");
			System.out.println("Àç°í °ü¸®·Î µ¹¾Æ°©´Ï´Ù.");
			return;
		}
		
		
		
		System.out.print("ÇØ´ç »óÇ°ÀÇ À¯Åë±âÇÑ ¼³Á¤ °ªÀ» ÀÔ·ÂÇØÁÖ¼¼¿ä: ");	//À¯Åë±âÇÑ ¼³Á¤ °ª ÀÔ·Â
		ep_value = scan.next();
		
		while( (!ep_value.matches(".*[0-9]+.*")) || ep_value.length() < 1 || ep_value.length() > 3 || ep_value.substring(0, 1).equals("0"))	//À¯Åë±âÇÑ ¼³Á¤ °ª ¿¹¿ÜÃ³¸®
		{
			System.out.print("Àß¸øµÈ ÀÔ·Â, ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä(ONLY ¼ıÀÚ, 1ÀÚ¸® ÀÌ»ó 3ÀÚ¸® ÀÌÇÏ): ");
			ep_value = scan.next();
		}
		
		
		System.out.print("ÇØ´ç »óÇ°ÀÇ °¡°İÀ» ÀÔ·ÂÇØÁÖ¼¼¿ä: ");	//°¡°İ ÀÔ·Â
		price = scan.next();
		
		while( (!price.matches(".*[0-9]+.*")) || price.length() < 1 || price.length() > 8  || price.substring(0, 1).equals("0"))	//°¡°İ ¿¹¿ÜÃ³¸®
		{
			System.out.print("Àß¸øµÈ ÀÔ·Â, ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä(ONLY ¼ıÀÚ, 1ÀÚ¸® ÀÌ»ó 8ÀÚ¸® ÀÌÇÏ): ");
			price = scan.next();
		}
		
		sc.ScreenClear();
		
		System.out.println("»óÇ°¸í: " + proname);			//»óÇ°µî·Ï ÃÖÁ¾ °áÁ¤
		System.out.println("Ä«Å×°í¸®¸í: " + catename);
		System.out.println("À¯Åë±âÇÑ ¼³Á¤ °ª: " + ep_value);
		System.out.println("°¡°İ: " + price);
		System.out.print("\nÇØ´ç »óÇ°À» Á¤¸»·Î Ãß°¡ÇÏ½Ã°Ú½À´Ï±î?(Y/N) ");
		String answer = scan.next();
		
		while(!(answer.equals("Y") || answer.equals("N")))
		{
			System.out.print("Àß¸øµÈ ÀÔ·ÂÀÔ´Ï´Ù. ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä(Y/N) ");
			answer = scan.next();
		}
		
		if(answer.equals("Y"))
		{
	
			db.addNames(proname, code, Integer.parseInt(ep_value), Integer.parseInt(price));
			db.addCategory(catename);
			System.out.println("»óÇ°µî·ÏÀÌ ¿Ï·áµÇ¾ú½À´Ï´Ù.");
			System.out.println("Àç°í °ü¸®·Î µ¹¾Æ°©´Ï´Ù.");
		}
		else if(answer.equals("N"))
			System.out.println("Àç°í °ü¸®·Î µ¹¾Æ°©´Ï´Ù.");
		
	
		
	
	}
	
}