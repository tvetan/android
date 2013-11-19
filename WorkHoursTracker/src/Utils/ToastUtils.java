package Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void makeShortToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void makeLongToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}
