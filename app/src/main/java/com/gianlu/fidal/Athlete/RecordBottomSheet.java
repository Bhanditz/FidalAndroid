package com.gianlu.fidal.Athlete;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gianlu.commonutils.BottomSheet.BaseModalBottomSheet;
import com.gianlu.fidal.NetIO.Models.CompetitionRecord;
import com.gianlu.fidal.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public class RecordBottomSheet extends BaseModalBottomSheet<CompetitionRecord, Void> { // TODO
    @Override
    protected boolean onCreateHeader(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @NonNull CompetitionRecord payload) {
        return false;
    }

    @Override
    protected void onCreateBody(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @NonNull CompetitionRecord payload) {

    }

    @Override
    protected void onCustomizeToolbar(@NonNull Toolbar toolbar, @NonNull CompetitionRecord payload) {
        toolbar.setTitle(payload.competition.getText(requireContext()));
        toolbar.setBackgroundResource(R.color.colorPrimary);
    }

    @Override
    protected boolean onCustomizeAction(@NonNull FloatingActionButton action, @NonNull CompetitionRecord payload) {
        return false;
    }
}
