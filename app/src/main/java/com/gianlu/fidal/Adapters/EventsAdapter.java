package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.commonutils.SuperTextView;
import com.gianlu.fidal.NetIO.Models.Event;
import com.gianlu.fidal.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Event> events;
    private final Listener listener;
    private final Context context;

    public EventsAdapter(Context context, List<Event> events, Listener listener) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = events.get(position);
        holder.name.setText(event.name);
        holder.type.setHtml(R.string.eventType, event.type.getText(context));
        holder.level.setHtml(R.string.eventLevel, event.level.getText(context));
        holder.date.setHtml(R.string.eventDate, event.date.toReadable());
        holder.place.setHtml(R.string.eventPlace, event.place);

        if (event.desc.isEmpty()) {
            holder.desc.setVisibility(View.GONE);
        } else {
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(event.desc);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.selected(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public interface Listener {
        void selected(@NonNull Event event);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView desc;
        final SuperTextView type;
        final SuperTextView level;
        final SuperTextView date;
        final SuperTextView place;

        ViewHolder(@NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_event, parent, false));

            name = itemView.findViewById(R.id.eventItem_name);
            desc = itemView.findViewById(R.id.eventItem_desc);
            type = itemView.findViewById(R.id.eventItem_type);
            level = itemView.findViewById(R.id.eventItem_level);
            date = itemView.findViewById(R.id.eventItem_date);
            place = itemView.findViewById(R.id.eventItem_place);
        }
    }
}
