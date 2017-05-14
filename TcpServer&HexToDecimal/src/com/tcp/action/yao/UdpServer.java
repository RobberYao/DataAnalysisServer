package com.tcp.action.yao;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(808);//�˿ں�9080��IP��ַĬ��Ϊ����127.0.0.1
		DatagramPacket packet = null;
		byte[] data = null;
		int count = 0;
		System.out.println("***���������������ȴ���������***");
		while (true) {
			data = new byte[1024];// �����ֽ����飬ָ�����յ����ݰ��Ĵ�С
			packet = new DatagramPacket(data, data.length);
			socket.receive(packet);// �˷����ڽ��յ����ݱ�֮ǰ��һֱ����
			Thread thread = new Thread(new UdpServerThread(socket, packet));
			thread.start();
			count++;
			//System.out.println("�������˱����ӹ��Ĵ�����" + count);
			InetAddress address = packet.getAddress();
			//System.out.println("��ǰ�ͻ��˵�IPΪ��" + address.getHostAddress());
		}
	}
}
