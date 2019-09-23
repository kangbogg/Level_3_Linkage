package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> one = new ArrayList<>();
    private ArrayList<ArrayList<String>> two = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> three = new ArrayList<>();
    private String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parseJson(getStringForFile());
    }

    private void parseJson(String json) {

        List<Bean> beans = JSON.parseArray(json, Bean.class);
        for (Bean bean : beans) {
            //省
            one.add(bean.getName());
            //市
            List<Bean.CityBean> city = bean.getCity();
            ArrayList<String> strings = new ArrayList<>();
            ArrayList<ArrayList<String>> stringss = new ArrayList<>();
            for (Bean.CityBean cityBean : city) {
                strings.add(cityBean.getName());
                stringss.add((ArrayList<String>) cityBean.getArea());
            }
            two.add(strings);
            three.add(stringss);

        }
        Toast.makeText(this, ""+json, Toast.LENGTH_SHORT).show();
    }

    private String getStringForFile() {
        InputStream is = null;
        try {
            is = getAssets().open("city.json");
            int length = 0;
            byte[] bytes = new byte[1024 * 8];
            StringBuffer stringBuffer = new StringBuffer();
            while ((length = is.read(bytes)) != -1) {
                stringBuffer.append(new String(bytes, 0, length));
            }
            Log.d("##", "getStringForFile: "+stringBuffer.toString());
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public void click(View view) {
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                s = one.get(options1) +
                        two.get(options1).get(options2) +
                        three.get(options1).get(options2).get(options3);
            }
        }).setTitleText("城市选择")
                .build();

        pickerView.setPicker(one, two, three);
        pickerView.show();
    }
}
