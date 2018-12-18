package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.commonutils.FontsManager;
import com.gianlu.fidal.NetIO.Models.AthleteDetails;
import com.gianlu.fidal.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private final List<AthleteDetails.HistoryItem> history;

    public HistoryAdapter(@NonNull Context context, List<AthleteDetails.HistoryItem> history) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.history = history;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AthleteDetails.HistoryItem item = history.get(position);

        holder.club.setText(item.club.text);
        holder.year.setText(String.valueOf(item.year));
        holder.category.setText(item.category.getText(context));
        holder.reason.setText(item.reason.getText(context));
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView club;
        final TextView reason;
        final TextView year;
        final TextView category;

        ViewHolder(@NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_history, parent, false));

            club = itemView.findViewById(R.id.historyItem_club);
            reason = itemView.findViewById(R.id.historyItem_reason);
            year = itemView.findViewById(R.id.historyItem_year);
            category = itemView.findViewById(R.id.historyItem_category);

            FontsManager.set(FontsManager.ROBOTO_REGULAR, year, club, reason, category);
        }
    }
}
