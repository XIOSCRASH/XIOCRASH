package com.xioscrash.lumalauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private SharedPreferences prefs;
    private LinearLayout root, appGrid;
    private int bgColor, columns, iconSize;
    private boolean labels;
    private final int ink = Color.rgb(35, 38, 48);
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("launcher", MODE_PRIVATE);
        render();
    }
    @Override protected void onResume() { super.onResume(); if (root != null) render(); }

    private void render() {
        bgColor = prefs.getInt("wallpaper", Color.rgb(242, 242, 247));
        columns = prefs.getInt("columns", 4);
        iconSize = prefs.getInt("iconSize", 54);
        labels = prefs.getBoolean("labels", true);
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(dp(22), dp(28), dp(22), dp(14)); root.setBackgroundColor(bgColor);
        setContentView(root);
        LinearLayout bar = new LinearLayout(this); bar.setGravity(Gravity.CENTER_VERTICAL); bar.setOrientation(LinearLayout.HORIZONTAL);
        TextView brand = text("Luma", 19, ink, true); bar.addView(brand, new LinearLayout.LayoutParams(0, dp(42), 1));
        TextView settings = text("•••", 23, ink, true); settings.setGravity(Gravity.CENTER); settings.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class))); bar.addView(settings, new LinearLayout.LayoutParams(dp(42), dp(42))); root.addView(bar);

        TextView clock = text(new SimpleDateFormat("h:mm", Locale.getDefault()).format(new Date()), 58, ink, false);
        clock.setLetterSpacing(-.04f); clock.setOnClickListener(v -> { if (prefs.getBoolean("ambient", true)) startActivity(new Intent(this, AmbientActivity.class)); });
        root.addView(clock, new LinearLayout.LayoutParams(-1, dp(76)));
        TextView date = text(new SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(new Date()), 15, Color.rgb(115,117,127), false); root.addView(date);
        View spacer = new View(this); root.addView(spacer, new LinearLayout.LayoutParams(1, 0, 1));

        TextView search = text("⌕     Search apps", 16, Color.rgb(100,102,112), false); search.setPadding(dp(18), 0, dp(18), 0); search.setGravity(Gravity.CENTER_VERTICAL);
        search.setBackground(round(Color.argb(150,255,255,255), dp(24))); LinearLayout.LayoutParams searchLp = new LinearLayout.LayoutParams(-1, dp(50)); searchLp.bottomMargin = dp(18); root.addView(search, searchLp);
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(false); appGrid = new LinearLayout(this); appGrid.setOrientation(LinearLayout.VERTICAL); scroll.addView(appGrid); root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 2));
        loadApps();
        TextView customizeHint = text("Tap the clock for ambient mode  ·  Long-press for settings", 11, Color.rgb(130,131,140), false); customizeHint.setGravity(Gravity.CENTER); LinearLayout.LayoutParams hintLp = new LinearLayout.LayoutParams(-1, dp(32)); hintLp.topMargin = dp(8); root.addView(customizeHint, hintLp);
    }
    private void loadApps() {
        PackageManager pm = getPackageManager(); Intent query = new Intent(Intent.ACTION_MAIN, null); query.addCategory(Intent.CATEGORY_LAUNCHER);
        List<android.content.pm.ResolveInfo> apps = pm.queryIntentActivities(query, 0);
        Collections.sort(apps, (a,b) -> String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString()));
        int rowCount = (apps.size() + columns - 1) / columns;
        for (int r = 0; r < rowCount; r++) {
            LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.TOP); row.setOrientation(LinearLayout.HORIZONTAL); appGrid.addView(row, new LinearLayout.LayoutParams(-1, dp(labels ? 104 : 84)));
            for (int c = 0; c < columns; c++) {
                int i = r * columns + c; if (i >= apps.size()) break;
                android.content.pm.ResolveInfo info = apps.get(i); LinearLayout cell = new LinearLayout(this); cell.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL); cell.setOrientation(LinearLayout.VERTICAL);
                ImageView icon = new ImageView(this); icon.setImageDrawable(info.loadIcon(pm)); icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                LinearLayout.LayoutParams ip = new LinearLayout.LayoutParams(dp(iconSize), dp(iconSize)); cell.addView(icon, ip);
                if (labels) { TextView name = text(info.loadLabel(pm).toString(), 11, ink, false); name.setGravity(Gravity.CENTER); name.setMaxLines(1); name.setEllipsize(android.text.TextUtils.TruncateAt.END); LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-1, dp(27)); np.topMargin = dp(5); cell.addView(name,np); }
                cell.setOnClickListener(v -> { Intent launch = pm.getLaunchIntentForPackage(info.activityInfo.packageName); if (launch != null) startActivity(launch); });
                cell.setOnLongClickListener(v -> { startActivity(new Intent(this, SettingsActivity.class)); return true; });
                row.addView(cell, new LinearLayout.LayoutParams(0, -2, 1));
            }
        }
        for (int i=0;i<appGrid.getChildCount();i++) { View row=appGrid.getChildAt(i); row.setAlpha(0f); row.setTranslationY(dp(8)); row.animate().alpha(1f).translationY(0).setDuration(220).setStartDelay(i*24L).setInterpolator(new DecelerateInterpolator()).start(); }
    }
    private TextView text(String s, int size, int color, boolean bold) { TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); if(bold)t.setTypeface(Typeface.DEFAULT,Typeface.BOLD); return t; }
    private GradientDrawable round(int color, int radius) { GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius); return d; }
}
