package com.osipov.jobparser.parsers;

import com.osipov.jobparser.models.Vacancy;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class HHParser extends Parser{

    @Override
    public List<String> parse(String url) throws IOException {
        Document document = getHtmlCode(url);
        Elements elements = document.select("span.serp-item__title-link-wrapper > a.bloko-link");
        List<String> strings = new ArrayList<>();

        for (Element element : elements){
            Vacancy vacancy = new Vacancy();
            vacancy.setUrl(element.attr("href"));
            Document currentVacancy = getHtmlCode(vacancy.getUrl());

            String address = currentVacancy.select("span.magritte-text___pbpft_3-0-8 > " +
                    "span.magritte-text___tkzIl_4-1-2").text();

            String city = address.split(",")[0].split("\\s+")[0];
            if (city.equals("Switch")) continue;

            String company = Objects.requireNonNull(
                    currentVacancy.select(
                            "a.bloko-link.bloko-link_kind-tertiary > " +
                                    "span.bloko-header-section-2.bloko-header-section-2_lite")
                            .select("span").first()).text();

            String title = currentVacancy.select("div.vacancy-title > h1").text();

            strings.add("url=" + vacancy.getUrl() +
                    ", city=" + city +
                    ", company=" + company +
                    ", title=" + title);
        }
        return strings;
    }
}
