package com.example.user1.widgetization;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private CustomWidget customCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Scroller scroll = new Scroller(this);
        customCard = (CustomWidget)findViewById(R.id.customCard);
        customCard.removeTitle();
        customCard.removeProfilePic();
        customCard.removeTitleDescription();

        customCard.addColumnSpans(new int[]{0,0,1,3},
                new int[]{3,0,1,3},
                new int[]{5,0,1,3},
                new int[]{5,0,1,3},
                new int[]{6,0,1,3},
                new int[]{7,0,1,3},
                new int[]{13,0,1,2});

        customCard.addViews(1,
                0,0,0,
                11,11,11,
                1,
                8,8,8,
                1,
                0,
                13,
                2,2,2,
                3,3,3,
                4,4,4,
                5,5,5,
                6,6,6,
                7,7,
                8,8,8,
                9,9,9);

        customCard.setSmallText(1,0, "Service");
        customCard.setSmallText(1,1, "Product");
        customCard.setSmallText(1,2, "Stylist");
        customCard.setSmallText(6,0, "Only 13 Abuja braids left");

        customCard.setBigText(0,0,"Leaderboard");
        customCard.setBigText(3,0,"Appointments");
        customCard.setBigText(5,0,"Inventory");

        customCard.setSmallImage(8,0, R.drawable.drake);
        customCard.setSmallImage(8,1, R.drawable.zendaya);
        customCard.setSmallImage(8,2, R.drawable.justin_bieber);

        customCard.setBigImage(9,0, R.drawable.drake);
        customCard.setBigImage(9,1, R.drawable.zendaya);
        customCard.setBigImage(9,2, R.drawable.justin_bieber);

        customCard.setPercentage(10, 0, 60, "nails");
        customCard.setPercentage(10, 1, 25, "braids");
        customCard.setPercentage(10, 2, 40, "skin");

        customCard.setCurrency(11, 0, 600, "nails");
        customCard.setCurrency(11, 1, 2500, "braids");
        customCard.setCurrency(11, 2, 400, "skin");

        customCard.setDate(12,0, null);
        customCard.setDate(12,1, null);
        customCard.setDate(12,2, null);

        customCard.setVLValue(4, 0, "10", "Today");
        customCard.setVLValue(4, 1, "25", "This week");
        customCard.setVLValue(4, 2, "60", "This month");

        customCard.setProgress(2, 0, 75, "Nails");
        customCard.setProgress(2, 1, 78, "Braids");
        customCard.setProgress(2, 2, 85, "Skin");

        customCard.setHLValue(13, 0, "STYLE", "nails");
        customCard.setHLValue(13, 2, "STYLE", "braids");

        customCard.setVLValue(14, 0, "ESTHER", "nails");
        customCard.setVLValue(14, 1, "MICHAEL", "braids");
        customCard.setVLValue(14, 2, "JOY", "skin");

        customCard.setRating(15, 0, 3);
        customCard.setRating(15, 1, 4);
        customCard.setRating(15, 2, 1);

        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast();
            }
        };

        customCard.setButtonText(7, 0, "SHOW YES");
        customCard.setButtonListener(7, 0, btnListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void showToast(){
        Toast.makeText(this,"YAAAAAAAAAAS", Toast.LENGTH_SHORT).show();
    }
}
