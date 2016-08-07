package algorithm.guoqijun.javaleetcode.fragment;

import android.graphics.Color;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import algorithm.guoqijun.javaleetcode.R;
import algorithm.guoqijun.javaleetcode.database.JavaLeetcodeDao;

/**
 * Created by GUOQIJUN on 2016/8/1.
 */
public class ContentFragment extends BaseFragment{
    private TextView tv_answer;
    private TextView tv_title;
    JavaLeetcodeDao dao;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_main, null);
        tv_answer = (TextView) view.findViewById(R.id.tv_answer);
        tv_title= (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void initData() {
        // TODO Auto-generated method stub
        dao = JavaLeetcodeDao.getInstance(mActivity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * @param filename
     * 在leftfragment中调用，传递算法题的名字，在contentfragment中显示
     */
    public void setLeetcodeText(String filename) {
        // TODO Auto-generated method stub
        //Message msg = new Message();
        //msg.obj=filename;
        //tv_title.setText(filename);
        //tv_tip.setText(dao.getTipMessage(filename));
        //tv_package.setText(dao.getPackageMessage(filename).replace(";",";\n"));
        tv_title.setText(filename);
        tv_title.setTextColor(Color.WHITE);
        tv_answer.setText("");
        InputStreamReader inputReader =null;
        BufferedReader bufReader = null;
        String line = null;
        String color = "#FFFFFF";
        try {
            inputReader = new InputStreamReader( getResources().getAssets().open(filename+".java"));
            bufReader = new BufferedReader(inputReader);
            while((line = bufReader.readLine()) != null) {
                line.replace("  ","    ");   //looks better
                line.replace(" ","  ");   //looks better

                if (line.contains("//")&&!color.equals("red")) color = "green";
                if (line.contains("/*")) color = "red";
                if (line.contains("import")) color = "yellow";

                String html="<font color=\""+color+"\">"+line+"</font> <br>";
                CharSequence charSequence= Html.fromHtml(html);
                tv_answer.append(charSequence);

                if (line.contains("*/")) color = "#FFFFFF";
                if (color.equals("green")||line.contains("import")) color = "#FFFFFF";
            }
            inputReader.close();
            bufReader.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public  void SetTextSize(float size){
//        float textSize = tv_answer.getTextSize();
//        LogUtils.i("guoqijun",textSize+"");
        tv_answer.setTextSize(size);
    }
    public  String getTv_title(){
        return  tv_title.getText().toString();
    }
}
