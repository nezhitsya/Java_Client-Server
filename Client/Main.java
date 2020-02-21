package ä��Ŭ���̾�Ʈ;

/* 
 * ������ �����Ű�鼭 Ŭ���̾�Ʈ�� ������Ѽ� ������ Ŭ���̾�Ʈ�� �ٷ� ����� �� �ִ� MainŬ����
 * ������ ���������� ������ �����Ű�� Ŭ���̾�Ʈ�� �����Ű��,
 * ������ ���������� IOException�� �߻��� catch������ �� Ŭ���̾�Ʈ�� �ϳ� �� ��������ش�.
 * 
 * try�� : ������ �������� �� client�� �����Ų��. (������ ���������� catch������ �Ѿ��)
 * catch�� : ������ �������� �� client�� �����Ų��.
 */
import java.io.IOException;
import java.net.ServerSocket;

import ä�ü���.Server;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try{
			ServerSocket socket = new ServerSocket(1234);			// 1234��Ʈ�� ������ �����غ���.
																	// �̹� 1234��Ʈ�� ������ϰ�� catch������ �Ѿ��.
			
			socket.close();											// ������� �ƴϸ�, ������ �ݾ��ְ� ServerŬ������ �����Ͽ�
			Server server=new Server();								// 1234��Ʈ�� ������ �������ش�.
			
			Client.client = new Client();							// Ŭ���̾�Ʈ�� ��������ش�.
			
		} catch (IOException e) {
			// ������ ���������� �� ������ ������ �� �ִ� Ŭ���̾�Ʈ�� ��������ش�.
			Client.client = new Client();
		}
	}
}