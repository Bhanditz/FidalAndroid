package com.gianlu.fidal.NetIO.Models;

import com.gianlu.fidal.NetIO.FidalApi;

import org.jsoup.nodes.Element;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PageLink implements Serializable {
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
