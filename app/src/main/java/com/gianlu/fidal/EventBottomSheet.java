package com.gianlu.fidal;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gianlu.commonutils.BottomSheet.BaseModalBottomSheet;
import com.gianlu.commonutils.Dialogs.DialogUtils;
import com.gianlu.commonutils.Toaster;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Event;
import com.gianlu.fidal.NetIO.Models.EventDetails;

public class EventBottomSheet extends BaseModalBottomSheet<Event, EventDetails> {

    @Override
    protected boolean onCreateHeader(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, @NonNull Event payload) {
        return false;
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
}
