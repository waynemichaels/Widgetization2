package com.example.user1.widgetization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Scroller;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Scroller scroll = new Scroller(this);
        CustomWidget customCard = (CustomWidget)findViewById(R.id.customCard);
        //customCard.setSmallText(0,0, "MIKE");
    }
}
