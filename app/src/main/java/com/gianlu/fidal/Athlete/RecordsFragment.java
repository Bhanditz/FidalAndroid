package com.gianlu.fidal.Athlete;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gianlu.commonutils.Dialogs.FragmentWithDialog;
import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.fidal.Adapters.RecordsAdapter;
import com.gianlu.fidal.NetIO.Models.CompetitionRecord;
import com.gianlu.fidal.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecordsFragment extends FragmentWithDialog implements RecordsAdapter.Listener {

    @NonNull
    public static RecordsFragment getInstance(@NonNull Context context, @NonNull ArrayList<CompetitionRecord> records) {
        RecordsFragment fragment = new RecordsFragment();
        Bundle args = new Bundle();
        args.putString("title", context.getString(R.string.records));
        args.putSerializable("records", records);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerViewLayout layout = (RecyclerViewLayout) inflater.inflate(R.layout.fragment_records, container, false);
        layout.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        layout.getList().addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        List<CompetitionRecord> records;
        Bundle args;
        if ((args = getArguments()) == null || (records = (List<CompetitionRecord>) args.getSerializable("records")) == null) {
            layout.showError(R.string.failedLoading);
            return layout;
        }

        RecordsAdapter adapter = new RecordsAdapter(requireContext(), records, this);
        layout.loadListData(adapter);

        return layout;
    }

    @Override
    public void onRecordSelected(@NonNull CompetitionRecord record) {
        RecordBottomSheet.showSheet(getActivity(), record);
    }
}
