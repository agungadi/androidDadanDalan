package com.agungadi.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.agungadi.myapplication.Helper.PreferenceHelper;

public class MainActivity extends AppCompatActivity {
    private TextView tvname,tvemail;
    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        preferenceHelper = new PreferenceHelper(this);

       // tvemail = (TextView) findViewById(R.id.tvemail);
        //tvname = (TextView) findViewById(R.id.tvname);


        //tvname.setText(preferenceHelper.getName());
        //tvemail.setText(preferenceHelper.getEmail());
    }
}
