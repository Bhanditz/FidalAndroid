package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gianlu.fidal.NetIO.Models.CompetitionResults;
import com.gianlu.fidal.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<CompetitionResults> results;

    public ResultsAdapter(@NonNull Context context, List<CompetitionResults> results) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_result, parent, false));
        }
    }
}
