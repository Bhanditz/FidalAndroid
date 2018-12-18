package com.gianlu.fidal.Athlete;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.fidal.Adapters.HistoryAdapter;
import com.gianlu.fidal.NetIO.Models.AthleteDetails;
import com.gianlu.fidal.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFragment extends FragmentWithDialog {

    @NonNull
    public static HistoryFragment getInstance(@NonNull Context context, @NonNull ArrayList<AthleteDetails.HistoryItem> history) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.history));
        args.putSerializable("history", history);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerViewLayout layout = (RecyclerViewLayout) inflater.inflate(R.layout.fragment_history, container, false);
        layout.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        layout.getList().addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        List<AthleteDetails.HistoryItem> history;
        Bundle args;
        if ((args = getArguments()) == null || (history = (List<AthleteDetails.HistoryItem>) args.getSerializable("history")) == null) {
            layout.showError(R.string.failedLoading);
            return layout;
        }

        HistoryAdapter adapter = new HistoryAdapter(requireContext(), history);
        layout.loadListData(adapter);

        return layout;
    }
}
