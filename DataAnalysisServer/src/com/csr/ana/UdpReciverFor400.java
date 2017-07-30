package com.csr.ana;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;



import com.fleety.util.pool.timer.FleetyTimerTask;

public class UdpReciverFor400 extends FleetyTimerTask {
	
	private BlockingQueue<String> reciverQueue;
	DatagramSocket socket = null;
	DatagramPacket packet = null;

	public UdpReciverFor400(BlockingQueue<String> reciverQueue, DatagramSocket socket, DatagramPacket packet) {
		this.reciverQueue = reciverQueue;
		this.socket = socket;
		this.packet = packet;
	}

	@Override
	public void run() {
		while (true) {
			try {
				//long checkstartTime = System.currentTimeMillis();// 解析�?��计时
				System.out.println("receive!!!");
				String info = null;
				InetAddress address = null;
				int port = 808;// 返回客户端时传入的服务器监听端口
				byte[] data2 = null;
				DatagramPacket packet2 = null;
				byte[] data = new byte[1024];// 创建字节数组，指定接收的数据包的大小
				packet = new DatagramPacket(data, data.length);
				socket.receive(packet);// 此方法在接收到数据报之前会一直阻�?			
				String messageInfo = new String(packet.getData(), 0, packet.getLength());
				reciverQueue.add(messageInfo);// 加入队列
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
