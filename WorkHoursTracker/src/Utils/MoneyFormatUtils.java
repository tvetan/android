package Utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MoneyFormatUtils {
	public static String toLocaleCurrencyFormatFromInteger(Integer value){
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		
		double valueDoubleFormat =((double) value)  / 100;
		String valueFormated = format.format(valueDoubleFormat);
		
		return valueFormated;
	}
	
	public static String toLocaleCurrencyFormatFromBigDecimal(BigDecimal value){
		NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		
		return format.format(value);
	}
}
