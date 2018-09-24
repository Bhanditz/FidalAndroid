package com.gianlu.fidal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.commonutils.Toaster;
import com.gianlu.fidal.Adapters.EventsAdapter;
import com.gianlu.fidal.NetIO.Event;
import com.gianlu.fidal.NetIO.FidalApi;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FidalApi.OnResult<List<Event>> {
    private RecyclerViewLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layout = findViewById(R.id.main_layout);
        layout.disableSwipeRefresh();
        layout.getList().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layout.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        layout.startLoading();

        FidalApi api = FidalApi.get();
        api.getCalendar(2018, FidalApi.Month.SEPTEMBER, FidalApi.Level.ANY, FidalApi.Region.EMILIA_ROMAGNA, FidalApi.Type.ANY,
                FidalApi.Category.ANY, false, FidalApi.Approval.ANY, FidalApi.ApprovalType.ANY, this);
    }

    @Override
    public void result(@NonNull List<Event> result) {
        layout.loadListData(new EventsAdapter(this, result));
    }

    @Override
    public void exception(@NonNull Exception ex) {
        Toaster.with(this).message(R.string.failedLoading).ex(ex).show();
    }
}
