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
	Set<String> set; 														//Å° °ªÀ» ÀúÀåÇÏ´Â set
	Iterator<String> it; 													//set °Ë»öÀ» À§ÇÑ iterator
	String key, proname;													//Set¿¡¼­ °Ë»öÀ» À§ÇÑ ½ºÆ®¸µ º¯¼ö, »óÇ°ÀÌ¸§
	String catecode, procode, today, ep_date, price;						//»óÇ°ÀÇ Ä«Å×°í¸® ÄÚµå, »óÇ°ÄÚµå, ¿À´Ã ³¯Â¥, À¯Åë±âÇÑ, °¡°İ
	int procount, last_num, epd_value;   									//Ãß°¡ÇÒ °³¼ö»ı¼ºµÈ °³¼ö, À¯Åë±âÇÑ ¼³Á¤ °ª
	boolean check;															//°Ë»ö ¼º°ø ¿©ºÎ È®ÀÎ
	private HashMap<String, NameInfo> products;								//»óÇ°ÀÌ¸§ ÀúÀåÇÏ´Â ¸®½ºÆ®
	
	public AddInventory(Db db)
	{
		this.db = db;
		products = db.getNames();	
		today = db.getLast_date();
		System.out.print("Ãß°¡ÇÒ »óÇ°ÀÇ ÀÌ¸§À» ÀÔ·ÂÇØÁÖ¼¼¿ä: ");
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
				check = true;
				break;
			}
			else	//¾ø´Â °æ¿ì
				check = false;
		}
		
		if(!check)		//°Ë»ö ½ÇÆĞ
		{
			System.out.println("Á¸ÀçÇÏÁö ¾Ê´Â »óÇ°ÀÔ´Ï´Ù.");
			return;
		}
		else			//°Ë»ö ¼º°ø
		{
			catecode = products.get(proname).getName_code();
			last_num = products.get(proname).getLast_num();
			epd_value = products.get(proname).getEpd_value();
			price = Integer.toString(products.get(proname).getPrice());
			ep_date = FindEp_date(today, epd_value);
		}
		
		if(products.get(proname).getLast_num() >= 26*26)		//ÇØ´ç »óÇ°ÀÇ Àç°í°¡ ²Ë Âù °æ¿ì
		{
			System.out.println("ÇØ´ç »óÇ°Àº ´õ ÀÌ»ó Àç°í¸¦ Ãß°¡ÇÒ ¼ö ¾ø½À´Ï´Ù.");
			System.out.println("Àç°í °ü¸®·Î µ¹¾Æ°©´Ï´Ù.");
			return;
		}
			
		
		System.out.print("Ãß°¡ÇÒ °³¼ö¸¦ ÀÔ·ÂÇØÁÖ¼¼¿ä: ");
		String temp = scan.next();
		
		while( (!temp.matches(".*[0-9]+.*")) || (temp.length() > 3) )	//Ãß°¡ °³¼ö ¿¹¿ÜÃ³¸®
		{
			System.out.print("Àß¸øµÈ ÀÔ·Â, ´Ù½Ã ÀÔ·ÂÇØÁÖ¼¼¿ä(ONLY ¼ıÀÚ, 3±ÛÀÚ ÀÌÇÏ): ");
			temp = scan.next();
		}	
		procount = Integer.parseInt(temp);
		if(procount > 676)					//676ÀÌ»óÀÌ¸é 676°³¸¸ ÀúÀå
			procount = 676;
		if(procount + last_num > 676)		//ÇöÀç ÀúÀåµÈ °³¼ö + Ãß°¡ÇÒ °³¼ö > 676ÀÏ¶§
			procount -= last_num;
	
		for(int i = 0; i < procount; i++)
		{
			procode = catecode + String.valueOf((char) ('A' + products.get(proname).getLast_num()/26) + String.valueOf((char) ('A' + products.get(proname).getLast_num()%26)));
			db.addProduct(new Product(procode, proname, ep_date, price));
			db.addNames(proname);
		}
		
	}
	
	public String FindEp_date(String today, int epd_value)			//À¯Åë±âÇÑ °è»ê ÇÔ¼ö
	{
		String ep_date;
		int[] maxdate = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		int value, Cyear, Cmonth, Cday;	//ÇöÀç ³â, ¿ù, ÀÏ
		value = epd_value;
		Cyear = Integer.parseInt(today.substring(0, 4));
		Cmonth = Integer.parseInt(today.substring(4, 6));
		Cday = Integer.parseInt(today.substring(6, 8));
		
		while(value > 0)
		{
			value--;
			if(Cday != maxdate[Cmonth-1])	//¿À´Ã ÀÏÀÌ ¿ùÀÇ ÃÖ´ëÀÏÀÌ ¾Æ´Ò ¶§
				Cday++;
			else							//ÃÖ´ë ÀÏ ÀÏ¶§ ´ÙÀ½ ´Ş 1ÀÏ·Î º¯°æ
			{
				Cday = 1;			
				if(Cmonth == 12)			//12¿ùÀÏ¶§ ´ÙÀ½ ³â 1¿ù·Î º¯°æ
				{
					Cyear += 1;
					Cmonth = 1;		
				}
				else						//12¿ùÀÌ ¾Æ´Ï¸é ´ÙÀ½ ´Ş·Î º¯°æ
					Cmonth++;
			}	
		}
		ep_date = Integer.toString(Cyear);
		if(Integer.toString(Cmonth).length() == 1)	//ÇÑ ÀÚ¸® ¼ö ¿ùÀÌ¸é
			ep_date += "0" + Integer.toString(Cmonth);
		else										//µÎ ÀÚ¸® ¼öÀÌ¸é
			ep_date += Integer.toString(Cmonth);
		
		if(Integer.toString(Cday).length() == 1)	//ÇÑ ÀÚ¸® ¼ö ÀÏÀÌ¸é
			ep_date += "0" + Integer.toString(Cday);
		else										//µÎ ÀÚ¸® ¼öÀÌ¸é
			ep_date += Integer.toString(Cday);
		
		
		return ep_date;
	}
	
}