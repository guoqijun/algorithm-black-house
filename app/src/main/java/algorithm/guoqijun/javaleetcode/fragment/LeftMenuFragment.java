package algorithm.guoqijun.javaleetcode.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import algorithm.guoqijun.javaleetcode.R;
import algorithm.guoqijun.javaleetcode.activity.MainActivity;
import algorithm.guoqijun.javaleetcode.been.ConstantValues;
import algorithm.guoqijun.javaleetcode.database.JavaLeetcodeDao;
import algorithm.guoqijun.javaleetcode.utils.LogUtils;

/**
 * Created by GUOQIJUN on 2016/8/1.
 */
public class LeftMenuFragment extends BaseFragment{

    private ListView lvList;
    private EditText et_search;
    private ImageButton Iv_goSearch;
    private int files_count;	//java算法题的总数
    private JavaLeetcodeDao mDao;
    private ArrayList<String> fileNamesAll;
    private Filename_Adapter filename_Adapter;
    private  boolean allowLoad = true;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left, null);
        lvList = (ListView) view.findViewById(R.id.lv_list);
        et_search = (EditText) view.findViewById(R.id.et_search);
        Iv_goSearch = (ImageButton) view.findViewById(R.id.Iv_goSearch);
        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    //处理事件
                    searchProcess();

                }
                return false;
            }
        });
        Iv_goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProcess();
            }
        });
        return view;
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        mDao = JavaLeetcodeDao.getInstance(mActivity);
        files_count = mDao.getCount();
        fileNamesAll = new ArrayList<String>();
        loadName(0);
        filename_Adapter = new Filename_Adapter();
        lvList.setAdapter(filename_Adapter);

    }
    private void searchProcess(){
        //模糊搜索数据库
        allowLoad =false;
        String keyWord = et_search.getText().toString();
        fileNamesAll.clear();
        filename_Adapter.notifyDataSetChanged();
        if (keyWord.isEmpty()) {
            lvList.setAdapter(null);
            filename_Adapter = new Filename_Adapter();
            allowLoad=true;
            lvList.setAdapter(filename_Adapter);
            loadName(0);
            lvList.setSelection(0);
            filename_Adapter.notifyDataSetChanged();
        }else{
            fileNamesAll= mDao.searchBlurName(keyWord);
            filename_Adapter.notifyDataSetChanged();
            LogUtils.i(ConstantValues.TAG_DEBUG,"ri si ni"+keyWord);
            lvList.setSelection(0);
        }
    }
    /**
     * @param start
     * 加载数据从start开始加载20条数据
     * 将数据add到集合fileNamesAll中
     */
    private void loadName(int start) {
        // TODO Auto-generated method stub
        int less = files_count-fileNamesAll.size();
        if(less>=20){
            //还有至少20条或以上的数据,加载20条数据
            for (int j = start; j <start+20; j++) {
                //LogUtils.i("guoqiun", "全部文本内容：第"+j+"个："+mDao.getName(j));
                fileNamesAll.add(mDao.getName(j)) ;
            }
            LogUtils.i(ConstantValues.TAG_DEBUG, "加载了20条");
        }else if (less==0) {
            //加载完毕了，不加载了
            LogUtils.i(ConstantValues.TAG_DEBUG, "加载完了");
            return ;
        }else {
            //加载剩余的不足20条的数据
            for (int j = start; j <less; j++) {
                fileNamesAll.add(mDao.getName(j)) ;
            }
            LogUtils.i(ConstantValues.TAG_DEBUG, "加载剩余");
        }
    }

    class Filename_Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fileNamesAll.size();
        }

        @Override
        public String getItem(int position) {
            // TODO Auto-generated method stub
            return fileNamesAll.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView ==null  || !allowLoad ) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.list_item_left_menu,null);
                holder.file_name =(TextView) convertView.findViewById(R.id.tv_menu);
                holder.file_name.setText(getItem(position));
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLeetcodeMessage(getItem(position));
                    toggle();
                }
            });
            if (position == (fileNamesAll.size()-1) && allowLoad) {
                loadName(position);
                filename_Adapter.notifyDataSetChanged();
            }
            LogUtils.i(ConstantValues.TAG_DEBUG, "listview 的位置：第"+position+"个，文件总数："+fileNamesAll.size());

            return convertView;
        }
    }

    /**
     * @author GUOQIJUN
     *Viewholder的 优化方式
     */
    static class ViewHolder{
        TextView file_name;
    }


    /**
     * 打开或者关闭侧边栏
     */
    protected void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开, 调用后就关; 反之亦然

    }

    /**
     * 显示contentfragment的算法文本
     *
     * @param filename
     */
    protected void showLeetcodeMessage(String filename) {
        // 获取新闻中心的对象
        MainActivity mainUI = (MainActivity) mActivity;
        // 获取ContentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        // 获取NewsCenterPager
        // 修改新闻中心的FrameLayout的布局
        fragment.setLeetcodeText(filename);
    }
}