package algorithm.guoqijun.javaleetcode.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import algorithm.guoqijun.javaleetcode.R;
import algorithm.guoqijun.javaleetcode.database.JavaLeetcodeDao;
import algorithm.guoqijun.javaleetcode.fragment.ContentFragment;
import algorithm.guoqijun.javaleetcode.fragment.LeftMenuFragment;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends SlidingFragmentActivity  {
	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";
	JavaLeetcodeDao dao;
	ImageButton bt_size ;
	ImageButton bt_menu ;
	ImageButton bt_back ;
	ImageButton bt_search ;
	ImageButton bt_share ;
	String shareText="";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ShareSDK.initSDK(this,"15ce986cdca1c");
		bt_size = (ImageButton) findViewById(R.id.bt_size);
		bt_menu = (ImageButton) findViewById(R.id.bt_menu);
		bt_back = (ImageButton) findViewById(R.id.bt_back);
		bt_search = (ImageButton) findViewById(R.id.bt_search);
		bt_share = (ImageButton) findViewById(R.id.bt_share);
		dao =JavaLeetcodeDao.getInstance(getApplicationContext());

		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
		// slidingMenu.setBehindOffset(200);// 屏幕预留200像素宽度

		// 200/320 * 屏幕宽度
		WindowManager wm = getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		//侧边栏占据1/2的屏幕
		slidingMenu.setBehindOffset(width * 120 / 320);
		slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen() {
//				LogUtils.i(ConstantValues.TAG_DEBUG, "open");
				bt_back.setVisibility(View.VISIBLE);
				bt_menu.setVisibility(View.INVISIBLE);
			}
		});
		slidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
			@Override
			public void onClose() {
//				LogUtils.i(ConstantValues.TAG_DEBUG, "close");
				bt_back.setVisibility(View.INVISIBLE);
				bt_menu.setVisibility(View.VISIBLE);
			}
		});
		//加载碎片
		initFragment();
		//按钮们的点击事件注册
		initProcess();

	}
	/**
	 *register button click
	 */
	private void initProcess() {
		//设置字体大小
		bt_size.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChooseDialog();
			}
		});
		//选择leftfragment切换成别的功能
		bt_menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		//收起slidingmenu
		bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getSlidingMenu().toggle();
			}
		});
		//进入同城java，寻求帮助
		bt_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),CityFriend.class);
				startActivity(intent);
			}
		});
		//分享功能
		bt_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				shareText =dao.getTipMessage(getContentFragment().getTv_title());
				showShare();
			}
		});
	}


	private int mTempWhich;// 记录临时选择的字体大小(点击确定之前)
	private int mCurrenWhich = 40;// 记录当前选中的字体大小(点击确定之后), 默认正常字体
	/**
	 *
	 */
	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体设置");
		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };
		builder.setSingleChoiceItems(items, mCurrenWhich, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
						mTempWhich = which;
					}
		});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 根据选择的字体来修改网页字体大小
				ContentFragment contentFragment = getContentFragment();
				switch (mTempWhich) {
					case 0:
						// 超大字体
						contentFragment.SetTextSize(25);
						// settings.setTextZoom(22);
						break;
					case 1:
						// 大字体
						contentFragment.SetTextSize(20);
						break;
					case 2:
						// 正常字体
						contentFragment.SetTextSize(15);
						break;
					case 3:
						// 小字体
						contentFragment.SetTextSize(12);
						break;
					case 4:
						// 超小字体
						contentFragment.SetTextSize(10);
						break;
					default:
						break;
				}
				mCurrenWhich=mTempWhich;
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 
	 */
	private void initFragment() {
		// TODO Auto-generated method stub
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();// 开始事务
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),TAG_LEFT_MENU);// 用fragment替换帧布局;参1:帧布局容器的id;参2:是要替换的fragment;参3:标记
		transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		transaction.commit();// 提交事务
		// Fragment fragment =
		// fm.findFragmentByTag(TAG_LEFT_MENU);//根据标记找到对应的fragment
	}
	// 获取侧边栏fragment对象
	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fm
				.findFragmentByTag(TAG_LEFT_MENU);// 根据标记找到对应的fragment
		return fragment;
	}

	// 获取主页fragment对象
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(TAG_CONTENT);// 根据标记找到对应的fragment
		return fragment;
	}
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
		oks.setTitle("来自Java小黑屋的分享");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(shareText);
//		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl("http://sharesdk.cn");
//		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		oks.setSiteUrl("http://sharesdk.cn");
//		// titleUrl是标题的网络链接，QQ和QQ空间等使用
//		oks.setTitleUrl("http://sharesdk.cn");

// 启动分享GUI
		oks.show(this);
	}
}
