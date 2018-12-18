package com.gianlu.fidal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gianlu.commonutils.Dialogs.ActivityWithDialog;
import com.gianlu.commonutils.Logging;
import com.gianlu.commonutils.MessageView;
import com.gianlu.fidal.Adapters.FragmentPagerAdapter;
import com.gianlu.fidal.Athlete.HistoryFragment;
import com.gianlu.fidal.Athlete.RecordsFragment;
import com.gianlu.fidal.Athlete.ResultsFragment;
import com.gianlu.fidal.NetIO.FidalApi;
import com.gianlu.fidal.NetIO.Models.AthleteDetails;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public class AthleteActivity extends ActivityWithDialog implements FidalApi.OnResult<AthleteDetails> {
    private ViewPager pager;
    private MessageView message;
    private ProgressBar loading;
    private FidalApi fidal;
    private TextView dateOfBirth;
    private TextView club;
    private TextView membershipDetails;

    public static void startActivity(@NonNull Context context, @NonNull String url) {
        context.startActivity(new Intent(context, AthleteActivity.class).putExtra("url", url));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete);

        Toolbar toolbar = findViewById(R.id.athlete_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        loading = findViewById(R.id.athlete_loading);
        message = findViewById(R.id.athlete_message);
        club = findViewById(R.id.athlete_club);
        dateOfBirth = findViewById(R.id.athlete_dateOfBirth);
        membershipDetails = findViewById(R.id.athlete_membershipDetails);

        pager = findViewById(R.id.athlete_pager);
        pager.setOffscreenPageLimit(3);

        TabLayout tabLayout = findViewById(R.id.athlete_tabs);
        tabLayout.setupWithViewPager(pager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        String url = getIntent().getStringExtra("url");
        if (url == null) {
            pager.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            message.setError(R.string.failedLoading);
            return;
        }

        fidal = FidalApi.get();
        fidal.getAthleteDetails(url, this);
    }

    @Override
    public void result(@NonNull AthleteDetails result) {
        loading.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
        pager.setVisibility(View.VISIBLE);

        setTitle(getString(R.string.fidalSuffix, result.name));

        club.setText(result.club.text);
        dateOfBirth.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(result.dateOfBirth));
        membershipDetails.setText(result.membershipDetails);

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager(),
                ResultsFragment.getInstance(this),
                RecordsFragment.getInstance(this),
                HistoryFragment.getInstance(this)));
    }

    @Override
    public void exception(@NonNull Exception ex) {
        Logging.log(ex);
        pager.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
        message.setError(R.string.failedLoading_reason, ex.getMessage());
    }
}
