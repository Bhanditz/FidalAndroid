package com.gianlu.fidal.NetIO;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.gianlu.commonutils.GetText;
import com.gianlu.commonutils.Logging;
import com.gianlu.fidal.NetIO.Models.Event;
import com.gianlu.fidal.NetIO.Models.EventDetails;
import com.gianlu.fidal.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    private static final Processor<EventDetails> EVENT_DETAILS_PROCESSOR = new Processor<EventDetails>() {
        @NonNull
        @Override
        public EventDetails process(@NonNull Document document) throws ParseException {
            Element element = document.selectFirst("#content .section .text-holder");
            return new EventDetails(element);
        }
    };
    private static FidalApi instance;
    private final OkHttpClient client;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Handler handler;
    private Requester<?> currentGetCalendarTask = null;

    private FidalApi() {
        client = new OkHttpClient.Builder()
                .followRedirects(false)
                .build();
        handler = new Handler(Looper.getMainLooper());
    }

    @NonNull
    public static FidalApi get() {
        if (instance == null) instance = new FidalApi();
        return instance;
    }

    @NonNull
    public static List<Integer> yearsRange() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        List<Integer> list = new ArrayList<>((year - 2002) + 1);
        for (int i = year; i > 2002; i--) list.add(i);
        return list;
    }

    /**
     * Only one request at a time is allowed for this method.
     */
    public void getCalendar(int year, Month month, Level level, Region region, Type type, Category category,
                            boolean federal, Approval approval, ApprovalType approvalType, OnResult<List<Event>> listener) {
        HttpUrl.Builder url = CALENDAR_URL.newBuilder();
        url.addQueryParameter("submit", "Invia");
        url.addQueryParameter("anno", String.valueOf(year));
        url.addQueryParameter("mese", String.valueOf(month.val()));
        url.addQueryParameter("livello", region != Region.ANY ? Level.REGIONAL.val : level.val);
        if (level == Level.REGIONAL) url.addQueryParameter("new_regione", region.val);
        url.addQueryParameter("new_tipo", String.valueOf(type.val));
        url.addQueryParameter("new_categoria", category.val);
        url.addQueryParameter("new_campionati", String.valueOf(federal ? 1 : 0));
        if (type.hasApproval()) {
            url.addQueryParameter("omologazione", approval.val);
            url.addQueryParameter("omologazione_tipo", approvalType.val);
        }

        if (currentGetCalendarTask != null) currentGetCalendarTask.abort();
        currentGetCalendarTask = new Requester<>(url.build(), new EventsProcessor(year), listener);
        executorService.execute(currentGetCalendarTask);
    }

    @NonNull
    private Document requestSync(@NonNull HttpUrl url) throws IOException {
        try (Response resp = client.newCall(new Request.Builder().url(url).get().build()).execute()) {
            if (resp.code() == 302) {
                throw new IOException(String.format("Attempted redirect to %s from %s.", resp.header("Location"), url.toString()));
            } else if (resp.code() != 200) {
                throw new IOException(String.format("%d: %s (%s)", resp.code(), resp.message(), url.toString()));
            }

            ResponseBody body = resp.body();
            if (body == null)
                throw new IOException(new NullPointerException("Request body is empty!"));

            return Jsoup.parse(body.string());
        }
    }

    public void getEvent(@NonNull Event payload, OnResult<EventDetails> listener) {
        executorService.execute(new Requester<>(HttpUrl.get(payload.url), EVENT_DETAILS_PROCESSOR, listener));
    }

    public enum Approval implements GetText {
        ANY(""), YES("si"), NO("no");

        private final String val;

        Approval(String val) {
            this.val = val;
        }

        @NonNull
        public static List<Approval> list() {
            return Arrays.asList(values());
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case ANY:
                    return context.getString(R.string.any);
                case YES:
                    return context.getString(R.string.yes);
                case NO:
                    return context.getString(R.string.no);
                default:
                    throw new IllegalArgumentException("Unknown approval: " + this);
            }
        }
    }

    public enum ApprovalType implements GetText {
        ANY(""), A("a"), B("b");

        private final String val;

        ApprovalType(String val) {
            this.val = val;
        }

        @NonNull
        public static List<ApprovalType> list() {
            return Arrays.asList(values());
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case ANY:
                    return context.getString(R.string.any);
                case A:
                    return "A";
                case B:
                    return "B";
                default:
                    throw new IllegalArgumentException("Unknown approval type: " + this);
            }
        }
    }

    public enum Month implements GetText {
        JANUARY, FEBRUARY, MARCH, APRIL,
        MAY, JUNE, JULY, AUGUST,
        SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

        @NonNull
        public static List<Month> list() {
            return Arrays.asList(values());
        }

        @NonNull
        public static Month now() {
            return values()[Calendar.getInstance().get(Calendar.MONTH)];
        }

        private int val() {
            return ordinal() + 1;
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case JANUARY:
                    return context.getString(R.string.month_january);
                case FEBRUARY:
                    return context.getString(R.string.month_february);
                case MARCH:
                    return context.getString(R.string.month_march);
                case APRIL:
                    return context.getString(R.string.month_april);
                case MAY:
                    return context.getString(R.string.month_may);
                case JUNE:
                    return context.getString(R.string.month_june);
                case JULY:
                    return context.getString(R.string.month_july);
                case AUGUST:
                    return context.getString(R.string.month_august);
                case SEPTEMBER:
                    return context.getString(R.string.month_september);
                case OCTOBER:
                    return context.getString(R.string.month_october);
                case NOVEMBER:
                    return context.getString(R.string.month_november);
                case DECEMBER:
                    return context.getString(R.string.month_december);
                default:
                    throw new IllegalArgumentException("Unknown month: " + this);
            }
        }
    }

    public enum Level implements GetText {
        ANY(""), NATIONAL("COD"), REGIONAL("REG");

        private final String val;

        Level(String val) {
            this.val = val;
        }

        @NonNull
        public static List<Level> list() {
            return Arrays.asList(values());
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case ANY:
                    return context.getString(R.string.any);
                case NATIONAL:
                    return context.getString(R.string.level_national);
                case REGIONAL:
                    return context.getString(R.string.level_regional);
                default:
                    throw new IllegalArgumentException("Unknown level: " + this);
            }
        }
    }

    public enum Region implements GetText {
        ANY(""), ABRUZZO("ABRUZZO"), ALTO_ADIGE("ALTOADIGE"), BASILICATA("BASILICATA"),
        CALABRIA("CALABRIA"), CAMPANIA("CAMPANIA"), EMILIA_ROMAGNA("EMILIAROMAGNA"),
        FRIULI_VENEZIA_GIULIA("FRIULIVENEZIAGIULIA"), LAZIO("LAZIO"), LIGURIA("LIGURIA"),
        LOMBARDIA("LOMBARDIA"), MARCHE("MARCHE"), MOLISE("MOLISE"), PIEMONTE("PIEMONTE"),
        PUGLIA("PUGLIA"), SARDEGNA("SARDEGNA"), SICILIA("SICILIA"), TOSCANA("TOSCANA"),
        TRENTINO("TRENTINO"), UMBRIA("UMBRIA"), VALLEDAOSTA("VALLEDAOSTA"), VENETO("VENETO");

        public final String val;

        Region(String val) {
            this.val = val;
        }

        @NonNull
        public static List<Region> list() {
            return Arrays.asList(values());
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case ANY:
                    return context.getString(R.string.any);
                case ABRUZZO:
                    return context.getString(R.string.region_abruzzo);
                case ALTO_ADIGE:
                    return context.getString(R.string.region_alto_adige);
                case BASILICATA:
                    return context.getString(R.string.region_basilicata);
                case CALABRIA:
                    return context.getString(R.string.region_calabria);
                case CAMPANIA:
                    return context.getString(R.string.region_campania);
                case EMILIA_ROMAGNA:
                    return context.getString(R.string.region_emilia_romagna);
                case FRIULI_VENEZIA_GIULIA:
                    return context.getString(R.string.region_friuli_venezia_giulia);
                case LAZIO:
                    return context.getString(R.string.region_lazio);
                case LIGURIA:
                    return context.getString(R.string.region_liguria);
                case LOMBARDIA:
                    return context.getString(R.string.region_lombardia);
                case MARCHE:
                    return context.getString(R.string.region_marche);
                case MOLISE:
                    return context.getString(R.string.region_molise);
                case PIEMONTE:
                    return context.getString(R.string.region_piemonte);
                case PUGLIA:
                    return context.getString(R.string.region_puglia);
                case SARDEGNA:
                    return context.getString(R.string.region_sardegna);
                case SICILIA:
                    return context.getString(R.string.region_sicilia);
                case TOSCANA:
                    return context.getString(R.string.region_toscana);
                case TRENTINO:
                    return context.getString(R.string.region_trentino);
                case UMBRIA:
                    return context.getString(R.string.region_umbria);
                case VALLEDAOSTA:
                    return context.getString(R.string.region_valledaosta);
                case VENETO:
                    return context.getString(R.string.region_veneto);
                default:
                    throw new IllegalArgumentException("Unknown region: " + this);
            }
        }
    }

    public enum Type implements GetText {
        ANY(0), CROSS(2), INDOOR(3), PISTA_REGIONAL(5), MARCIA_STRADA(8), MONTAGNA(11),
        MONTAGNA_TRAIL(4), NORDIC_WALKING(13), OUTDOOR(5), PIAZZA_ALTRO(10), STRADA(6),
        TRAIL(12), ULTRAMARATONA(7), ULTRAMARATONA_TRAIL(9), MONTAGNA_REGIONAL(4), TRAIL_REGIONAL(7),;

        public final int val;

        Type(int val) {
            this.val = val;
        }

        @NonNull
        public static List<Type> list(@NonNull Level level) {
            if (level == Level.REGIONAL) {
                return Arrays.asList(ANY, CROSS, INDOOR, MONTAGNA_REGIONAL, PISTA_REGIONAL, STRADA, TRAIL_REGIONAL);
            } else {
                return Arrays.asList(ANY, CROSS, INDOOR, MARCIA_STRADA, MONTAGNA, MONTAGNA_TRAIL, NORDIC_WALKING,
                        OUTDOOR, PISTA_REGIONAL, STRADA, TRAIL, ULTRAMARATONA, ULTRAMARATONA_TRAIL);
            }
        }

        @NonNull
        public static FidalApi.Type parseType(@NonNull String text) throws FidalApi.ParseException {
            switch (text) {
                case "OUTDOOR":
                    return FidalApi.Type.OUTDOOR;
                case "STRADA":
                    return FidalApi.Type.STRADA;
                case "MONTAGNA":
                    return FidalApi.Type.MONTAGNA;
                case "ULTRAMARATONA":
                    return FidalApi.Type.ULTRAMARATONA;
                case "TRAIL":
                    return FidalApi.Type.TRAIL;
                case "PIAZZA e altri ambiti":
                    return FidalApi.Type.PIAZZA_ALTRO;
                case "INDOOR":
                    return FidalApi.Type.INDOOR;
                case "NORDIC WALKING":
                    return FidalApi.Type.NORDIC_WALKING;
                case "CROSS":
                    return FidalApi.Type.CROSS;
                case "MONTAGNA/TRAIL":
                    return FidalApi.Type.MONTAGNA_TRAIL;
                case "MARCIA SU STRADA":
                    return FidalApi.Type.MARCIA_STRADA;
                default:
                    throw new FidalApi.ParseException("Unknown type: " + text);
            }
        }

        @DrawableRes
        public int getIcon() {
            switch (this) {
                case STRADA:
                case MARCIA_STRADA:
                    return R.drawable.map_city;
                case MONTAGNA_REGIONAL:
                case MONTAGNA:
                case NORDIC_WALKING:
                case MONTAGNA_TRAIL:
                    return R.drawable.mountains;
                case PISTA_REGIONAL:
                case OUTDOOR:
                    return R.drawable.running_track;
                case INDOOR:
                    return R.drawable.indoor_track;
                case PIAZZA_ALTRO:
                    return R.drawable.urban;
                case TRAIL_REGIONAL:
                case TRAIL:
                case CROSS:
                case ULTRAMARATONA:
                case ULTRAMARATONA_TRAIL:
                    return R.drawable.forest;
                default:
                    throw new IllegalArgumentException("Missing icon for " + this);
            }
        }

        public boolean hasApproval() {
            return this == CROSS || this == MONTAGNA_REGIONAL || this == STRADA ||
                    this == TRAIL_REGIONAL || this == MARCIA_STRADA || this == ULTRAMARATONA_TRAIL;
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case ANY:
                    return context.getString(R.string.any);
                case CROSS:
                    return context.getString(R.string.type_cross);
                case INDOOR:
                    return context.getString(R.string.type_indoor);
                case PISTA_REGIONAL:
                    return context.getString(R.string.type_track);
                case MARCIA_STRADA:
                    return context.getString(R.string.type_marciaStrada);
                case MONTAGNA:
                case MONTAGNA_REGIONAL:
                    return context.getString(R.string.type_montagna);
                case MONTAGNA_TRAIL:
                    return context.getString(R.string.type_montagnaTrail);
                case NORDIC_WALKING:
                    return context.getString(R.string.type_nordicWalking);
                case OUTDOOR:
                    return context.getString(R.string.type_outdoor);
                case PIAZZA_ALTRO:
                    return context.getString(R.string.type_piazzaAltro);
                case STRADA:
                    return context.getString(R.string.type_strada);
                case TRAIL_REGIONAL:
                case TRAIL:
                    return context.getString(R.string.type_trail);
                case ULTRAMARATONA:
                    return context.getString(R.string.type_ultramaratona);
                case ULTRAMARATONA_TRAIL:
                    return context.getString(R.string.type_ultramaratonaTrail);
                default:
                    throw new IllegalArgumentException("Unknown type: " + this);
            }
        }
    }

    public enum Category implements GetText {
        ANY(""), ESORDIENTI("ESO"), RAGAZZI("RAG"), CADETTI("CAD"), ALLIEVI("ALL"),
        JUNIORES("JUN"), PROMESSE("PRO"), SENIORES("SEN"), MASTER("MAS");

        public final String val;

        Category(String val) {
            this.val = val;
        }

        @NonNull
        public static List<Category> list() {
            return Arrays.asList(values());
        }

        @NonNull
        public static Category parseCategory(@NonNull String val) throws ParseException {
            for (Category cat : values())
                if (cat.val.equals(val)) return cat;

            throw new ParseException("Unknown category: " + val);
        }

        @NonNull
        @Override
        public String getText(@NonNull Context context) {
            switch (this) {
                case ANY:
                    return context.getString(R.string.any);
                case ESORDIENTI:
                    return context.getString(R.string.category_eso);
                case RAGAZZI:
                    return context.getString(R.string.category_rag);
                case CADETTI:
                    return context.getString(R.string.category_cad);
                case ALLIEVI:
                    return context.getString(R.string.category_all);
                case JUNIORES:
                    return context.getString(R.string.category_jun);
                case PROMESSE:
                    return context.getString(R.string.category_pro);
                case SENIORES:
                    return context.getString(R.string.category_sen);
                case MASTER:
                    return context.getString(R.string.category_mas);
                default:
                    throw new IllegalArgumentException("Unknown category: " + this);
            }
        }
    }

    private interface Processor<A> {
        @NonNull
        @WorkerThread
        A process(@NonNull Document document) throws ParseException;
    }

    @UiThread
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
        public List<Event> process(@NonNull Document document) {
            List<Event> events = new ArrayList<>();
            Elements rows = document.selectFirst("#content .table_btm table tbody").children();

            boolean first = true;
            for (Element row : rows) {
                if (first && row.text().contains("Non sono disponibili manifestazioni con i filtri selezionati"))
                    break;

                first = false;

                try {
                    events.add(new Event(year, row));
                } catch (ParseException ex) {
                    Logging.log(ex);
                }
            }

            return events;
        }
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }

        public ParseException(Throwable ex) {
            super(ex);
        }
    }

    private class Requester<A> implements Runnable {
        private final HttpUrl url;
        private final Processor<A> processor;
        private final OnResult<A> listener;
        private volatile boolean aborted = false;

        private Requester(HttpUrl url, Processor<A> processor, OnResult<A> listener) {
            this.url = url;
            this.processor = processor;
            this.listener = listener;
        }

        private void abort() {
            aborted = true;
        }

        @Override
        public void run() {
            if (aborted) return;

            try {
                Document document = requestSync(url);
                if (!aborted) {
                    final A result = processor.process(document);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.result(result);
                        }
                    });
                }
            } catch (IOException | ParseException ex) {
                if (!aborted) {
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
}
