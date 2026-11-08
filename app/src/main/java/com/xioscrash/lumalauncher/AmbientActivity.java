package com.xioscrash.lumalauncher;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** A low-motion ambient clock shown on demand; it does not override Android's lock-screen AOD. */
public class AmbientActivity extends Activity {
    private TextView clock,date;
    private final Runnable tick=new Runnable(){public void run(){if(clock!=null){clock.setText(new SimpleDateFormat("h:mm",Locale.getDefault()).format(new Date()));date.setText(new SimpleDateFormat("EEEE, MMM d",Locale.getDefault()).format(new Date()));clock.postDelayed(this,30_000);}}};
    @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.BLACK);getWindow().setNavigationBarColor(Color.BLACK);getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_LOW_PROFILE);getWindow().setBackgroundDrawableResource(android.R.color.black);
      android.widget.LinearLayout content=new android.widget.LinearLayout(this);content.setOrientation(android.widget.LinearLayout.VERTICAL);content.setGravity(Gravity.CENTER);clock=new TextView(this);clock.setTextColor(0xff777982);clock.setTextSize(64);clock.setGravity(Gravity.CENTER);date=new TextView(this);date.setTextColor(0xff555760);date.setTextSize(15);date.setGravity(Gravity.CENTER);content.addView(clock);content.addView(date);setContentView(content);content.setOnTouchListener((v,e)->{if(e.getAction()==MotionEvent.ACTION_DOWN)finish();return true;});tick.run();}
    @Override protected void onDestroy(){if(clock!=null)clock.removeCallbacks(tick);super.onDestroy();}
}
