package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlu.commonutils.FontsManager;
import com.gianlu.fidal.NetIO.Models.CompetitionRecord;
import com.gianlu.fidal.R;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<CompetitionRecord> records;
    private final Listener listener;

    public RecordsAdapter(@NonNull Context context, List<CompetitionRecord> records, @NonNull Listener listener) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompetitionRecord record = records.get(position);

        holder.competition.setText(record.competition.getText(context));
        holder.year.setText(String.valueOf(record.year));
        holder.type.setText(record.type.getText(context));
        holder.place.setText(record.place);
        holder.performance.setText(String.format(Locale.getDefault(), record.competition.twoDigitsPrecision() ? "%.2f" : "%.0f", record.performance));
        holder.performanceIcon.setImageResource(record.competition.getIcon());
        holder.windy.setVisibility(record.windy ? View.VISIBLE : View.GONE);
        if (record.competition.hasWind()) {
            ((ViewGroup) holder.wind.getParent()).setVisibility(View.VISIBLE);
            holder.wind.setText(String.format(Locale.getDefault(), "%.1f", record.wind));
        } else {
            ((ViewGroup) holder.wind.getParent()).setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onRecordSelected(record));
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public interface Listener {
        void onRecordSelected(@NonNull CompetitionRecord record);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView year;
        final TextView competition;
        final TextView type;
        final TextView performance;
        final TextView wind;
        final ImageView performanceIcon;
        final TextView place;
        final TextView windy;

        ViewHolder(@NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_record, parent, false));

            year = itemView.findViewById(R.id.recordItem_year);
            competition = itemView.findViewById(R.id.recordItem_competition);
            type = itemView.findViewById(R.id.recordItem_type);
            performance = itemView.findViewById(R.id.recordItem_performance);
            performanceIcon = itemView.findViewById(R.id.recordItem_performanceIcon);
            wind = itemView.findViewById(R.id.recordItem_wind);
            place = itemView.findViewById(R.id.recordItem_place);
            windy = itemView.findViewById(R.id.recordItem_windy);

            FontsManager.set(FontsManager.ROBOTO_REGULAR, year, competition, place);
            FontsManager.set(FontsManager.ROBOTO_BOLD, type, windy);
            FontsManager.set(FontsManager.ROBOTO_LIGHT, performance, wind);
        }
    }
}
