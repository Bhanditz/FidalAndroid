package com.gianlu.fidal.NetIO;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.jsoup.nodes.Element;

public class EventDetails {
    public final String title;
    public final String desc;
    public final String date;
    public final String organizer;
    public final String type;
    public final String level;
    public final String category;
    public final String sex;
    public final String place;
    public final String email;
    public final String website;
    public final String organization;
    public final String approval;
    public final String authotiry;
    public final String information;
    public final String attachments;
    public final String entriesResults;

    EventDetails(@NonNull Element element) throws FidalApi.ParseException {
        title = element.child(1).text();
        desc = element.child(2).text();

        Element others = element.selectFirst(".common_section table tbody");
        date = find(others, "Data svolgimento", false);
        organizer = find(others, "Organizzatore", true);
        type = find(others, "Tipologia", false);
        level = find(others, "Livello", false);
        category = find(others, "Categoria", false);
        sex = find(others, "Sesso", false);
        place = find(others, "Località", false);
        email = find(others, "Email", true);
        website = find(others, "Sito web", true);
        organization = find(others, "Organizzazione", true);
        approval = find(others, "Percorso omologato", true);
        authotiry = find(others, "Entity", true);
        information = find(others, "Informazioni", true);
        attachments = find(others, "Allegati", true);
        entriesResults = find(others, "Iscritti/Risultati", true);
    }

    @Nullable
    private static String find(@NonNull Element element, @NonNull String name, boolean optional) throws FidalApi.ParseException {
        try {
            return element.getElementsContainingOwnText(name).first().parent().parent().child(1).text();
        } catch (Exception ex) {
            if (optional) return null;
            else throw new FidalApi.ParseException("Couldn't find: " + name);
        }
    }
}
