package sw.pos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileIO {
	public FileIO() {

	}
	//��¥ ������ txt ���� �б�
	public String readLastDate() {
		String date = "";
		try {
			File file = new File("Date.txt");
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				date = scan.nextLine();
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		return date;
	}
	//��ǰ�� ������ txt ���� �б�
	public ArrayList<Product> readProduct(String file_name){
		ArrayList<Product> list = new ArrayList<>();
		try {
			File file = new File(file_name);
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String str = scan.nextLine();
				StringTokenizer token = new StringTokenizer(str, "/");
				list.add(new Product(token.nextToken().trim(), token.nextToken().trim(), token.nextToken().trim(), token.nextToken().trim()));
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		return list;
	}
	//ū ī�װ� ������ txt ���� �б�
	public HashMap<String, CategoryInfo> readCategory(){
		HashMap<String, CategoryInfo> cates = new HashMap<>();
		try {
			File file = new File("Category.txt");
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String str = scan.nextLine();
				StringTokenizer token = new StringTokenizer(str, "/");
				cates.put(token.nextToken(), new CategoryInfo(token.nextToken(), Integer.parseInt(token.nextToken())));
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		return cates;
	}
	//��ǰ����(��ǰ�̸�) ������ txt ���� �б�
	public HashMap<String, NameInfo> readName(){
		HashMap<String, NameInfo> names = new HashMap<>();
		try {
			File file = new File("PName.txt");
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String str = scan.nextLine();
				StringTokenizer token = new StringTokenizer(str, "/");
				String name = token.nextToken();
				NameInfo info = new NameInfo(token.nextToken(), Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
				names.put(name, info);
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		return names;
	}
	//���� ������ ������ txt ���� �б�
	public HashMap<Integer, Integer> readCash(){
		HashMap<Integer, Integer> cash = new HashMap<>();
		try {
			File file = new File("Cash.txt");
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				String str = scan.nextLine();
				StringTokenizer token = new StringTokenizer(str, ":");
				cash.put(Integer.parseInt(token.nextToken()), Integer.parseInt(token.nextToken()));
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		return cash;
	}
	//���� ����Ʈ ������ txt ���� �б�
	public HashMap<String, ArrayList<Product>> readPayment() {
		HashMap<String, ArrayList<Product>> pays = new HashMap<>();
		try {
			File file = new File("PaymentList.txt");
			Scanner scan = new Scanner(file);
			while(scan.hasNextLine()) {
				ArrayList<Product> list = new ArrayList<>();
				String str = "";
				String code = scan.nextLine();
				while(scan.hasNextLine()) {
					str = scan.nextLine();
					if(str.equals("@"))
						break;
					StringTokenizer token = new StringTokenizer(str, "/");
					list.add(new Product(token.nextToken(), token.nextToken(), token.nextToken(), token.nextToken()));
				}
				pays.put(code, list);
			}
			scan.close();
		}catch(FileNotFoundException e) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return null;
		}
		return pays;
	}
	//���Ͽ� ���ο� �� �����
	public void writeFile(String file_name, ArrayList<String> contents) {
		File file = new File(file_name);
		FileWriter writer = null;
		BufferedWriter buf_writer = null;
		try {
			writer = new FileWriter(file, false);
			buf_writer = new BufferedWriter(writer);
			
			buf_writer.write("");
			for(int i=0; i<contents.size(); i++) {
				buf_writer.write(contents.get(i));
			}
			buf_writer.flush();
		}catch(IOException e) {
			System.out.println("���Ͼ��� ����");
		}finally {
			try {
				if(buf_writer != null)
					buf_writer.close();
				if(writer != null)
					writer.close();
			}catch(IOException e) {
				System.out.println("���Ϲ��� ����� ����");
			}
		}
	}
	//���Ͽ� ���ο� �� �߰�
	public void writeFile(String file_name, String contents) {
		File file = new File(file_name);
		FileWriter writer = null;
		BufferedWriter buf_writer = null;
		try {
			writer = new FileWriter(file, true);
			buf_writer = new BufferedWriter(writer);
			buf_writer.write(contents);
			buf_writer.write("\n");
			buf_writer.flush();
		}catch(IOException e) {
			System.out.println("���Ͼ��� ����");
		}finally {
			try {
				if(buf_writer != null)
					buf_writer.close();
				if(writer != null)
					writer.close();
			}catch(IOException e) {
				System.out.println("���Ϲ��� ����� ����");
			}
		}
	}
}
