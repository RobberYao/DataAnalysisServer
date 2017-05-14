package com.tcp.action.yao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * ����Ϊ���߳��࣬���ڷ����
 */
public class TcpServerThread implements Runnable {

	private Socket client = null;

	public TcpServerThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			// ��ȡSocket���������������ͻ��ˣ���Ƭ������������
			PrintStream out = new PrintStream(client.getOutputStream());
			// ��ȡSocket�����������������մӿͻ��ˣ���Ƭ�������͹���������
			BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			boolean flag = true;
			while (flag) {
				// ���մӿͻ��˷��͹���������
				String str = buf.readLine();
				if (str == null || "".equals(str)) {
					flag = false;
				} else {
					if ("bye".equals(str)) {
						flag = false;
					} else {
						// �����յ����ַ���ǰ�����echo�����͵���Ӧ�Ŀͻ���
						out.println("echo:" + str);
					}
				}
			}
			out.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}