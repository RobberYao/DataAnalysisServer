package com.tcp.action.yao;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

	public static void main(String[] args) throws Exception {
		// �������20006�˿ڼ����ͻ��������TCP����
		// IP��ַĬ��Ϊ127.0.0.1
		ServerSocket server = new ServerSocket(20006);// �˿ں�
		Socket client = null;
		boolean f = true;
		while (f) {
			// �ȴ��ͻ��˵����ӣ����û�л�ȡ����
			client = server.accept();
			System.out.println("��ͻ������ӳɹ���");
			// Ϊÿ���ͻ������ӿ���һ���߳�
			new Thread(new TcpServerThread(client)).start();
		}
		server.close();
	}
}