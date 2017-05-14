package com.tcp.action.yao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Function {
	DataBaseUtilimpl dBaseUtilimpl = new DataBaseUtilimpl();
	private final double tempCheck = -10.00;
	private final double humCheck =10.00;

	public static void main(String[] args) {
		Function function = new Function();
		for (int i = 0; i < 1; i++) {
			String str = "+YAV:0005AABB" + ",000 000 000 007 001 " + ",000 000 000 007 001 " + ",007 001 007 000 000 "
					+ ",009 001 008 000 000 " + ",000 000 004 000 000 " + ",004 000 008 001 003 "
					+ ",001 005 004 000 002 " + ",008 00C 00B 008 008 " + ",0 0,0 0,0 0 0 0,00"
					+ ",FF0203FF,V V V V V V V V" + ",8AD00001,X,EEFF";
			function.loadParamBySCM(str);
		}
	}

	/**
	 * ����δ�������ݣ�����������ݿ⣨��Ƭ���ã�
	 * 
	 * @param str
	 */
	public void loadParamBySCM(String str) {
		//System.out.println("-----Start LoadParamBySCM-----");
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		String inputProbeId = "";// ���+�˿ں�
		String createdon = "";// �ɼ�ʱ�䣨��Ƭ���ˣ�
		String temperature = "";// �ɼ��¶�
		String humidity = "";// �ɼ�ʪ��
		String displayProbeId = "";// ̽ͷ��ţ��ͻ����ƣ�
		String displayTabName = "";// ��ʾ����
		String inputTabName = "";// ԭ���ݱ���
		List listmodify = new ArrayList();
		try {

			String[] paramterStr = SCMUtil.getArrayFromSCM(RegexUtil.getParams(str));// �������ݰ��ֺŷָ������

			for (int i = 0; i < paramterStr.length; i++) {
				String[] paramters = SCMUtil.getParamterFromArray(paramterStr[i]);
				inputProbeId = paramters[0];// ���+�˿ں�
				createdon = SCMUtil.getSimpledDateTime();
				temperature = paramters[1];
				humidity = paramters[2];
				// ���Ҹ�̽ͷ��Ӧ��ԭ���ݱ���
				inputTabName = dBaseUtilimpl.findInputTableNameByInputProbNum(inputProbeId);
				// ������ʾ����
				displayTabName = dBaseUtilimpl.findDisplayTableNameByInputProbNum(inputProbeId);
				// �����̻��Զ�̽ͷ���
				displayProbeId = dBaseUtilimpl.findDisplayProbeNumberByInputProbNum(inputProbeId);
				// ���Ҹ�̽ͷ��У׼ֵ
				listmodify = dBaseUtilimpl.findModifyNameByInputProbNum(inputProbeId);
				if (Double.valueOf(temperature) > tempCheck && Double.valueOf(humidity) > humCheck) {
					System.out.println("\n" + inputProbeId + "   " + createdon + "   " + temperature + "   " + humidity);
					// д��ԭ���ݱ�
					dBaseUtilimpl.insertTable(inputTabName, inputProbeId, createdon, temperature, humidity);
					// д����ʾ���ݱ�
					dBaseUtilimpl.insertDisplayTable(displayTabName, inputProbeId, displayProbeId, createdon,
							temperature, humidity, listmodify);
					// д��ԭʼ���ݻ��ܱ�
					dBaseUtilimpl.insertSumInputTable(inputProbeId, createdon, temperature, humidity);
					// д����ʾ���ݻ��ܱ�
					dBaseUtilimpl.insertSumDisplayTable(inputProbeId, displayProbeId, createdon, temperature, humidity,
							listmodify);
				}
			}

			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			System.out.println("����������Ϻ�ʱ�� " + Float.toString(seconds) + " seconds.");
		} catch (Exception e) {
			System.out.println("�쳣ԭ�� " + e);
		}
	}

}
