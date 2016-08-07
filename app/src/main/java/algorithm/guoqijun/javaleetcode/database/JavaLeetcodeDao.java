package algorithm.guoqijun.javaleetcode.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import algorithm.guoqijun.javaleetcode.been.ConstantValues;
import algorithm.guoqijun.javaleetcode.utils.LogUtils;

public class JavaLeetcodeDao {
	private JavaLeetcodeDatabase javaLeetcodeOpenHelper;

	//1,私有化构造方法
	private JavaLeetcodeDao(Context context) {
		javaLeetcodeOpenHelper = new JavaLeetcodeDatabase(context);
	}
	//2,声明一个当前类的对象
	private static JavaLeetcodeDao LeetcodeDao = null;
	//3,提供一个静态方法,如果当前类的对象为空,创建一个新的
	public static JavaLeetcodeDao getInstance(Context context){
		if(LeetcodeDao == null){
			LeetcodeDao = new JavaLeetcodeDao(context);
		}
		return LeetcodeDao;
	}
	/**增加一个条目
	 * @param title	java文件名
	 * @param packages	导入包
	 * @param tip		leetcode的提示
	 * @param answer	算法答案
	 */
	public void insert(String num ,String title,String packages,String tip,String answer){
		//1,开启数据库,准备做写入操作
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("num", num);
		values.put("title", title);
		values.put("packages", packages);
		values.put("tip", tip);
		values.put("answer", answer);
		db.insert("javaleetcode", null, values);
		
		db.close();
	}
	/**
	 * @return	数据库中数据的总条目个数,返回0代表没有数据或异常
	 */
	public int getCount(){
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();
		int count = 0;
		Cursor cursor = db.rawQuery("select count(*) from javaleetcode;", null);
		if(cursor.moveToNext()){
			count = cursor.getInt(0);
		}
		
		cursor.close();
		db.close();
		return count;
	}
	/**
	 * @param num	作为查询条件的电话号码
	 * @return	传入电话号码的拦截模式	1:短信	2:电话	3:所有	0:没有此条数据
	 */
	public String getName(int num){
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("javaleetcode", new String[]{"title"}, "num = ?", new String[]{num+""}, null, null,null);
		String name = null;
		if(cursor.moveToNext()){
			name = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return name;
	}
	/**
	 * @param filename	作为查询条件的算法名字
	 * @return	packages信息
	 */
	public String getPackageMessage(String filename){
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("javaleetcode", new String[]{"packages"}, "title = ?", new String[]{filename+""}, null, null,null);
		String packages = null;
		if(cursor.moveToNext()){
			packages = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return packages;
	}
	/**
	 * @param filename	作为查询条件的算法名字
	 * @return	packages信息
	 */
	public String getTipMessage(String filename){
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("javaleetcode", new String[]{"tip"}, "title = ?", new String[]{filename+""}, null, null,null);
		String tip = null;
		if(cursor.moveToNext()){
			tip = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return tip;
	}
	/**
	 * @param filename	作为查询条件的算法名字
	 * @return	packages信息
	 */
	public String getAnswerMessage(String filename){
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("javaleetcode", new String[]{"answer"}, "title = ?", new String[]{filename+""}, null, null,null);
		String answer = null;
		if(cursor.moveToNext()){
			answer = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return answer;
	}

	public ArrayList<String> searchBlurName(String s){
		ArrayList<String> fileNamesAll = new ArrayList<String>();
		SQLiteDatabase db = javaLeetcodeOpenHelper.getWritableDatabase();
		Cursor cursor = db.query("javaleetcode", new String[]{"title"}, "title like '%"+s +"%'", null, null, null,null);
		while(cursor.moveToNext()){
			fileNamesAll.add(cursor.getString(0));
			LogUtils.i(ConstantValues.TAG_DEBUG,"result:"+cursor.getString(0));
		}
		cursor.close();
		db.close();
		return fileNamesAll;
	}
}
