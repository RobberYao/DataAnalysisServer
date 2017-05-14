package com.tcp.action.yao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface DataBaseUtil {

	public ArrayList<String> quaryByParamter(String tableName, String startTime, String endTime);// 选择时间、按表名查找时间（注：用于打印PDF时使用，查找lab_displayparamter*
																									// 表）

	public String findDisplayProbeNumberByInputProbNum(String inputProbNum);// 传入探头名，查找lab_disprobenumber，返回客户自定探头号。

	public String findDisplayTableNameByInputProbNum(String inputProbNum);// 传入探头名，查找lab_disprobenumber，返回该探头对应的显示表名。

	public String findInputTableNameByInputProbNum(String inputProbNum);// 传入探头名，查找lab_disprobenumber，返回该探头对应的原数据表名。

	public List findModifyNameByInputProbNum(String inputProbNum);// 传入探头名，查找lab_modify，返回STATUS为Y值的所有参数修改值。

	public void insertTable(String tableName, String probNum, String createdOn, String temperature, String humidity);// 插入数据，用于获取数据库 按探头名匹配表后插入原数据表（lab_inputparamter*）

	public void insertDisplayTable(String tableName, String inputTabName, String probNum, String createdOn,
			String temperature, String humidity, List<Map<String, Double>> modify);// 查询参数修改表后，修改校验值后传入display显示表中。

	public void insertSumInputTable(String probNum, String createdOn, String temperature, String humidity);// 插入数据至原数据汇总表

	public void insertSumDisplayTable(String inputTabName, String probNum, String createdOn, String temperature,
			String humidity, List<Map<String, Double>> modify);// 插入数据至显示数据汇总表
	
	public String getAvgTemperatureByCreatedon(String inputProbNum,String createdOn);//从原数据表中得到温度平均值
	
    public String OptimizedTemp(String inputTemp,String DisplayTemp);//数据优化
}
