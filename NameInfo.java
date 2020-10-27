package sw.pos;

public class NameInfo {
	
	private int last_num, epd_value, price;	//현재 동일 이름으로 생성된 상품 개수, 유통기한 설정값, 가격
	private String name_code;			//상품이름에 따른 카테고리 코드 4자리
	
	public NameInfo(String name_code, int last_num, int epd_value, int price) 
	{
		this.name_code = name_code;
		this.last_num = last_num;
		this.epd_value = epd_value;
		this.price = price;
	}

	public int getLast_num() 
	{
		return last_num;
	}

	public void addLast_num() 
	{
		this.last_num++;
	}

	public int getEpd_value() 
	{
		return epd_value;
	}

	public String getName_code()
	{
		return name_code;
	}
	
	public int getPrice()
	{
		return price;
	}
	
}
