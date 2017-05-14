package com.tcp.action.yao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

public class DataBaseUtilimpl implements DataBaseUtil {

	public static final String URL = "jdbc:mysql://localhost:3306/lab";
	public static final String USER = "root";
	public static final String PASSWORD = "";
	public static final String DRIVER = "com.mysql.jdbc.Driver";
	public static final String DISHUMIDITY = "DISHUMIDITY";
	public static final String DISTEMPERATURE = "DISTEMPERATURE";

	// public static final String ILLUMINATION ="DISILLUMINATION";
	public static void main(String[] args) {
		DataBaseUtilimpl dbBaseUtilimpl = new DataBaseUtilimpl();
		// System.out.println(dbBaseUtilimpl.findModifyNameByInputProbNum("wn02"));
		// dbBaseUtilimpl.insertNewTableIntoLab_disprobenumber("wn02");
		// dbBaseUtilimpl.findDisplayProbeNumberByInputProbNum("wn01");
		Connection conn = getConn();

	}

	public static Connection getConn() {
		// String driver = "com.mysql.jdbc.Driver";
		// String url =
		// "jdbc:mysql://127.0.0.1:3306/mysql?characterEncoding=utf8&useSSL=false";
		// String username = "root";
		// String password = "password";
		Connection conn = null;
		long checkstartTime = System.currentTimeMillis();// 开始计时
		try {
			Class.forName(DRIVER); // classLoader,加载对应驱动
			conn = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("Connection Mysql: " + Float.toString(seconds) + " seconds.");
		} catch (ClassNotFoundException e) {
			System.out.println("连接错误");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("连接错误");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 选择时间端、按表名查找时间（注：用于查找lab_displayparamter* 表）
	 * 
	 * @param tableName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@Override
	public ArrayList<String> quaryByParamter(String tableName, String startTime, String endTime) {
		//System.out.println("Start quaryByParamter...");
		ArrayList<String> rslist = new ArrayList();
		long checkstartTime = System.currentTimeMillis();// 开始计时

		// String tableName="lab_displayparamter";
		// String startTime="\'"+"2016-12-10 00:00:00"+"\'";
		// String endTime="\'"+"2016-12-11 00:00:00"+"\'";

		String sql = "select * from " + tableName + " where createdon between " + "\'" + startTime + "\'" + " and "
				+ "\'" + endTime + "\'";
		try {
			// Class.forName(DRIVER).newInstance();
			// Connection conn = DriverManager.getConnection(URL, USER,
			// PASSWORD);
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				String rsl = "#" + rs.getString(1) + "——" + rs.getString(2) + "——" + rs.getString(3) + "——"
						+ rs.getString(4) + "——" + rs.getString(5);
				// 输出时对温湿度字段解码
				// String rsl = "#" + rs.getString(1) + "——" + rs.getString(2) +
				// "——" + rs.getString(3) +
				// "——"+Base64.getFromBase64(rs.getString(4)) + "——"
				// +Base64.getFromBase64(rs.getString(5));

				rslist.add(rsl);
			}
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("QuaryTable" + tableName + " Cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rslist;
	}

	/**
	 * 插入数据，用于获取数据库 按探头名匹配表后插入原数据表（lab_inputparamter*）
	 * 
	 * @param tableName
	 * @param probNum
	 * @param temperature
	 * @param humidity
	 * @return
	 */
	@Override
	public void insertTable(String tableName, String probNum, String createdon, String temperature, String humidity) {
		//System.out.println("Start insertTable");
		String sql = "";
		try {
			if (tableName == null) {
				throw new LabRunTimeException("表名为空 抛异常");
			}
			long checkstartTime = System.currentTimeMillis();// 开始计时
			// 匹配显示表与原数据表
			sql = "INSERT  INTO " + tableName+ "(INPUTPROBENUMBER,CREATEDON,INPUTTEMPERATURE,INPUTHUMIDITY) VALUES (?,?,?,?);";
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, probNum);
			pst.setString(2, createdon);
			pst.setString(3, temperature);
			pst.setString(4, humidity);
			int rs1 = pst.executeUpdate();
			if (rs1 != 0) {
				//System.out.println("Success insert " + tableName);
			}
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("-----insertTable " + tableName + " cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 传入探头名，查找lab_disprobenumber，返回客户自定探头号。
	 * 
	 * @param probNum
	 * @return
	 */
	@Override
	public String findDisplayProbeNumberByInputProbNum(String inputProbNum) {
		//System.out.println("Start findDisplayProbeNumberByProbNum");
		String displayprobenum = null;
		long checkstartTime = System.currentTimeMillis();// 开始计时
		// 入参检查，非空判断
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("参数不能为空");
			return null;
		}
		String sql = "select * from lab_disprobenumber where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (!rs.next()) {
				throw new LabRunTimeException(inputProbNum + " 找不到对应商户自定探头号（显示）");
			}
			// 1:ID、2：探头名、3：商户自定探头名、4、创建时间、5：原数据表名称、6：显示表名称
			displayprobenum = rs.getString(3);
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println(
				//	"-----FindDisProbeNumber " + displayprobenum + " " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}
		return displayprobenum;
	}

	/**
	 * 传入探头名，查找lab_disprobenumber，返回该探头对应的显示表名。
	 * 
	 * @param probNum
	 * @return
	 */
	@Override
	public String findDisplayTableNameByInputProbNum(String inputProbNum) {
		//System.out.println("Start findDisplayProbeNumberByProbNum");
		String displayTableName = null;
		long checkstartTime = System.currentTimeMillis();// 开始计时
		// 入参检查，非空判断
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("参数不能为空");
			return null;
		}
		String sql = "select * from lab_disprobenumber where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (!rs.next()) {
				throw new LabRunTimeException(inputProbNum + " 找不到对应的显示数据表名");
			}
			// 1:ID、2：探头名、3：商户自定探头名、4、创建时间、6：原数据表名称、6：显示表名称
			displayTableName = rs.getString(6);
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println(
				//	"-----FindDisplayTableName: " + displayTableName + " " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}
		return displayTableName;
	}

	/**
	 * 传入探头名，查找lab_disprobenumber，返回该探头对应的原数据表名。
	 * 
	 * @param probNum
	 * @return
	 */
	@Override
	public String findInputTableNameByInputProbNum(String inputProbNum) {
		//System.out.println("Start findInputTableNameByInputProbNum");
		long checkstartTime = System.currentTimeMillis();// 开始计时
		String InputTableName = null;
		// 入参检查，非空判断
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("参数不能为空");
			return null;
		}
		String sql = "select * from lab_disprobenumber where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (!rs.next()) {
				throw new LabRunTimeException(inputProbNum + " 找不到对应的原数据表");
			}
			// 1:ID、2：探头名、3：商户自定探头名、4:创建时间、5：原数据表名称、6：显示表名称
			InputTableName = rs.getString(5);
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("-----FindInputTableName " + inputProbNum + " " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}
		return InputTableName;
	}

	/**
	 * 传入探头名，查找lab_modify，返回STATUS为Y值的所有参数修改值。
	 * 
	 * @param inputProbNum
	 * @return
	 */
	public List findModifyNameByInputProbNum(String inputProbNum) {
		//System.out.println("Start findModifyNameByInputProbNum");
		String InputTableName = null;
		long checkstartTime = System.currentTimeMillis();// 开始计时
		List list = new ArrayList();
		List listValue = new ArrayList();
		// 入参检查，非空判断
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("参数不能为空");
			return listValue;
		}
		String sql = "select * from lab_modify where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";

		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			// lab_modify表：1：ID、2：探头名、3：商户自定探头名、4：参数修改时间、5：原数据表名称
			List ls = resultSetToList(rs);
			listValue = getValueByColumnName(ls);

			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("-----FindModifyName by " + inputProbNum + " " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listValue;
	}

	/**
	 * 查询数据表结果集转换为list
	 * 
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	public List<Map> resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List<Map> list = new ArrayList();
		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	/**
	 * 传入表名，与lab_inputparamter匹配，如果包含返回true 用于区别原数据表和显示数据表
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean regularTableName(String tableName) {
		boolean flag = true;
		String targeTableName = "lab_inputparamter";
		flag = tableName.contains(targeTableName);
		if (!flag) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 传入数据库查询结果集，根据字段名判断该字段使用状态（Y/N）输出结果List
	 * 
	 * @param ls
	 * @return
	 */
	public List getValueByColumnName(List ls) {
		String value = "";
		List list = new ArrayList<Map<String, Double>>();
		Map modifydata = new HashMap();
		Iterator it = ls.iterator();
		while (it.hasNext()) {
			Map hm = (Map) it.next();
			// 判断STATUS状态，是Y则输出该值
			value = hm.get("STATUS") + "";
			if ("Y".equals(value)) {
				modifydata.put(hm.get("MODIFYPARAMTER"), hm.get("MODIFYNUMBER"));
			}
		}
		list.add(modifydata);
		return list;
	}

	/**
	 * 
	 */
	@Override
	public void insertDisplayTable(String tableName,String inputTabName, String probNum, String createdon, String temperature,
			String humidity, List<Map<String, Double>> modify) {
		//System.out.println("Start insertDisplayTable");
		Double modifyhum;
		Double modifytemp;
		String temperatureAfterModify ="";
		String humidityAfterModify = "";
		try {
			
			long checkstartTime = System.currentTimeMillis();// 开始计时
			if (tableName == null) {
				throw new LabRunTimeException("表名为空 抛异常");
			}
			// 遍历校准值
			for (int i = 0; i < modify.size(); i++) {
				modifyhum = Double.valueOf(modify.get(i).get("DISHUMIDITY"));
				modifytemp = Double.valueOf(modify.get(i).get("DISTEMPERATURE"));
				// modifyill=modify.get(i).get("DISILLUMINATION");//光照值（暂无）illumination
				temperatureAfterModify = DoubleTest.getDoubleToStringValueByBigDecimalAdd(modifytemp,
						Double.parseDouble(temperature));
				humidityAfterModify = DoubleTest.getDoubleToStringValueByBigDecimalAdd(modifyhum,
						Double.parseDouble(humidity));
			}
			String sql = "INSERT  INTO " + tableName+ "(INPUTPROBENUMBER,DISPROBENUMBER,CREATEDON,DISTEMPERATURE,DISHUMIDITY) VALUES (?,?,?,?,?);";
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, inputTabName);
			pst.setString(2, probNum);
			pst.setString(3, createdon);
			pst.setString(4, temperature);
			pst.setString(5, humidity);
			int rs1 = pst.executeUpdate();
			if (rs1 != 0) {
				//System.out.println("Success insert " + tableName);
			}
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("-----insertTable " + tableName + " cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}
		
		
		
		
		//insertTable(tableName,inputTabName, probNum, createdon, temperatureAfterModify, humidityAfterModify);
	}

	public void insertNewTableIntoLab_disprobenumber(String inputProbNum) {
		//System.out.println("Start findDisplayProbeNumberByProbNum");
		String InputTableName = null;
		long checkstartTime = System.currentTimeMillis();// 开始计时
		// 入参检查，非空判断
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("参数不能为空");
			return;
		}
		String sql = "select * from lab_disprobenumber order by ID DESC LIMIT 1";

		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				// 1:ID、2：探头名、3：商户自定探头名、4：显示表名称、5：原数据表名称
				InputTableName = rs.getString(4);
			}
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println(Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	/*
	 * public void exec3(Connection conn) { try { conn.setAutoCommit(false);
	 * Long beginTime = System.currentTimeMillis(); // 构造预处理statement String sql
	 * =
	 * "INSERT  INTO lab_displayparamter (DISPROBENUMBER,CREATEDON,DISTEMPERATURE,DISHUMIDITY) VALUES (?,?,?,?);"
	 * ; PreparedStatement pst = conn.prepareStatement(sql); // 1万次循环 for (int i
	 * = 1; i <= 10000; i++) { pst.setString(1, "wn01"); pst.setString(2,
	 * "2016-01-01 00:00:00"); pst.setString(3, "35.5"); pst.setString(4,
	 * "36.5"); pst.addBatch(); // 每1000次提交一次 if (i % 10000 == 0) {//
	 * 可以设置不同的大小；如50，100，500，1000等等 pst.executeBatch(); conn.commit();
	 * pst.clearBatch(); } } pst.executeBatch(); conn.commit();
	 * pst.clearBatch();
	 * 
	 * Long endTime = System.currentTimeMillis();
	 * System.out.println("pst+batch：" + (endTime - beginTime) + "秒");
	 * pst.close(); conn.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 */

	/**
	 * 插原数据汇总表
	 * 
	 * @param tableName
	 * @param probNum
	 * @param temperature
	 * @param humidity
	 * @return
	 */
	@Override
	public void insertSumInputTable(String probNum, String createdon, String temperature, String humidity) {
		//System.out.println("Start insertSumInputTable");
		String sql = "";
		long checkstartTime = System.currentTimeMillis();// 开始计时
		// 匹配显示表与原数据表
		sql = "INSERT  INTO lab_inputparamter (INPUTPROBENUMBER,CREATEDON,INPUTTEMPERATURE,INPUTHUMIDITY) VALUES (?,?,?,?);";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, probNum);
			pst.setString(2, createdon);
			pst.setString(3, temperature);
			pst.setString(4, humidity);
			int rs1 = pst.executeUpdate();
			if (rs1 != 0) {
				//System.out.println("Success insertSumInputTable ");
			}
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("-----insertSumInputTable cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 插显示数据汇总表
	 * 
	 * @param tableName
	 * @param probNum
	 * @param temperature
	 * @param humidity
	 * @return
	 */
	@Override
	public void insertSumDisplayTable(String inputTabName,String probNum, String createdon, String temperature, String humidity,
			List<Map<String, Double>> modify) {
		//System.out.println("Start insertSumDisplayTable");
		long checkstartTime = System.currentTimeMillis();// 开始计时
		String sql = "";
		// 匹配显示表与原数据表
		Double modifyhum;
		Double modifytemp;
		String temperatureAfterModify = "";
		String humidityAfterModify = "";
		// 遍历校准值
		for (int i = 0; i < modify.size(); i++) {
			modifyhum = Double.valueOf(modify.get(i).get("DISHUMIDITY"));
			modifytemp = Double.valueOf(modify.get(i).get("DISTEMPERATURE"));
			// modifyill=modify.get(i).get("DISILLUMINATION");//光照值（暂无）illumination

			temperatureAfterModify = DoubleTest.getDoubleToStringValueByBigDecimalAdd(modifytemp,
					Double.parseDouble(temperature));
			humidityAfterModify = DoubleTest.getDoubleToStringValueByBigDecimalAdd(modifyhum,
					Double.parseDouble(humidity));
		}
		sql = "INSERT  INTO  lab_displayparamter (INPUTPROBENUMBER,DISPROBENUMBER,CREATEDON,DISTEMPERATURE,DISHUMIDITY) VALUES (?,?,?,?,?);";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setString(1, inputTabName);
			pst.setString(2, probNum);
			pst.setString(3, createdon);
			pst.setString(4, temperatureAfterModify);
			pst.setString(5, humidityAfterModify);
			int rs1 = pst.executeUpdate();
			if (rs1 != 0) {
				//System.out.println("Success insertSumDisplayTable ");
			}
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// 计时结束
			float seconds = (checkendTime - checkstartTime) / 1000F;// 计算耗时
			//System.out.println("-----insertSumInputTable cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
