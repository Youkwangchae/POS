package sw.pos;

public class NameInfo {
	private int last_num, epd_value;//���� ���� �̸����� ������ ��ǰ ����, ������� ������
	private String name_code;//��ǰ�̸��� ���� ��ǰ �ڵ� 4�ڸ�
	
	public NameInfo(String name_code, int last_num, int epd_value) {
		this.name_code = name_code;
		this.last_num = last_num;
		this.epd_value = epd_value;
	}

	public int getLast_num() {
		return last_num;
	}

	public void addLast_num() {
		this.last_num++;
	}

	public int getEpd_value() {
		return epd_value;
	}

	public String getName_code() {
		return name_code;
	}
	
}
