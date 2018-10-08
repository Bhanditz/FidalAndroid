package com.gianlu.fidal;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gianlu.commonutils.BottomSheet.ThemedModalBottomSheet;
import com.gianlu.commonutils.Dialogs.DialogUtils;
import com.gianlu.commonutils.FontsManager;
import com.gianlu.commonutils.Toaster;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Event;
import com.gianlu.fidal.NetIO.Models.EventDetails;

public class EventBottomSheet extends ThemedModalBottomSheet<Event, EventDetails> {

    @Override
    protected boolean onCreateHeader(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @NonNull Event payload) {
        inflater.inflate(R.layout.sheet_header_event_details, parent, true);

        TextView title = parent.findViewById(R.id.eventDetailsSheetHeader_title);
        FontsManager.set(title, FontsManager.ROBOTO_REGULAR);
        title.setText(payload.name);

        TextView desc = parent.findViewById(R.id.eventDetailsSheetHeader_desc);
        if (payload.desc == null || payload.desc.isEmpty()) {
            desc.setVisibility(View.GONE);
        } else {
            desc.setVisibility(View.VISIBLE);
            FontsManager.set(desc, FontsManager.ROBOTO_LIGHT);
            desc.setText(payload.desc);
        }

        return true;
    }

    @Override
    protected void onCreateBody(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @NonNull Event payload) {
        isLoading(true);

        FidalApi.get().getEvent(payload, new FidalApi.OnResult<EventDetails>() {
            @Override
            public void result(@NonNull EventDetails result) {
                update(result);
            }

            @Override
            public void exception(@NonNull Exception ex) {
                DialogUtils.showToast(getActivity(), Toaster.build().message(R.string.failedLoading).ex(ex));
                dismiss();
            }
        });
    }

    @Override
    protected void onRequestedUpdate(@NonNull EventDetails payload) {
        isLoading(false);
        // TODO: Fill body
    }

    @Override
    protected void onCustomizeToolbar(@NonNull Toolbar toolbar, @NonNull Event payload) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        toolbar.setTitle(payload.name);
    }

    @Override
    protected boolean onCustomizeAction(@NonNull FloatingActionButton action, @NonNull Event payload) {
        return false;
    }

    @Override
    protected int getCustomTheme(@NonNull Event payload) {
        return R.style.AppTheme;
    }
}
