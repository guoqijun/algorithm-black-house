package algorithm.guoqijun.javaleetcode.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import algorithm.guoqijun.javaleetcode.R;
import algorithm.guoqijun.javaleetcode.been.ConstantValues;
import algorithm.guoqijun.javaleetcode.database.JavaLeetcodeDao;
import algorithm.guoqijun.javaleetcode.utils.LogUtils;
import algorithm.guoqijun.javaleetcode.utils.UtilsSp;
import info.abdolahi.CircularMusicProgressBar;

public class SplashActivity extends Activity {
	private static final String TAG = "guoqijun";
	private static final int ENTER_HOME = 100;
	private static final int UPDATE_UI = 200;
	private int progressBarValue = 1;
	private String[] filesName =null;
	private String[] temp =null;
	private boolean isSetDB;
	JavaLeetcodeDao javaLeetcodeDao = JavaLeetcodeDao.getInstance(this);
	CircularMusicProgressBar progressBar;
	LinearLayout ll_plash;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what){
				case UPDATE_UI:
					progressBar.setValue(progressBarValue);
					break;
				case  ENTER_HOME:
					enterHome();
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		progressBar = (CircularMusicProgressBar) findViewById(R.id.album_art);
		ll_plash = (LinearLayout)findViewById(R.id.ll_plash);
		progressBar.setValue(progressBarValue);
		JavaLeetcodeDao javaLeetcodeDao = JavaLeetcodeDao.getInstance(this);
		initAnimation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isSetDB = UtilsSp.getBoolean(this, ConstantValues.SPLASH_DATA, false);
		if (isSetDB) {
			//set database,装逼一下再进入主界面
			new Thread(){
				@Override
				public void run() {
					for (int i=1;i<100;i++){
						try {
							Thread.sleep(30+new Random().nextInt(5));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						progressBarValue =i;
						Message msg = Message.obtain();
						msg.what = UPDATE_UI;
						mHandler.sendMessage(msg);
					}
					Message msg = Message.obtain();
					msg.what = ENTER_HOME;
					mHandler.sendMessage(msg);
				}
			}.start();
		}else{
			//加载数据库
			new Thread(){
				@Override
				public void run() {
					try {
						//说明第一次进入，初始化java算法题的数据库
						initDB();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.what = ENTER_HOME;
					mHandler.sendMessage(msg);
				}
			}.start();
		}
	}
	/**
	 * init database for java sources
	 * @throws IOException 
	 */
	private void initDB() throws IOException {
		// 加载数据库，asset文件下的java文件
		InputStreamReader inputReader =null;
		BufferedReader bufReader = null;
		String line = "";

		//得到资源文件的所有文件的列表
		temp = getAssets().list("");
		//消灭ShareSDK.xml文件
		for (int i = 0,j=0; i < temp.length; i++) {
			if (temp[i]!="ShareSDK.xml"){
				filesName[j]=temp[i];
				j++;
			}
		}
		//打印总文件的长度
		//LogUtils.i(TAG, filesName.length+"");
		//循环打开每一个java文件，整理数据导入数据库
		for (int i = 0; i < filesName.length; i++) {
			progressBarValue =i*100/filesName.length;
			Message msg = Message.obtain();
			msg.what = UPDATE_UI;
			mHandler.sendMessage(msg);
			inputReader = new InputStreamReader( getResources().getAssets().open(filesName[i]) );
            bufReader = new BufferedReader(inputReader);
            StringBuffer Result = new StringBuffer();
            while((line = bufReader.readLine()) != null)
            	Result.append(line);
            //LogUtils.i(TAG, "全部文本内容："+i+Result.toString());
            //分解字符串，并分解结果存储入数据库中
            decomposeResult(Result.toString(),i);
           // LogUtils.i(TAG, "插入了一条数据进数据库，第"+i+"条");
			//关闭输入输出文件流
			inputReader.close();
			bufReader.close();
		}

	}

	/**
	 * 将result字符串分解成3部分
	 * 1.package
	 * 2.注释的提示内容
	 * 3.代码本身
	 * 最后存储到数据库当中。	
	 * @param result
	 */
	private void decomposeResult(String result,int i) {
		// 按照java文件格式，分解，返回3个字符串
		LogUtils.i(TAG, "分解之前的文本内容："+result);
		String[] seperateStrings = result.split("/\\*|\\*/");
		String[] nameString = filesName[i].split("\\.");
		//将seperateStrings字符串数组 导入生成的数据库中。
		if (seperateStrings.length <2)
		{
			//有的算题题没有把tip信息复制进来
			javaLeetcodeDao.insert(i+"",nameString[0],"(*^__^*) ", seperateStrings[0], "no tip");
		}else{
			javaLeetcodeDao.insert(i+"",nameString[0], seperateStrings[0], seperateStrings[1], seperateStrings[2]);
			LogUtils.i(TAG, "文本内容："+i+nameString[0]+seperateStrings[0]+seperateStrings[1]+seperateStrings[2]);
		}


	}

	/**
	 * go in to main activity
	 */
	private void enterHome() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		//记录已经加载数据乐，下次不需要在创建数据库
		UtilsSp.setBoolean(this, ConstantValues.SPLASH_DATA, true);
		//销毁splash界面
		finish();
	}

	/**
	 * 添加淡入动画效果
	 */
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		ll_plash.startAnimation(alphaAnimation);
	}
}
