package com.cai.workhourstracker.helper;

import java.math.BigDecimal;

public class MoneyCalculateUtils {
	public static BigDecimal convertToBigDecimal(Integer money){
		BigDecimal moneyAsBigDecimal = new BigDecimal(money);
		BigDecimal moneyRealValue = moneyAsBigDecimal.divide(new BigDecimal(100));
		
		return moneyRealValue;
	}
}
