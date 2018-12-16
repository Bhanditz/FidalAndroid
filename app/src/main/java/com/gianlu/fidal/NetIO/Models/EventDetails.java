package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;

import org.jetbrains.annotations.Contract;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EventDetails {
    public final String title;
    public final String desc;
    public final EventDate date;
    public final PageLink organizer;
    public final FidalApi.Type type;
    public final Event.Level level;
    public final Categories category;
    public final Sex sex;
    public final String place;
    public final String email;
    public final PageLink website;
    public final PageLink organization;
    public final FidalApi.Approval approval;
    public final FidalApi.ApprovalType approvalType;
    public final String authotiry;
    public final PageLink information;
    public final List<PageLink> attachments;
    public final PageLink entriesResults;

    public EventDetails(@NonNull Element element) throws FidalApi.ParseException {
        title = element.child(1).text();
        desc = element.child(2).text();

        Element others = element.selectFirst(".common_section table tbody");
        date = EventDate.fromEventDetails(findText(others, "Data svolgimento", false));
        organizer = findLink(others, "Organizzatore", true);
        type = FidalApi.Type.parseType(findText(others, "Tipologia", false));
        level = Event.Level.parseExtended(findText(others, "Livello", false));
        category = new Categories(find(others, "Categoria", true));
        sex = Sex.parseSex(findText(others, "Sesso", false));
        place = findText(others, "Località", false);
        email = findText(others, "Email", true);
        website = findLink(others, "Sito web", true);
        organization = findLink(others, "Organizzazione", true);
        authotiry = findText(others, "Ente", true);
        information = findLink(others, "Informazioni", true);
        attachments = findLinks(others, "Allegati", true);
        entriesResults = findLink(others, "Iscritti/Risultati", true);

        String approvalStr = findText(others, "Percorso omologato", true);
        approval = parseApproval(approvalStr);
        approvalType = parseApprovalType(approvalStr);
    }

    @Nullable
    private static FidalApi.Approval parseApproval(@Nullable String text) throws FidalApi.ParseException {
        if (text == null) return null;
        else if (text.startsWith("Si")) return FidalApi.Approval.YES;
        else if (text.startsWith("No")) return FidalApi.Approval.NO;
        else throw new FidalApi.ParseException("Unknown approval: " + text);
    }

    @Nullable
    private static FidalApi.ApprovalType parseApprovalType(@Nullable String text) throws FidalApi.ParseException {
        if (text == null) return null;

        String val = text.substring(14, 15);
        switch (val) {
            case "-":
                return null;
            case "A":
                return FidalApi.ApprovalType.A;
            case "B":
                return FidalApi.ApprovalType.B;
            default:
                throw new FidalApi.ParseException("Unknown approval type: " + text);
        }
    }

    @Nullable
    @Contract("_, _, false -> !null")
    private static String findText(@NonNull Element parent, @NonNull String name, boolean optional) throws FidalApi.ParseException {
        Element elm = find(parent, name, optional);
        if (elm == null) return null;
        else return elm.text();
    }

    @Nullable
    @Contract("_, _, false -> !null")
    private static PageLink findLink(@NonNull Element parent, @NonNull String name, boolean optional) throws FidalApi.ParseException {
        Element elm = find(parent, name, optional);
        if (elm == null) return null;

        Element a = elm.selectFirst("a");
        return new PageLink(a == null ? null : a.attr("href"), a == null ? elm.text() : a.text());
    }

    @NonNull
    private static List<PageLink> findLinks(@NonNull Element parent, @NonNull String name, boolean optional) throws FidalApi.ParseException {
        Element elm = find(parent, name, optional);
        if (elm == null) return Collections.emptyList();

        List<PageLink> list = new ArrayList<>();
        for (Element a : elm.select("a"))
            list.add(new PageLink(a.attr("href"), a.text()));
        return list;
    }

    @Nullable
    @Contract("_, _, false -> !null")
    private static Element find(@NonNull Element parent, @NonNull String name, boolean optional) throws FidalApi.ParseException {
        try {
            return parent.getElementsContainingOwnText(name).first().parent().parent().child(1);
        } catch (Exception ex) {
            if (optional) return null;
            else throw new FidalApi.ParseException("Couldn't find: " + name);
        }
    }

    public enum Sex {
        M, F, ANY;

        @NonNull
        public static Sex parseSex(@NonNull String str) throws FidalApi.ParseException {
            switch (str) {
                case "M":
                    return M;
                case "F":
                    return F;
                case "M/F":
                    return ANY;
                default:
                    throw new FidalApi.ParseException("Unknown sex: " + str);
            }
        }
    }

    public static class Categories {
        private final List<FidalApi.Category> list = new ArrayList<>();

        private Categories(@Nullable Element element) throws FidalApi.ParseException {
            if (element == null) return;

            for (TextNode node : element.textNodes()) {
                String text = node.text().trim();
                if (text.isEmpty()) continue;

                list.add(FidalApi.Category.parse(text.substring(0, 3)));
            }
        }
    }
}
