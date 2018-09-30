package com.gianlu.fidal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.commonutils.Spinners.LabeledSpinner;
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

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        LabeledSpinner year = findViewById(R.id.main_year);
        year.setNumberItems(FidalApi.yearsRange());
        year.setOnItemSelectedListener(new LabeledSpinner.SelectListener<Integer>() {
            @Override
            public void selected(@NonNull Integer year) {
                System.out.println(year);
            }
        });
        LabeledSpinner month = findViewById(R.id.main_month);
        month.setItems(FidalApi.Month.list());
        LabeledSpinner level = findViewById(R.id.main_level);
        level.setItems(FidalApi.Level.list());
        LabeledSpinner region = findViewById(R.id.main_region);
        region.setItems(FidalApi.Region.list());
        LabeledSpinner type = findViewById(R.id.main_type);
        type.setItems(FidalApi.Type.list());
        LabeledSpinner category = findViewById(R.id.main_category);
        category.setItems(FidalApi.Category.list());
        CheckBox federalChampionship = findViewById(R.id.main_federalChampionship);
        LabeledSpinner approval = findViewById(R.id.main_approval);
        approval.setItems(FidalApi.Approval.list());
        LabeledSpinner approvalType = findViewById(R.id.main_approvalType);
        approvalType.setItems(FidalApi.ApprovalType.list());

        layout = findViewById(R.id.main_layout);
        layout.disableSwipeRefresh();
        layout.getList().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layout.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

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
