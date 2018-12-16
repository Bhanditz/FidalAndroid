package com.gianlu.fidal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

@UiThread
public class BadgeView extends LinearLayout {
    private final ImageView icon;
    private final TextView text;

    public BadgeView(Context context) {
        this(context, null, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);

        View.inflate(context, R.layout.view_badge, this);

        icon = findViewById(R.id.badgeView_icon);
        text = findViewById(R.id.badgeView_text);
    }

    public void set(@DrawableRes int icon, @StringRes int text) {
        set(icon, getResources().getString(text));
    }

    public void set(@DrawableRes int icon, String text) {
        this.icon.setImageResource(icon);
        this.text.setText(text);
    }
}
