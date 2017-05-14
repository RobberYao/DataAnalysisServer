package com.tcp.action.yao;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServer {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(808);//端口号9080、IP地址默认为本地127.0.0.1
		DatagramPacket packet = null;
		byte[] data = null;
		int count = 0;
		System.out.println("***服务器端启动，等待发送数据***");
		while (true) {
			data = new byte[1024];// 创建字节数组，指定接收的数据包的大小
			packet = new DatagramPacket(data, data.length);
			socket.receive(packet);// 此方法在接收到数据报之前会一直阻塞
			Thread thread = new Thread(new UdpServerThread(socket, packet));
			thread.start();
			count++;
			//System.out.println("服务器端被连接过的次数：" + count);
			InetAddress address = packet.getAddress();
			//System.out.println("当前客户端的IP为：" + address.getHostAddress());
		}
	}
}
