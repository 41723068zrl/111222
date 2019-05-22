package cn.edu.swufe.first;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayAdapter adapter;
    List<String> data=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        ListView listView = (ListView) findViewById(R.id.mylist);
        for (int i = 0; i < 10; i++) {
            data.add("item" + i);
        }

        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setEmptyView(findViewById(R.id.nodata));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {
        adapter.remove(listv.getItemAtPosition(position));
        adapter.notifyDataSetChanged();

    }
}

