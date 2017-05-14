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
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		try {
			Class.forName(DRIVER); // classLoader,���ض�Ӧ����
			conn = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("Connection Mysql: " + Float.toString(seconds) + " seconds.");
		} catch (ClassNotFoundException e) {
			System.out.println("���Ӵ���");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("���Ӵ���");
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * ѡ��ʱ��ˡ�����������ʱ�䣨ע�����ڲ���lab_displayparamter* ��
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
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ

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
				String rsl = "#" + rs.getString(1) + "����" + rs.getString(2) + "����" + rs.getString(3) + "����"
						+ rs.getString(4) + "����" + rs.getString(5);
				// ���ʱ����ʪ���ֶν���
				// String rsl = "#" + rs.getString(1) + "����" + rs.getString(2) +
				// "����" + rs.getString(3) +
				// "����"+Base64.getFromBase64(rs.getString(4)) + "����"
				// +Base64.getFromBase64(rs.getString(5));

				rslist.add(rsl);
			}
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("QuaryTable" + tableName + " Cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rslist;
	}

	/**
	 * �������ݣ����ڻ�ȡ���ݿ� ��̽ͷ��ƥ�������ԭ���ݱ�lab_inputparamter*��
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
				throw new LabRunTimeException("����Ϊ�� ���쳣");
			}
			long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
			// ƥ����ʾ����ԭ���ݱ�
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
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("-----insertTable " + tableName + " cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����̽ͷ��������lab_disprobenumber�����ؿͻ��Զ�̽ͷ�š�
	 * 
	 * @param probNum
	 * @return
	 */
	@Override
	public String findDisplayProbeNumberByInputProbNum(String inputProbNum) {
		//System.out.println("Start findDisplayProbeNumberByProbNum");
		String displayprobenum = null;
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		// ��μ�飬�ǿ��ж�
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("��������Ϊ��");
			return null;
		}
		String sql = "select * from lab_disprobenumber where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (!rs.next()) {
				throw new LabRunTimeException(inputProbNum + " �Ҳ�����Ӧ�̻��Զ�̽ͷ�ţ���ʾ��");
			}
			// 1:ID��2��̽ͷ����3���̻��Զ�̽ͷ����4������ʱ�䡢5��ԭ���ݱ����ơ�6����ʾ������
			displayprobenum = rs.getString(3);
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
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
	 * ����̽ͷ��������lab_disprobenumber�����ظ�̽ͷ��Ӧ����ʾ������
	 * 
	 * @param probNum
	 * @return
	 */
	@Override
	public String findDisplayTableNameByInputProbNum(String inputProbNum) {
		//System.out.println("Start findDisplayProbeNumberByProbNum");
		String displayTableName = null;
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		// ��μ�飬�ǿ��ж�
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("��������Ϊ��");
			return null;
		}
		String sql = "select * from lab_disprobenumber where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (!rs.next()) {
				throw new LabRunTimeException(inputProbNum + " �Ҳ�����Ӧ����ʾ���ݱ���");
			}
			// 1:ID��2��̽ͷ����3���̻��Զ�̽ͷ����4������ʱ�䡢6��ԭ���ݱ����ơ�6����ʾ������
			displayTableName = rs.getString(6);
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
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
	 * ����̽ͷ��������lab_disprobenumber�����ظ�̽ͷ��Ӧ��ԭ���ݱ�����
	 * 
	 * @param probNum
	 * @return
	 */
	@Override
	public String findInputTableNameByInputProbNum(String inputProbNum) {
		//System.out.println("Start findInputTableNameByInputProbNum");
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		String InputTableName = null;
		// ��μ�飬�ǿ��ж�
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("��������Ϊ��");
			return null;
		}
		String sql = "select * from lab_disprobenumber where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";
		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (!rs.next()) {
				throw new LabRunTimeException(inputProbNum + " �Ҳ�����Ӧ��ԭ���ݱ�");
			}
			// 1:ID��2��̽ͷ����3���̻��Զ�̽ͷ����4:����ʱ�䡢5��ԭ���ݱ����ơ�6����ʾ������
			InputTableName = rs.getString(5);
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("-----FindInputTableName " + inputProbNum + " " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (LabRunTimeException e) {
			e.printStackTrace();
		}
		return InputTableName;
	}

	/**
	 * ����̽ͷ��������lab_modify������STATUSΪYֵ�����в����޸�ֵ��
	 * 
	 * @param inputProbNum
	 * @return
	 */
	public List findModifyNameByInputProbNum(String inputProbNum) {
		//System.out.println("Start findModifyNameByInputProbNum");
		String InputTableName = null;
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		List list = new ArrayList();
		List listValue = new ArrayList();
		// ��μ�飬�ǿ��ж�
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("��������Ϊ��");
			return listValue;
		}
		String sql = "select * from lab_modify where INPUTPROBENUMBER = " + "\'" + inputProbNum + "\'";

		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			// lab_modify��1��ID��2��̽ͷ����3���̻��Զ�̽ͷ����4�������޸�ʱ�䡢5��ԭ���ݱ�����
			List ls = resultSetToList(rs);
			listValue = getValueByColumnName(ls);

			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("-----FindModifyName by " + inputProbNum + " " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listValue;
	}

	/**
	 * ��ѯ���ݱ�����ת��Ϊlist
	 * 
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 */
	public List<Map> resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData(); // �õ������(rs)�Ľṹ��Ϣ�������ֶ������ֶ�����
		int columnCount = md.getColumnCount(); // ���ش� ResultSet �����е�����
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
	 * �����������lab_inputparamterƥ�䣬�����������true ��������ԭ���ݱ����ʾ���ݱ�
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
	 * �������ݿ��ѯ������������ֶ����жϸ��ֶ�ʹ��״̬��Y/N��������List
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
			// �ж�STATUS״̬����Y�������ֵ
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
			
			long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
			if (tableName == null) {
				throw new LabRunTimeException("����Ϊ�� ���쳣");
			}
			// ����У׼ֵ
			for (int i = 0; i < modify.size(); i++) {
				modifyhum = Double.valueOf(modify.get(i).get("DISHUMIDITY"));
				modifytemp = Double.valueOf(modify.get(i).get("DISTEMPERATURE"));
				// modifyill=modify.get(i).get("DISILLUMINATION");//����ֵ�����ޣ�illumination
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
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
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
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		// ��μ�飬�ǿ��ж�
		if (null == inputProbNum || inputProbNum.length() <= 0) {
			System.out.println("��������Ϊ��");
			return;
		}
		String sql = "select * from lab_disprobenumber order by ID DESC LIMIT 1";

		try {
			Connection conn = getConn();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				// 1:ID��2��̽ͷ����3���̻��Զ�̽ͷ����4����ʾ�����ơ�5��ԭ���ݱ�����
				InputTableName = rs.getString(4);
			}
			rs.close();
			pst.close();
			conn.close();
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println(Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}

	/*
	 * public void exec3(Connection conn) { try { conn.setAutoCommit(false);
	 * Long beginTime = System.currentTimeMillis(); // ����Ԥ����statement String sql
	 * =
	 * "INSERT  INTO lab_displayparamter (DISPROBENUMBER,CREATEDON,DISTEMPERATURE,DISHUMIDITY) VALUES (?,?,?,?);"
	 * ; PreparedStatement pst = conn.prepareStatement(sql); // 1���ѭ�� for (int i
	 * = 1; i <= 10000; i++) { pst.setString(1, "wn01"); pst.setString(2,
	 * "2016-01-01 00:00:00"); pst.setString(3, "35.5"); pst.setString(4,
	 * "36.5"); pst.addBatch(); // ÿ1000���ύһ�� if (i % 10000 == 0) {//
	 * �������ò�ͬ�Ĵ�С����50��100��500��1000�ȵ� pst.executeBatch(); conn.commit();
	 * pst.clearBatch(); } } pst.executeBatch(); conn.commit();
	 * pst.clearBatch();
	 * 
	 * Long endTime = System.currentTimeMillis();
	 * System.out.println("pst+batch��" + (endTime - beginTime) + "��");
	 * pst.close(); conn.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 */

	/**
	 * ��ԭ���ݻ��ܱ�
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
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		// ƥ����ʾ����ԭ���ݱ�
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
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("-----insertSumInputTable cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ����ʾ���ݻ��ܱ�
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
		long checkstartTime = System.currentTimeMillis();// ��ʼ��ʱ
		String sql = "";
		// ƥ����ʾ����ԭ���ݱ�
		Double modifyhum;
		Double modifytemp;
		String temperatureAfterModify = "";
		String humidityAfterModify = "";
		// ����У׼ֵ
		for (int i = 0; i < modify.size(); i++) {
			modifyhum = Double.valueOf(modify.get(i).get("DISHUMIDITY"));
			modifytemp = Double.valueOf(modify.get(i).get("DISTEMPERATURE"));
			// modifyill=modify.get(i).get("DISILLUMINATION");//����ֵ�����ޣ�illumination

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
			long checkendTime = System.currentTimeMillis();// ��ʱ����
			float seconds = (checkendTime - checkstartTime) / 1000F;// �����ʱ
			//System.out.println("-----insertSumInputTable cost: " + Float.toString(seconds) + " seconds.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
