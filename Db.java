package sw.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Db {
	private String last_date;//���� �ֱ��� ��¥ ���� ����
	private HashMap<String, ArrayList<Product>> products = new HashMap<>();//��ǰ �����ϴ� ����Ʈ
	private HashMap<String, CategoryInfo> categorys;//ū ī�װ� �����ϴ� ����Ʈ
	private HashMap<String, NameInfo> names;//��ǰ����(��ǰ�̸�) �����ϴ� ����Ʈ
	private HashMap<Integer, Integer> cash;//���ݺ����� �����ϴ� ����Ʈ
	private HashMap<String, ArrayList<Product>> payments = new HashMap<>();//���� ���� ���� ����Ʈ
	
	private FileIO fileio;//���� ����� Ŭ����
	
	public Db() {
		fileio = new FileIO();
		//����Ʈ�� ���ϳ��� ����
		last_date = fileio.readLastDate();//�ֽ� ��¥.
		categorys = fileio.readCategory();
		names = fileio.readName();
		cash = fileio.readCash();
		payments = fileio.readPayment();
		for(int i=0; i<categorys.size(); i++) {//AA�� �ϳ��� ������Ŵ
			int num1 = (i/26)+65;
			int num2 = (i%26)+65;
			char ascii1 = (char)num1;
			char ascii2 = (char)num2;
			String file_name = "";
			file_name+=ascii1;
			file_name+=ascii2;
			products.put(file_name, fileio.readProduct((file_name+".txt")));
		}
	}
	//��ǰ �߰� �Լ�
	public void addProduct(Product product) {
		String file_name = product.getCode().substring(0, 2);
		if(!products.containsKey(file_name)) {
			ArrayList<Product> list = new ArrayList<Product>();
			list.add(product);
			products.put(file_name, list);
		}
		else {
			products.get(file_name).add(product);
		}
		
		ArrayList<String> contents = new ArrayList<>();
		for(int i=0; i<products.get(file_name).size(); i++) {
			String str = products.get(file_name).get(i).getCode();
			str+="/"+products.get(file_name).get(i).getName();
			str+="/"+products.get(file_name).get(i).getEpdate();
			str+="/"+products.get(file_name).get(i).getPrice()+"\n";
			contents.add(str);
		}
		fileio.writeFile((file_name+".txt"), contents);
	}
	//��ǰ ���� �Լ�
	public void removeProduct(String code) {
		String file_name = code.substring(0, 2);
		for(int i=0; i<products.get(file_name).size(); i++) {
			if(products.get(file_name).get(i).getCode().equals(code)) {
				products.get(file_name).remove(i);
				break;
			}
		}
		ArrayList<String> contents = new ArrayList<>();
		for(int i=0; i<products.get(file_name).size(); i++) {
			String str = products.get(file_name).get(i).getCode();
			str+="/"+products.get(file_name).get(i).getName();
			str+="/"+products.get(file_name).get(i).getEpdate();
			str+="/"+products.get(file_name).get(i).getPrice()+"\n";
			contents.add(str);
		}
		fileio.writeFile((file_name+".txt"), contents);
	}
	//��ǰ����(��ǰ�̸�) ���� �Լ�
	public void addNames(String name) {
		names.get(name).addLast_num();
		Set<String> set = names.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> contents = new ArrayList<>();
		String key;
		for(int i=0; i<names.size(); i++) {
			key = it.next();
			String str = key + "/" + names.get(key).getName_code() + "/" + names.get(key).getLast_num();
			str += "/" + names.get(key).getEpd_value()+"\n";
			contents.add(str);
		}
		fileio.writeFile("PName.txt", contents);
	}
	//��ǰ����(��ǰ�̸�) �߰� �Լ�
	public void addNames(String name, String code, int epd_value) {
		names.put(name, new NameInfo(code, 1, epd_value));
		String contents = name+"/"+code+"/1/"+epd_value;
		fileio.writeFile("PName.txt", contents);
	}
	//���� �ܷ� ���� �Լ� 
	public void setCash(int unit, int count, boolean isNegative) {
		
		int num = cash.get(unit);
		cash.remove(unit);
		if(isNegative)//���� �ܷ� ����
		{	if(num-count>0) {
			System.out.println(unit+"�� "+count+"�� ���� �Ϸ�!");
				cash.put(unit, num-count);
		}
			else {
				System.out.println("�ش� ������ ������ 0���� �Ǿ����ϴ�.");
				cash.put(unit,0);
			}
		}
		else
		{	if(num+count<=99)//���� �ܷ� ����
			{System.out.println(unit+"�� "+count+"�� ���� �Ϸ�!");
			cash.put(unit, num+count);
			}
			else {
				System.out.println("�� ����� �ִ� 99�������� ������ �� �־� 99���� �߰��Ǿ����ϴ�.");
				cash.put(unit,99);
			}
		}
		
		ArrayList<String> contents = new ArrayList<>();
		contents.add("50000:"+cash.get(50000)+"\n");
		contents.add("10000:"+cash.get(10000)+"\n");
		contents.add("5000:"+cash.get(5000)+"\n");
		contents.add("1000:"+cash.get(1000)+"\n");
		contents.add("500:"+cash.get(500)+"\n");
		contents.add("100:"+cash.get(100)+"\n");
		contents.add("50:"+cash.get(50)+"\n");
		contents.add("10:"+cash.get(10)+"\n");
		fileio.writeFile("Cash.txt", contents);
		
	}
	//ū ī�װ� ���� �Լ�
	public void addCategory(String cate) {
		categorys.get(cate).addLast_num();
		Set<String> set = categorys.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> contents = new ArrayList<>();
		String key;
		for(int i=0; i<categorys.size(); i++) {
			key = it.next();
			String str = key + "/" + categorys.get(key).getCategory_code() + "/" + categorys.get(key).getLast_num() + "\n";
			contents.add(str);
		}
		fileio.writeFile("Category.txt", contents);
	}
	//ū ī�װ� �߰� �Լ�
	public void addCategory(String cate, String code) {
		categorys.put(cate, new CategoryInfo(code, 1));
		String contents = cate+"/"+code+"/1";
		fileio.writeFile("Category.txt", contents);
	}
	//���� ��� �߰� �Լ�
	public void addPayment(String code, ArrayList<Product> list) {
		payments.put(code, list);
		String str = code+"\n";
		for(int i=0; i<list.size(); i++) {
			str+= list.get(i).getCode();
			str+="/"+list.get(i).getName();
			str+="/"+list.get(i).getEpdate();
			str+="/"+list.get(i).getPrice()+"\n";
		}
		str+="@\n";
		fileio.writeFile("PaymentList.txt", str);
	}
	//������ ��ǰ ���� ���
	public void removePayment(String pay_code, String pro_code) {
		for(int i=0; i<payments.get(pay_code).size(); i++) {
			if(payments.get(pay_code).get(i).getCode().equals(pro_code)) {
				payments.get(pay_code).remove(i);
				if(payments.get(pay_code).isEmpty()) {
					payments.remove(pay_code);
				}
				break;
			}
		}
		
		Set<String> set = payments.keySet();
		Iterator<String> it = set.iterator();
		ArrayList<String> contents = new ArrayList<>();
		String key;
		for(int i=0; i<payments.size(); i++) {
			key = it.next();
			String str = key+"\n";
			for(int j=0; j<payments.get(key).size(); j++) {
				str+=payments.get(key).get(j).getCode()+"/";
				str+=payments.get(key).get(j).getName()+"/";
				str+=payments.get(key).get(j).getEpdate()+"/";
				str+=payments.get(key).get(j).getPrice()+"\n";
			}
			str+="@\n";
			contents.add(str);
		}
		fileio.writeFile("PaymentList.txt", contents);
	}
	//���� ��¥���� ���� ��¥���� �Ǵ�.
	public String getLast_date() {
		return last_date;
	}
	
	public boolean isPossible(String date) {
		boolean isLast_date = false;
		//��¥�� ������ 20000101~20991231�ϱ� index 2���� index 7���� ���� ��.
		
		if(isPossible(date, 2)) {
			isLast_date = true;
		}
		
		if(isLast_date) 
			return true;
		else {
			System.out.println("������ �Է��� ��¥�� ���ų� ������ ��¥�� �Է� �����մϴ�.");
			System.out.println("�ٽ� �Է����ּ���.");
			return false;
		}
		
	}
	
	public boolean isPossible(String date, int i) {
		if(i==8)
			return true;
		if(date.charAt(i)-'0' > last_date.charAt(i)-'0')
			return true;
		else if(date.charAt(i)-'0'== last_date.charAt(i)-'0')
			return isPossible(date, i+1);
		else
			return false;
	}
	
	//�ֱ� ��¥ ���� �Լ�
	public String setLast_date(String last_date) {
			this.last_date = last_date;
			fileio.writeFile("Date.txt", last_date);
			return last_date.substring(0, 4)+"_"+last_date.substring(4, 6)+"_"+last_date.substring(6);
	}

	public HashMap<String, ArrayList<Product>> getProducts() {
		return products;
	}

	public HashMap<String, CategoryInfo> getCategorys() {
		return categorys;
	}

	public HashMap<String, NameInfo> getNames() {
		return names;
	}

	public HashMap<String, ArrayList<Product>> getPayments() {
		return payments;
	}
	public void setPayments(HashMap<String, ArrayList<Product>> payments) {
		this.payments = payments;
	}
	public HashMap<Integer, Integer> getCash() {
		int [] key = {10, 50, 100, 500, 1000, 5000, 10000, 50000};
		for(int i=0;i<cash.size();i++)
			System.out.println((i+1)+"."+key[i]+"��: "+cash.get(key[i])+"��");
		return cash;
	}
	
}
