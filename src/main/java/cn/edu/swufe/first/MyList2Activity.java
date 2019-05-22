package cn.edu.swufe.first;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    Handler handler;
    private ArrayList<HashMap<String,String>> listItems;
    private SimpleAdapter listItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_list2);
        initListView();
        this.setListAdapter(listItemAdapter);
        Thread t=new Thread(this);
        t.start();

        handler=new Handler(){
            public void handleMessage(Message msg){
                if(msg.what==7){
                    List<HashMap<String,String>> listItems=( List<HashMap<String,String>>)msg.obj;

                    listItemAdapter = new SimpleAdapter(MyList2Activity.this,listItems,
                            R.layout.activity_my_list2,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );

                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };


        getListView().setOnItemClickListener(this);//使用this做监听
        getListView().setOnItemLongClickListener(this);

    }

    private void initListView(){
        listItems = new ArrayList<HashMap<String,String>>();
        for(int i =0;i<10;i++){
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("ItemTitle","Rate: "+ i);
            map.put("ItemDetail","detail: "+ i);
            listItems.add(map);


            listItemAdapter = new SimpleAdapter(this,listItems,
                    R.layout.activity_my_list2,
                    new String[]{"ItemTitle","ItemDetail"},
                    new int[]{R.id.itemTitle,R.id.itemDetail}
            );
        }
    }


    @Override
    public void run() {
        List<HashMap<String,String>> retList=new ArrayList<HashMap<String,String>>();
        Bundle bundle=new Bundle();
        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i("TAG","run:"+doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table6=tables.get(5);
            Elements tds=table6.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8){
                Element td1=tds.get(i);
                Element td2=tds.get(i+5);
                Log.i("TAG","run:"+td1.text()+"==>"+td2.text());
                String str1= td1.text();
                String val= td2.text();

                HashMap<String,String> map=new HashMap<String,String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                retList.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Message msg = handler.obtainMessage(7);
        msg.what = 5;
        msg.obj = retList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//获取数据
        HashMap<String,String> map=(HashMap<String,String>)getListView().getItemAtPosition(position);
        String titleStr=map.get("ItemTitle");
        String detailStr=map.get("ItemDetail");

        //打开新的页面，传入参数
        Intent rateCalc=new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",detailStr);
        startActivity(rateCalc);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //删除操作
        //构造对话框确认操作
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MyList2","onClick:对话框事件处理");
                listItems.remove(position);
                listItemAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}
