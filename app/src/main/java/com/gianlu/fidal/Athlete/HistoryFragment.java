package com.gianlu.fidal.Athlete;

import android.content.Context;
import android.os.Bundle;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public class HistoryFragment extends FragmentWithDialog { // TODO

    @NonNull
    public static HistoryFragment getInstance(@NonNull Context context) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.history));
        fragment.setArguments(args);
        return fragment;
    }
}
