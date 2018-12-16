package com.gianlu.fidal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlu.commonutils.FontsManager;
import com.gianlu.fidal.NetIO.Models.Event;
import com.gianlu.fidal.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.date.setText(event.date.toReadableShort());

        holder.type.setText(event.type.getText(context));
        holder.typeIcon.setImageResource(event.type.getIcon());

        holder.level.setText(event.level.getText(context));
        holder.levelIcon.setImageResource(event.level.getIcon());

        holder.place.setText(event.place);

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
        final TextView date;
        final TextView name;
        final TextView desc;
        final TextView type;
        final ImageView typeIcon;
        final TextView level;
        final ImageView levelIcon;
        final TextView place;

        ViewHolder(@NonNull ViewGroup parent) {
            super(inflater.inflate(R.layout.item_event, parent, false));

            name = itemView.findViewById(R.id.eventItem_name);
            desc = itemView.findViewById(R.id.eventItem_desc);
            type = itemView.findViewById(R.id.eventItem_type);
            typeIcon = itemView.findViewById(R.id.eventItem_typeIcon);
            level = itemView.findViewById(R.id.eventItem_level);
            levelIcon = itemView.findViewById(R.id.eventItem_levelIcon);
            date = itemView.findViewById(R.id.eventItem_date);
            place = itemView.findViewById(R.id.eventItem_place);

            FontsManager.set(date, FontsManager.ROBOTO_LIGHT);
            FontsManager.set(desc, FontsManager.ROBOTO_LIGHT);
            FontsManager.set(name, FontsManager.ROBOTO_REGULAR);
        }
    }
}
