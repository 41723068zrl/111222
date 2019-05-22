package cn.edu.swufe.first;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {

    EditText dollarText;
    EditText euroText;
    EditText wonText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent=getIntent();
        float dollar2=intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2=intent.getFloatExtra("euro_rate_key",0.0f);
        float won2=intent.getFloatExtra("won_rate_key",0.0f);

        dollarText=(EditText)findViewById(R.id.rate_dollar);
        euroText=(EditText)findViewById(R.id.rate_euro);
        wonText=(EditText)findViewById(R.id.rate_won);

        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));

    }
    public void save(View btn){
        Log.i("cfg", "save: ");
        float newDollar =Float.parseFloat(dollarText.getText().toString());
        float newEuro =Float.parseFloat(euroText.getText().toString());
        float newWon =Float.parseFloat(wonText.getText().toString());

        Intent intent =getIntent();
        Bundle bdl =new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);

        setResult(2,intent);

        finish();
    }

}
