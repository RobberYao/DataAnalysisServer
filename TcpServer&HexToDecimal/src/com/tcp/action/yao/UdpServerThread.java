package com.tcp.action.yao;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpServerThread implements Runnable {
    DatagramSocket socket = null;
    DatagramPacket packet = null;
    Function fc=new Function();
   
    public UdpServerThread(DatagramSocket socket,DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }
	@Override
	public void run() {
		 String info = null;
	        InetAddress address = null;
	        int port = 808;//返回客户端时传入的服务器监听端口
	        byte[] data2 = null;
	        DatagramPacket packet2 = null;
	        try {
	            info = new String(packet.getData(), 0, packet.getLength());
	            System.out.println("服务器收到单片机："+info);
	            fc.loadParamBySCM(info);
	            address = packet.getAddress();
	            port = packet.getPort();
	            data2 = "Get Message!".getBytes();
	            packet2 = new DatagramPacket(data2, data2.length, address, port);
	            socket.send(packet2);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        //socket.close();不能关闭         
	}

}
