package cn.edu.swufe.first;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static cn.edu.swufe.first.R.*;
import static cn.edu.swufe.first.R.id.btn;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        EditText edit = (EditText) findViewById(R.id.ss);
        String str = edit.getText().toString();
        float c = Float.parseFloat(str);
        float f = c * 9 / 5 + 32;
        String str2 = Float.toString(f);

        TextView out = findViewById(R.id.txtout);
        out.setText("结果为：" + str2);


    }
}