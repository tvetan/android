package com.cai.workhourstracker.helper;

import java.math.BigDecimal;

public class MoneyCalculateUtils {
	public static BigDecimal convertToBigDecimal(Integer money){
		BigDecimal moneyAsBigDecimal = new BigDecimal(money);
		BigDecimal moneyRealValue = moneyAsBigDecimal.divide(new BigDecimal(100));
		
		return moneyRealValue;
	}
	
	public static BigDecimal moneyEarned(double workTimeHours, Integer baseRate) {
		BigDecimal baseRateRealValue = convertToBigDecimal(baseRate);		
		BigDecimal moneyEarned = (new BigDecimal(workTimeHours)).multiply(baseRateRealValue);
		
		return moneyEarned;
	}
}
