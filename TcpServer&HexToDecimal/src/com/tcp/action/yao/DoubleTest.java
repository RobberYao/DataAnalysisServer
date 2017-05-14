package com.tcp.action.yao;


import java.math.BigDecimal;



public class DoubleTest {
	// 默认除法运算精度，小数点后2位
	private static final Integer DEF_DIV_SCALE = 2;

	public static void main(String[] args) {

		Double x = 1.0;
		Double y = 3.0;

		System.out.println(getDoubleToStringValueByBigDecimalDiv(x, y));

		System.out.println(x + y);

	}

	/**
	 * Double类型加法运算，返回String类型（使用BigDecimal运算）
	 * 
	 * @param x
	 * @param y
	 * @return x+y
	 */
	public static String getDoubleToStringValueByBigDecimalAdd(Double x, Double y) {

		BigDecimal x1 = new BigDecimal(Double.toString(x));
		BigDecimal y1 = new BigDecimal(Double.toString(y));

		return x1.add(y1).toString();
	}

	/**
	 * Double类型减法运算（x-y），返回String类型（使用BigDecimal运算）
	 * 
	 * @param x
	 * @param y
	 * @return x-y
	 */
	public static String getDoubleToStringValueByBigDecimalSub(Double x, Double y) {

		BigDecimal x1 = new BigDecimal(Double.toString(x));
		BigDecimal y1 = new BigDecimal(Double.toString(y));

		return x1.subtract(y1).toString();

	}

	/**
	 * Double类型乘法运算（x*y），返回String类型（使用BigDecimal运算）
	 * 
	 * @param x
	 * @param y
	 * @return x*y
	 */
	public static String getDoubleToStringValueByBigDecimalMul(Double x, Double y) {

		BigDecimal x1 = new BigDecimal(Double.toString(x));
		BigDecimal y1 = new BigDecimal(Double.toString(y));

		return x1.multiply(y1).toString();

	}

	public static String getDoubleToStringValueByBigDecimalDiv(Double x, Double y){
		
		BigDecimal x1 = new BigDecimal(Double.toString(x));
		BigDecimal y1 = new BigDecimal(Double.toString(y));
		System.out.println(x1.divide(y1));
		
		return x1.divide(y1,2,BigDecimal.ROUND_HALF_DOWN).toString();
		
	}
	
	
	
	
}
