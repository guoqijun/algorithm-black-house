package algorithm.guoqijun.javaleetcode.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JavaLeetcodeDatabase extends SQLiteOpenHelper {
 
	public JavaLeetcodeDatabase(Context context) {
		super(context, "javaleetcode.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table javaleetcode " +
				"(_id integer primary key autoincrement , num varchar(10),title varchar(50), packages varchar(1000), tip varchar(3000), answer varchar(65500));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
