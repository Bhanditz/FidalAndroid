package com.gianlu.fidal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.commonutils.Spinners.LabeledSpinner;
import com.gianlu.fidal.Adapters.EventsAdapter;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Event;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements FidalApi.OnResult<List<Event>>, EventsAdapter.Listener {
    private RecyclerViewLayout layout;
    private FidalApi api;
    private LabeledSpinner year;
    private LabeledSpinner month;
    private LabeledSpinner level;
    private LabeledSpinner region;
    private LabeledSpinner type;
    private LabeledSpinner category;
    private CheckBox federalChampionship;
    private LabeledSpinner approval;
    private LabeledSpinner approvalType;
    private EventBottomSheet bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        year = findViewById(R.id.main_year);
        month = findViewById(R.id.main_month);
        level = findViewById(R.id.main_level);
        region = findViewById(R.id.main_region);
        type = findViewById(R.id.main_type);
        category = findViewById(R.id.main_category);
        federalChampionship = findViewById(R.id.main_federalChampionship);
        approval = findViewById(R.id.main_approval);
        approvalType = findViewById(R.id.main_approvalType);

        year.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        year.setNumberItems(FidalApi.yearsRange(), Calendar.getInstance().get(Calendar.YEAR));
        month.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        month.setItems(FidalApi.Month.list(), FidalApi.Month.now());
        level.setOnItemSelectedListener(new CollateralItemSelectionListener<>(new LabeledSpinner.SelectListener<FidalApi.Level>() {
            @Override
            public void selected(@NonNull FidalApi.Level item) {
                type.setItems(FidalApi.Type.list(item), FidalApi.Type.ANY);
                if (item == FidalApi.Level.REGIONAL) region.setVisibility(View.VISIBLE);
                else region.setVisibility(View.GONE);
            }
        }));
        level.setItems(FidalApi.Level.list(), FidalApi.Level.ANY);
        region.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        region.setItems(FidalApi.Region.list(), FidalApi.Region.ANY);
        type.setOnItemSelectedListener(new CollateralItemSelectionListener<>(new LabeledSpinner.SelectListener<FidalApi.Type>() {
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
        }));
        type.setItems(FidalApi.Type.list((FidalApi.Level) level.getSelectedItem()), FidalApi.Type.ANY);
        category.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        category.setItems(FidalApi.Category.list(), FidalApi.Category.ANY);
        federalChampionship.setOnCheckedChangeListener(new CollateralItemSelectionListener<>(null));
        federalChampionship.setChecked(false);
        approval.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        approval.setItems(FidalApi.Approval.list(), FidalApi.Approval.ANY);
        approvalType.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        approvalType.setItems(FidalApi.ApprovalType.list(), FidalApi.ApprovalType.ANY);

        layout = findViewById(R.id.main_layout);
        layout.disableSwipeRefresh();
        layout.getList().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layout.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        api = FidalApi.get();
        somethingChanged();
    }

    private void somethingChanged() {
        layout.startLoading();
        api.getCalendar((Integer) year.getSelectedItem(), (FidalApi.Month) month.getSelectedItem(),
                (FidalApi.Level) level.getSelectedItem(), (FidalApi.Region) region.getSelectedItem(), (FidalApi.Type) type.getSelectedItem(),
                (FidalApi.Category) category.getSelectedItem(), federalChampionship.isChecked(),
                (FidalApi.Approval) approval.getSelectedItem(), (FidalApi.ApprovalType) approvalType.getSelectedItem(), this);
    }

    @Override
    public void result(@NonNull List<Event> result) {
        if (result.isEmpty()) {
            layout.showInfo(R.string.noEventsFound);
            return;
        }

        layout.loadListData(new EventsAdapter(this, result, this));
    }

    @Override
    public void exception(@NonNull Exception ex) {
        layout.showError(R.string.failedLoading_reason, ex.getLocalizedMessage());
    }

    @Override
    public void selected(@NonNull Event event) {
        bottomSheet = new EventBottomSheet();
        bottomSheet.show(this, event);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheet.isVisible()) bottomSheet.dismiss();
        else super.onBackPressed();
    }

    private class CollateralItemSelectionListener<A> implements LabeledSpinner.SelectListener<A>, CompoundButton.OnCheckedChangeListener {
        private final LabeledSpinner.SelectListener<A> listener;

        private CollateralItemSelectionListener(@Nullable LabeledSpinner.SelectListener<A> listener) {
            this.listener = listener;
        }

        private void postChanged() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    somethingChanged();
                }
            });
        }

        @Override
        public void selected(@NonNull A item) {
            if (listener != null) listener.selected(item);
            postChanged();
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            postChanged();
        }
    }
}
