package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

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
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;

        ViewHolder(@NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_event, parent, false));

            name = itemView.findViewById(R.id.eventItem_name);
        }
    }
}
