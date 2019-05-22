package cn.edu.swufe.first;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    String data[]={"wait..."};
    Handler handler;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_rate_list);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);

       // ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
       // setListAdapter(adapter);

        Thread t=new Thread(this);
        t.start();

        handler=new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==7){
                    List<String> list2=(List<String>)msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };


    }

    @Override
    //获取网络数据，带回主线程
    public void run() {
        List<String> retList=new ArrayList<String>();
        //获取当前日期
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run","curDateStr:" + curDateStr + " logDate:" + logDate);


        //获得数据
        if(curDateStr.equals(logDate)) {
            //如果相等，则不从网络中获取数据
            Log.i("run", "日期相等，从数据库中获取数据");
            RateManager manager=new RateManager(this);
            for(RateItem item:manager.ListAll()){
                retList.add(item.getCurName()+"-->"+item.getCurRate());
            }

        }else {
            //日期不等，从网络中获取数据
            Log.i("run", "日期不等，从网络中获取数据");
            // Bundle bundle=new Bundle();


            Document doc = null;
            try {
                Thread.sleep(3000);
                doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i("TAG","run:"+doc.title());
                Elements tables = doc.getElementsByTag("table");
                Element table6=tables.get(5);
                Elements tds=table6.getElementsByTag("td");
                List<RateItem> rateList = new ArrayList<RateItem>();

                for(int i=0;i<tds.size();i+=8){
                    Element td1=tds.get(i);
                    Element td2=tds.get(i+5);
                    Log.i("TAG","run:"+td1.text()+"==>"+td2.text());
                    String str1= td1.text();
                    String val= td2.text();

                    retList.add(str1+"==>"+val);
                    rateList.add(new RateItem(str1,val));
                }

                //把数据写入数据库中
                RateManager manager=new RateManager(this);
                manager.deleteAll();
                manager.addAll(rateList);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //更新记录日期
            SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY, curDateStr);
            edit.commit();
            Log.i("run","更新日期结束：" + curDateStr);
        }


        Message msg = handler.obtainMessage(7);
        msg.what = 5;
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}
