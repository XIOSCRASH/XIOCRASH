package com.xioscrash.lumalauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class SettingsActivity extends Activity {
    private android.content.SharedPreferences prefs;
    private int dp(float v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    @Override public void onCreate(Bundle b){super.onCreate(b);prefs=getSharedPreferences("launcher",MODE_PRIVATE);show();}
    private void show(){
        LinearLayout page=new LinearLayout(this);page.setOrientation(LinearLayout.VERTICAL);page.setPadding(dp(22),dp(30),dp(22),dp(20));page.setBackgroundColor(Color.rgb(246,246,250));
        TextView title=new TextView(this);title.setText("Home settings");title.setTextSize(28);title.setTypeface(null,Typeface.BOLD);title.setTextColor(Color.rgb(35,38,48));page.addView(title);
        TextView intro=new TextView(this);intro.setText("Make your home feel like yours.");intro.setTextSize(14);intro.setTextColor(Color.GRAY);LinearLayout.LayoutParams ilp=new LinearLayout.LayoutParams(-1,dp(42));ilp.topMargin=dp(4);page.addView(intro,ilp);
        addAction(page,"Wallpaper tint","Choose a soft home-screen color",this::colors);
        addAction(page,"Home screen grid","Columns: "+prefs.getInt("columns",4),this::grid);
        addAction(page,"Icon size","Adjust app icon scale",this::size);
        Switch label=new Switch(this);label.setText("Show app names");label.setTextSize(16);label.setChecked(prefs.getBoolean("labels",true));page.addView(label,space(58));label.setOnCheckedChangeListener((v,on)->prefs.edit().putBoolean("labels",on).apply());
        Switch ambient=new Switch(this);ambient.setText("Ambient clock");ambient.setTextSize(16);ambient.setChecked(prefs.getBoolean("ambient",true));page.addView(ambient,space(58));ambient.setOnCheckedChangeListener((v,on)->prefs.edit().putBoolean("ambient",on).apply());
        TextView note=new TextView(this);note.setText("Ambient clock is an optional, dim clock view while the launcher is open. Android controls the actual lock-screen Always-on Display and screen-off behavior.");note.setTextSize(13);note.setTextColor(Color.GRAY);LinearLayout.LayoutParams nlp=new LinearLayout.LayoutParams(-1,-2);nlp.topMargin=dp(12);page.addView(note,nlp);
        TextView done=new TextView(this);done.setText("Done");done.setTextSize(16);done.setGravity(Gravity.CENTER);done.setTextColor(Color.WHITE);done.setBackground(bg(0xff6878e8,dp(24)));LinearLayout.LayoutParams dlp=new LinearLayout.LayoutParams(-1,dp(50));dlp.topMargin=dp(24);page.addView(done,dlp);done.setOnClickListener(v->finish());setContentView(page);
    }
    private LinearLayout.LayoutParams space(int height){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(height));p.topMargin=dp(6);return p;}
    private void addAction(LinearLayout page,String title,String subtitle,Runnable action){LinearLayout item=new LinearLayout(this);item.setOrientation(LinearLayout.VERTICAL);item.setGravity(Gravity.CENTER_VERTICAL);item.setPadding(dp(15),dp(7),dp(15),dp(7));item.setBackground(bg(Color.WHITE,dp(16)));TextView a=new TextView(this);a.setText(title);a.setTextSize(16);a.setTextColor(0xff272936);item.addView(a);TextView b=new TextView(this);b.setText(subtitle);b.setTextSize(12);b.setTextColor(0xff858691);item.addView(b);LinearLayout.LayoutParams p=space(68);p.bottomMargin=dp(5);page.addView(item,p);item.setOnClickListener(v->action.run());}
    private GradientDrawable bg(int color,int rad){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(rad);return d;}
    private void colors(){String[] names={"Cloud","Warm","Rose","Sage","Night"};int[] colors={0xfff2f2f7,0xfff7f1e8,0xfff8edf1,0xffedf4ef,0xff20222b};new AlertDialog.Builder(this).setTitle("Wallpaper tint").setItems(names,(d,w)->{prefs.edit().putInt("wallpaper",colors[w]).apply();}).show();}
    private void grid(){String[] options={"3 columns","4 columns","5 columns"};new AlertDialog.Builder(this).setTitle("Home screen grid").setItems(options,(d,w)->prefs.edit().putInt("columns",w+3).apply()).show();}
    private void size(){LinearLayout box=new LinearLayout(this);box.setPadding(dp(20),dp(12),dp(20),dp(4));SeekBar seek=new SeekBar(this);seek.setMax(36);seek.setProgress(prefs.getInt("iconSize",54)-40);box.addView(seek,new LinearLayout.LayoutParams(-1,dp(48)));new AlertDialog.Builder(this).setTitle("Icon size").setView(box).setNegativeButton("Cancel",null).setPositiveButton("Save",(d,w)->prefs.edit().putInt("iconSize",40+seek.getProgress()).apply()).show();}
}
