package sw.pos;

public class CategoryInfo {
	private int last_num;//���� ī�װ� ���� ������ ��ǰ���� ����
	private String category_code;//ū ī�װ� �ڵ� 2�ڸ�
	
	public CategoryInfo(String category_code, int last_num) {
		this.category_code = category_code;
		this.last_num = last_num;
	}

	public int getLast_num() {
		return last_num;
	}

	public void addLast_num() {
		this.last_num++;
	}

	public String getCategory_code() {
		return category_code;
	}
	
}
