package algorithm.guoqijun.javaleetcode.utils;
import android.util.Log;

public class LogUtils {

	public static boolean isDebug = true;
	public static void i(String tag, String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
	public static void i(Object tag, String msg){
		if(isDebug){
			Log.i(tag.getClass().getSimpleName(), msg);
		}
	}
	public static void e(String tag, String msg){
		if(isDebug){
			Log.i(tag, msg);
		}
	}
}
