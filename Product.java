package sw.pos;

public class Product {
	private String name, code, ep_date;//��ǰ��, ��ǰ �ڵ�, �������
	private int price;//����
	boolean isPayByCash;//���ݰ��� ����. true�� ���ݰ���, false�� ī�����
	public Product(String code, String name, String ep_date, String price) {
		super();
	    this.name = name;
	    this.code = code;
	    this.ep_date = ep_date;
	    this.price = Integer.parseInt(price);
	    this.isPayByCash = true;
	}

	public String getName() {
	    return name;
	}

	public String getCode() {
	    return code;
	}

	public String getEpdate() {
		return ep_date;
	}

	public int getPrice() {
		return price;
	}

	public boolean getIsPayByCash() {
		return isPayByCash;
	}

	public void setPayByCash(boolean isPayByCash) {
		this.isPayByCash = isPayByCash;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String str = this.code + "/" + this.name + "/" + this.ep_date + "/"+ this.price + "\n";
		return str;
	}
	
}
