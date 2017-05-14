package com.tcp.action.yao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DataBaseUtil {
	
	public ArrayList<String> quaryByParamter(String tableName, String startTime, String endTime);//ѡ��ʱ�䡢����������ʱ�䣨ע�����ڴ�ӡPDFʱʹ�ã�����lab_displayparamter* ��
	
	public void insertTable(String tableName, String probNum, String createdon,String temperature, String humidity);//�������ݣ����ڻ�ȡ���ݿ� ��̽ͷ��ƥ�������ԭ���ݱ�lab_inputparamter*��
	
	public void insertDisplayTable(String tableName,String inputTabName,String probNum, String createdon,String temperature, String humidity,List<Map<String, Double>> modify);//��ѯ�����޸ı���޸�У��ֵ����display��ʾ���С�
	
	public String findDisplayProbeNumberByInputProbNum(String inputProbNum);//����̽ͷ��������lab_disprobenumber�����ؿͻ��Զ�̽ͷ�š�
	
	public String findDisplayTableNameByInputProbNum(String inputProbNum);//����̽ͷ��������lab_disprobenumber�����ظ�̽ͷ��Ӧ����ʾ������
	
	public String findInputTableNameByInputProbNum(String inputProbNum);//����̽ͷ��������lab_disprobenumber�����ظ�̽ͷ��Ӧ��ԭ���ݱ�����
	
	public List findModifyNameByInputProbNum(String inputProbNum);//����̽ͷ��������lab_modify������STATUSΪYֵ�����в����޸�ֵ��
	
	public void insertSumInputTable(String probNum, String createdon, String temperature, String humidity);//����������ԭ���ݻ��ܱ�
	
	public void insertSumDisplayTable(String inputTabName,String probNum, String createdon,String temperature, String humidity,List<Map<String, Double>> modify);//������������ʾ���ݻ��ܱ�
	
}
