package com.gianlu.fidal.NetIO.Models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.gianlu.fidal.NetIO.FidalApi;

import org.jsoup.nodes.Element;

public class PageLink {
    public final String url;
    public final String text;

    PageLink(@Nullable String url, @NonNull String text) {
        this.url = url;
        this.text = text;
    }

    @NonNull
    public static PageLink extract(Element element) throws FidalApi.ParseException {
        if (!element.tagName().equals("a"))
            throw new FidalApi.ParseException("Element is not a link: " + element);
        return new PageLink(element.attr("href"), element.text());
    }
}
