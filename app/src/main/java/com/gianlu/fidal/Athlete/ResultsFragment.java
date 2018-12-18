package com.gianlu.fidal.Athlete;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.fidal.Adapters.ResultsAdapter;
import com.gianlu.fidal.NetIO.Models.CompetitionResults;
import com.gianlu.fidal.NetIO.Models.Competitions.AbsCompetition;
import com.gianlu.fidal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsFragment extends FragmentWithDialog { // TODO

    @NonNull
    public static ResultsFragment getInstance(@NonNull Context context, @NonNull HashMap<AbsCompetition, CompetitionResults> results) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.results));
        args.putSerializable("results", new ArrayList<>(results.values()));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerViewLayout layout = (RecyclerViewLayout) inflater.inflate(R.layout.fragment_records, container, false);
        layout.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        layout.getList().addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        List<CompetitionResults> results;
        Bundle args;
        if ((args = getArguments()) == null || (results = (List<CompetitionResults>) args.getSerializable("results")) == null) {
            layout.showError(R.string.failedLoading);
            return layout;
        }

        ResultsAdapter adapter = new ResultsAdapter(requireContext(), results);
        layout.loadListData(adapter);

        return layout;
    }
}
