package com.gianlu.fidal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

@UiThread
public class HorizontalBadges extends LinearLayout {
    public HorizontalBadges(Context context) {
        this(context, null, 0);
    }

    public HorizontalBadges(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalBadges(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
    }

    public void add(@NonNull BadgeView view) {
        addView(view);
    }

    public void add(@DrawableRes int icon, @StringRes int text) {
        add(icon, getResources().getString(text));
    }

    public void add(@DrawableRes int icon, String text) {
        BadgeView view = new BadgeView(getContext());
        view.set(icon, text);
        add(view);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (getChildCount() > 5) return;

        super.addView(child, index, params);
    }

    public void clear() {
        removeAllViews();
    }
}
