package com.gianlu.fidal.Athlete;

import android.content.Context;
import android.os.Bundle;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public class RecordsFragment extends FragmentWithDialog { // TODO

    @NonNull
    public static RecordsFragment getInstance(@NonNull Context context) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.records));
        fragment.setArguments(args);
        return fragment;
    }
}
