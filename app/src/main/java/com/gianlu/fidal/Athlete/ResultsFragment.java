package com.gianlu.fidal.Athlete;

import android.content.Context;
import android.os.Bundle;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.fidal.R;

import androidx.annotation.NonNull;

public class ResultsFragment extends FragmentWithDialog { // TODO

    @NonNull
    public static ResultsFragment getInstance(@NonNull Context context) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.results));
        fragment.setArguments(args);
        return fragment;
    }
}
