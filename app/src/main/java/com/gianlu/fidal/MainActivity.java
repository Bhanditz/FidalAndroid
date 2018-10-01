package com.gianlu.fidal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.commonutils.Spinners.LabeledSpinner;
import com.gianlu.commonutils.Toaster;
import com.gianlu.fidal.Adapters.EventsAdapter;
import com.gianlu.fidal.NetIO.Event;
import com.gianlu.fidal.NetIO.FidalApi;

import java.util.Calendar;
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
        LabeledSpinner month = findViewById(R.id.main_month);
        LabeledSpinner level = findViewById(R.id.main_level);
        LabeledSpinner region = findViewById(R.id.main_region);
        final LabeledSpinner type = findViewById(R.id.main_type);
        LabeledSpinner category = findViewById(R.id.main_category);
        CheckBox federalChampionship = findViewById(R.id.main_federalChampionship);
        final LabeledSpinner approval = findViewById(R.id.main_approval);
        final LabeledSpinner approvalType = findViewById(R.id.main_approvalType);

        year.setNumberItems(FidalApi.yearsRange(), Calendar.getInstance().get(Calendar.YEAR));
        month.setItems(FidalApi.Month.list(), FidalApi.Month.now());
        level.setOnItemSelectedListener(new LabeledSpinner.SelectListener<FidalApi.Level>() {
            @Override
            public void selected(@NonNull FidalApi.Level item) {
                type.setItems(FidalApi.Type.list(item), FidalApi.Type.ANY);
            }
        });
        level.setItems(FidalApi.Level.list(), FidalApi.Level.ANY);
        region.setItems(FidalApi.Region.list(), FidalApi.Region.ANY);
        type.setOnItemSelectedListener(new LabeledSpinner.SelectListener<FidalApi.Type>() {
            @Override
            public void selected(@NonNull FidalApi.Type item) {
                if (item.hasApproval()) {
                    approval.setVisibility(View.VISIBLE);
                    approvalType.setVisibility(View.VISIBLE);
                } else {
                    approval.setVisibility(View.GONE);
                    approvalType.setVisibility(View.GONE);
                }
            }
        });
        type.setItems(FidalApi.Type.list((FidalApi.Level) level.getSelectedItem()), FidalApi.Type.ANY);
        category.setItems(FidalApi.Category.list(), FidalApi.Category.ANY);
        federalChampionship.setChecked(false);
        approval.setItems(FidalApi.Approval.list(), FidalApi.Approval.ANY);
        approvalType.setItems(FidalApi.ApprovalType.list(), FidalApi.ApprovalType.ANY);


        layout = findViewById(R.id.main_layout);
        layout.disableSwipeRefresh();
        layout.getList().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layout.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        layout.startLoading();

        FidalApi api = FidalApi.get();
        api.getCalendar((Integer) year.getSelectedItem(), (FidalApi.Month) month.getSelectedItem(),
                (FidalApi.Level) level.getSelectedItem(), (FidalApi.Region) region.getSelectedItem(), (FidalApi.Type) type.getSelectedItem(),
                (FidalApi.Category) category.getSelectedItem(), federalChampionship.isChecked(),
                (FidalApi.Approval) approval.getSelectedItem(), (FidalApi.ApprovalType) approvalType.getSelectedItem(), this);
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
