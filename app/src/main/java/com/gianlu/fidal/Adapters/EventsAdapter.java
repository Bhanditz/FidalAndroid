package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.commonutils.SuperTextView;
import com.gianlu.fidal.NetIO.Event;
import com.gianlu.fidal.R;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<Event> events;

    public EventsAdapter(Context context, List<Event> events) {
        this.inflater = LayoutInflater.from(context);
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.name.setText(event.name);
        holder.type.setHtml(R.string.eventType, event.type.name());
        holder.level.setHtml(R.string.eventLevel, event.level.name());
        holder.date.setHtml(R.string.eventDate, event.date.toReadable());
        holder.place.setHtml(R.string.eventPlace, event.place);

        if (event.desc.isEmpty()) {
            holder.desc.setVisibility(View.GONE);
        } else {
            holder.desc.setVisibility(View.VISIBLE);
            holder.desc.setText(event.desc);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
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
