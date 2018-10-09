package com.gianlu.fidal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gianlu.commonutils.FontsManager;
import com.gianlu.fidal.NetIO.Models.EventDate;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AdvancedDateDisplayView extends LinearLayout {
    private final LinearLayout start;
    private final TextView startDay;
    private final TextView startDayName;
    private final TextView range;
    private final LinearLayout end;
    private final TextView endDay;
    private final TextView endDayName;

    public AdvancedDateDisplayView(Context context) {
        this(context, null, 0);
    }

    public AdvancedDateDisplayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvancedDateDisplayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.view_advanced_date_display, this);

        start = findViewById(R.id.advancedDateDisplay_start);
        startDay = findViewById(R.id.advancedDateDisplay_startDay);
        startDayName = findViewById(R.id.advancedDateDisplay_startDayName);
        range = findViewById(R.id.advancedDateDisplay_range);
        end = findViewById(R.id.advancedDateDisplay_end);
        endDay = findViewById(R.id.advancedDateDisplay_endDay);
        endDayName = findViewById(R.id.advancedDateDisplay_endDayName);

        FontsManager.set(startDay, FontsManager.ROBOTO_LIGHT);
        FontsManager.set(endDay, FontsManager.ROBOTO_LIGHT);
    }

    private static void set(TextView name, TextView number, long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
        number.setText(sdf.format(millis));
        sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        name.setText(sdf.format(millis));
    }

    private void setStart(long start) {
        set(startDayName, startDay, start);
    }

    private void setEnd(long end) {
        set(endDayName, endDay, end);
    }

    public void setDate(@NonNull EventDate date) {
        if (date.isSingleDay()) {
            end.setVisibility(GONE);
            range.setVisibility(GONE);

            setStart(date.start);
        } else {
            end.setVisibility(VISIBLE);
            range.setVisibility(VISIBLE);

            setStart(date.start);
            setEnd(date.end);
        }
    }
}
