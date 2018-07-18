package com.vinay.ticker;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vinay.ticker.lib.TickerView;

public class TickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);
        initContent();
    }

    private void initContent() {
        final SeekBar speedSeekView = findViewById(R.id.speedSeekView);
        final TickerView tickerView = findViewById(R.id.tickerView);
        for (int index = 0; index < 50; index++) {
            // childViews
            TextView tv = new TextView(this);
            tv.setLayoutParams(new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            tv.setText(String.valueOf(index + 1));
            tv.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            tv.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            tv.setPadding(10, 5, 10, 5);
            tickerView.addChildView(tv);
        }
        tickerView.showTickers();

        speedSeekView.setProgress(tickerView.getDisplacement());

        speedSeekView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tickerView.setDisplacement(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
