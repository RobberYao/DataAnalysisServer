package com.tcp.action.yao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Function {
	DataBaseUtilimpl dBaseUtilimpl = new DataBaseUtilimpl();
	private final double tempCheck = -10.00;
	private final double humCheck = 10.00;

	public static void main(String[] args) {
		Function function = new Function();
		for (int i = 0; i < 10; i++) {
			String str = "+YAV:0005AABB" + ",820 000 000 007 001 " + ",820 000 000 007 001 " + ",000 001 007 000 000 "
					+ ",000 001 008 000 000 " + ",000 000 004 000 000 " + ",000 000 008 001 003 "
					+ ",000 005 004 000 002 " + ",000 00C 00B 008 008 " + ",0 0,0 0,0 0 0 0,00"
					+ ",FF0203FF,V V V V V V V V" + ",8AD00001,X,EEFF";
			function.loadParamBySCM(str);
			String str1 = "+YAV:0005AABB" + ",822 000 000 007 001 " + ",920 000 000 007 001 " + ",000 001 007 000 000 "
					+ ",000 001 008 000 000 " + ",000 000 004 000 000 " + ",000 000 008 001 003 "
					+ ",000 005 004 000 002 " + ",000 00C 00B 008 008 " + ",0 0,0 0,0 0 0 0,00"
					+ ",FF0203FF,V V V V V V V V" + ",8AD00001,X,EEFF";
			function.loadParamBySCM(str1);
			String str2 = "+YAV:0005AABB" + ",821 000 000 007 001 " + ",990 000 000 007 001 " + ",000 001 007 000 000 "
					+ ",000 001 008 000 000 " + ",000 000 004 000 000 " + ",000 000 008 001 003 "
					+ ",000 005 004 000 002 " + ",000 00C 00B 008 008 " + ",0 0,0 0,0 0 0 0,00"
					+ ",FF0203FF,V V V V V V V V" + ",8AD00001,X,EEFF";
			function.loadParamBySCM(str2);
		}
	}

	/**
	 * 传入未解析数据，解析后存数据库（单片机用）
	 * 
	 * @param str
	 */
	public void loadParamBySCM(String str) {
		// System.out.println("-----Start LoadParamBySCM-----");
		long checkstartTime = System.currentTimeMillis();// 开始计时
		String inputProbeId = "";// 板号+端口号
		String createdon = "";// 采集时间（单片机端）
		String temperature = "";// 采集温度
		String avgTemprature = "";// 平均温度
		String displayTemprature = "";// 显示数据温度
		String humidity = "";// 采集湿度
		String displayProbeId = "";// 探头编号（客户定制）
		String displayTabName = "";// 显示表名
		String inputTabName = "";// 原数据表名
		List listmodify = new ArrayList();
		try {

			String[] paramterStr = SCMUtil.getArrayFromSCM(RegexUtil.getParams(str));// 将长数据按分号分割成数组

			for (int i = 0; i < paramterStr.length; i++) {
				String[] paramters = SCMUtil.getParamterFromArray(paramterStr[i]);
				inputProbeId = paramters[0];// 板号+端口号
				createdon = SCMUtil.getSimpledDateTime();
				temperature = paramters[1];
				humidity = paramters[2];
				// 查找该探头对应的原数据表名
				inputTabName = dBaseUtilimpl.findInputTableNameByInputProbNum(inputProbeId);
				// 查找显示表名
				displayTabName = dBaseUtilimpl.findDisplayTableNameByInputProbNum(inputProbeId);
				// 查找商户自定探头编号
				displayProbeId = dBaseUtilimpl.findDisplayProbeNumberByInputProbNum(inputProbeId);
				// 查找该探头的校准值
				listmodify = dBaseUtilimpl.findModifyNameByInputProbNum(inputProbeId);

				if (Double.valueOf(temperature) > tempCheck && Double.valueOf(humidity) > humCheck) {
					System.out.println(displayTabName + " 优化==== 原始 ："+temperature +"  ==> 显示：" + displayTemprature);
					System.out
							.println("\n" + inputProbeId + "   " + createdon + "   " + temperature + "   " + humidity);
					// 写入原数据表
					dBaseUtilimpl.insertTable(inputTabName, inputProbeId, createdon, temperature, humidity);
					// AVG for Temperture
					avgTemprature = dBaseUtilimpl.getAvgTemperatureByCreatedon(inputTabName, createdon);
					displayTemprature = dBaseUtilimpl.OptimizedTemp(temperature, avgTemprature);
					// 写入显示数据表
					dBaseUtilimpl.insertDisplayTable(displayTabName, inputProbeId, displayProbeId, createdon,
							displayTemprature, humidity, listmodify);
					// 写入原始数据汇总表
					dBaseUtilimpl.insertSumInputTable(inputProbeId, createdon, temperature, humidity);
					// 写入显示数据汇总表
					dBaseUtilimpl.insertSumDisplayTable(inputProbeId, displayProbeId, createdon, temperature, humidity,
							listmodify);
				}
			}
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			System.out.println("插入数据完毕耗时： " + Float.toString(seconds) + " seconds.");
		} catch (Exception e) {
			System.out.println("异常原因： " + e);
		}
	}

}
