package com.gianlu.fidal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.gianlu.fidal.NetIO.Event;
import com.gianlu.fidal.NetIO.FidalApi;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FidalApi api = new FidalApi();
        api.getCalendar(2018, FidalApi.Month.SEPTEMBER, FidalApi.Level.ANY, FidalApi.Region.EMILIA_ROMAGNA, FidalApi.Type.ANY,
                FidalApi.Category.ANY, false, FidalApi.Approval.ANY, FidalApi.ApprovalType.ANY, new FidalApi.OnResult<List<Event>>() {
                    @Override
                    public void result(@NonNull List<Event> result) {
                        System.out.println(result);
                    }

                    @Override
                    public void exception(@NonNull Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }
}
