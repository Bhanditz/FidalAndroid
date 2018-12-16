package com.gianlu.fidal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gianlu.commonutils.RecyclerViewLayout;
import com.gianlu.commonutils.Spinners.LabeledSpinner;
import com.gianlu.commonutils.Toaster;
import com.gianlu.fidal.Adapters.EventsAdapter;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.Event;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        level.setOnItemSelectedListener(new CollateralItemSelectionListener<>((LabeledSpinner.SelectListener<FidalApi.Level>) item -> {
            type.setItems(FidalApi.Type.list(item), FidalApi.Type.ANY);
            if (item == FidalApi.Level.REGIONAL) region.setVisibility(View.VISIBLE);
            else region.setVisibility(View.GONE);
        }));
        level.setItems(FidalApi.Level.list(), FidalApi.Level.ANY);
        region.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        region.setItems(FidalApi.Region.list(), FidalApi.Region.ANY);
        region.setVisibility(View.GONE);
        type.setOnItemSelectedListener(new CollateralItemSelectionListener<>((LabeledSpinner.SelectListener<FidalApi.Type>) item -> {
            if (item.hasApproval()) {
                approval.setVisibility(View.VISIBLE);
                approvalType.setVisibility(View.VISIBLE);
            } else {
                approval.setVisibility(View.GONE);
                approvalType.setVisibility(View.GONE);
            }
        }));
        type.setItems(FidalApi.Type.list(level.getSelectedItem()), FidalApi.Type.ANY);
        category.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        category.setItems(FidalApi.Category.list(), FidalApi.Category.ANY);
        federalChampionship.setOnCheckedChangeListener(new CollateralItemSelectionListener<>(null));
        federalChampionship.setChecked(false);
        approval.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        approval.setItems(FidalApi.Approval.list(), FidalApi.Approval.ANY);
        approval.setVisibility(View.GONE);
        approvalType.setOnItemSelectedListener(new CollateralItemSelectionListener<>(null));
        approvalType.setItems(FidalApi.ApprovalType.list(), FidalApi.ApprovalType.ANY);
        approvalType.setVisibility(View.GONE);

        layout = findViewById(R.id.main_layout);
        layout.disableSwipeRefresh();
        layout.getList().addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        layout.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        if (Objects.equals(getIntent().getAction(), Intent.ACTION_VIEW) || Objects.equals(getIntent().getAction(), Intent.ACTION_SEND)) {
            Uri url = getIntent().getData();
            if (url != null) {
                try {
                    setInputFromUrl(url);
                } catch (FidalApi.ParseException ex) {
                    Toaster.with(this).message(R.string.failedOpeningUrl).ex(ex).show();
                }
            }
        }

        api = FidalApi.get();
        somethingChanged();
    }

    private void setInputFromUrl(@NonNull Uri url) throws FidalApi.ParseException {
        String yearStr = url.getQueryParameter("anno");
        if (yearStr != null && yearStr.isEmpty()) {
            try {
                year.setSelectedItem(Integer.parseInt(yearStr), false);
            } catch (NumberFormatException ignored) {
            }
        }

        String monthStr = url.getQueryParameter("mese");
        if (monthStr != null && monthStr.isEmpty()) {
            try {
                month.setSelectedItem(FidalApi.Month.fromInt(Integer.parseInt(monthStr)), false);
            } catch (NumberFormatException ignored) {
            }
        }

        String levelStr = url.getQueryParameter("livello");
        if (levelStr != null)
            level.setSelectedItem(FidalApi.Level.parse(levelStr), false);

        String regionStr = url.getQueryParameter("new_regione");
        if (regionStr != null)
            region.setSelectedItem(FidalApi.Region.parse(regionStr), false);

        String typeStr = url.getQueryParameter("new_tipo");
        if (typeStr != null && typeStr.isEmpty()) {
            try {
                type.setSelectedItem(FidalApi.Type.fromInt(Integer.parseInt(typeStr)), false);
            } catch (NumberFormatException ignored) {
            }
        }

        String categoryStr = url.getQueryParameter("new_categoria");
        if (categoryStr != null)
            category.setSelectedItem(FidalApi.Category.parse(categoryStr), false);

        String federalStr = url.getQueryParameter("new_campionati");
        if (federalStr != null)
            federalChampionship.setChecked(Boolean.parseBoolean(federalStr));

        String approvalStr = url.getQueryParameter("omologazione");
        if (approvalStr != null)
            approval.setSelectedItem(FidalApi.Approval.parse(approvalStr), false);

        String approvalTypeStr = url.getQueryParameter("omologazione_tipo");
        if (approvalTypeStr != null)
            approvalType.setSelectedItem(FidalApi.ApprovalType.parse(approvalTypeStr), false);
    }

    private void somethingChanged() {
        layout.startLoading();
        api.getCalendar(year.getSelectedItem(), month.getSelectedItem(),
                level.getSelectedItem(), region.getSelectedItem(), type.getSelectedItem(),
                category.getSelectedItem(), federalChampionship.isChecked(),
                approval.getSelectedItem(), approvalType.getSelectedItem(), this);
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
            runOnUiThread(MainActivity.this::somethingChanged);
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
