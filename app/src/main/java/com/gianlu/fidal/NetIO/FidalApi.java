package com.gianlu.fidal.NetIO;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FidalApi {
    private static final HttpUrl CALENDAR_URL = HttpUrl.get("http://www.fidal.it/calendario.php");
    private static FidalApi instance;
    private final OkHttpClient client;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Handler handler;

    private FidalApi() {
        client = new OkHttpClient();
        handler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    public static FidalApi get() {
        if (instance == null) instance = new FidalApi();
        return instance;
    }

    public void getCalendar(int year, Month month, Level level, Region region, Type type, Category category,
                            boolean federal, Approval approval, ApprovalType approvalType, OnResult<List<Event>> listener) {
        HttpUrl.Builder url = CALENDAR_URL.newBuilder();
        url.addQueryParameter("year", String.valueOf(year));
        url.addQueryParameter("month", String.valueOf(month.val()));
        url.addQueryParameter("livello", level.val);
        url.addQueryParameter("new_regione", region.val);
        url.addQueryParameter("new_tipo", String.valueOf(type.val));
        url.addQueryParameter("new_categoria", category.val);
        url.addQueryParameter("new_campionati", String.valueOf(federal ? 1 : 0));
        url.addQueryParameter("omologazione", approval.val);
        url.addQueryParameter("omologazione_tipo", approvalType.val);
        executorService.execute(new Requester<>(url.build(), new EventsProcessor(year), listener));
    }

    @NonNull
    private Document requestSync(@NonNull HttpUrl url) throws IOException {
        try (Response resp = client.newCall(new Request.Builder().url(url).get().build()).execute()) {
            if (resp.code() == 200) {
                ResponseBody body = resp.body();
                if (body == null)
                    throw new IOException(new NullPointerException("Request body is empty!"));
                return Jsoup.parse(body.string());
            } else {
                throw new IOException(String.format("%d: %s", resp.code(), resp.message()));
            }
        }
    }

    public enum Approval {
        ANY(""),
        YES("si"),
        NO("no");

        private final String val;

        Approval(String val) {
            this.val = val;
        }
    }

    public enum ApprovalType {
        ANY(""),
        A("a"),
        B("b");

        private final String val;

        ApprovalType(String val) {
            this.val = val;
        }
    }

    public enum Month {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER;

        private int val() {
            return ordinal() + 1;
        }
    }

    public enum Level {
        ANY(""),
        NATIONAL("COD"),
        REGIONAL("REG");

        private final String val;

        Level(String val) {
            this.val = val;
        }
    }

    public enum Region {
        ANY(""),
        ABRUZZO("ABRUZZO"),
        ALTO_ADIGE("ALTOADIGE"),
        BASILICATA("BASILICATA"),
        CALABRIA("CALABRIA"),
        CAMPANIA("CAMPANIA"),
        EMILIA_ROMAGNA("EMILIAROMAGNA"),
        FRIULI_VENEZIA_GIULIA("FRIULIVENEZIAGIULIA"),
        LAZIO("LAZIO"),
        LIGURIA("LIGURIA"),
        LOMBARDIA("LOMBARDIA"),
        MARCHE("MARCHE"),
        MOLISE("MOLISE"),
        PIEMONTE("PIEMONTE"),
        PUGLIA("PUGLIA"),
        SARDEGNA("SARDEGNA"),
        SICILIA("SICILIA"),
        TOSCANA("TOSCANA"),
        TRENTINO("TRENTINO"),
        UMBRIA("UMBRIA"),
        VALLEDAOSTA("VALLEDAOSTA"),
        VENETO("VENETO");

        public final String val;

        Region(String val) {
            this.val = val;
        }
    }

    public enum Type {
        ANY(0),
        CROSS(2),
        INDOR(3),
        MARCIA_STRADA(8),
        MONTAGNA(11),
        MONTAGNA_TRAIL(4),
        NORDIC_WALKING(13),
        OUTDOOR(5),
        PIAZZA_ALTRO(10),
        STRADA(6),
        TRAIL(12),
        ULTRAMARATONA(7),
        ULTRAMARATONA_TRAIL(9);

        public final int val;

        Type(int val) {
            this.val = val;
        }
    }

    public enum Category {
        ANY(""),
        ESORDIENTI("ESO"),
        RAGAZZI("RAG"),
        CADETTI("CAD"),
        ALLIEVI("ALL"),
        JUNIORES("JUN"),
        PROMESSE("PRO"),
        SENIORES("SEN"),
        MASTER("MAS");

        public final String val;

        Category(String val) {
            this.val = val;
        }
    }

    private interface Processor<A> {
        @NonNull
        A process(@NonNull Document document) throws ParseException;
    }

    public interface OnResult<A> {
        void result(@NonNull A result);

        void exception(@NonNull Exception ex);
    }

    private static class EventsProcessor implements Processor<List<Event>> {
        private final int year;

        private EventsProcessor(int year) {
            this.year = year;
        }

        @NonNull
        @Override
        public List<Event> process(@NonNull Document document) throws ParseException {
            List<Event> events = new ArrayList<>();
            Elements rows = document.selectFirst("#content .table_btm table tbody").children();
            for (Element row : rows) events.add(new Event(year, row));
            return events;
        }
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }

    private class Requester<A> implements Runnable {
        private final HttpUrl url;
        private final Processor<A> processor;
        private final OnResult<A> listener;

        private Requester(HttpUrl url, Processor<A> processor, OnResult<A> listener) {
            this.url = url;
            this.processor = processor;
            this.listener = listener;
        }

        @Override
        public void run() {
            try {
                Document document = requestSync(url);
                final A result = processor.process(document);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.result(result);
                    }
                });
            } catch (IOException | ParseException ex) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.exception(ex);
                    }
                });
            }
        }
    }
}
