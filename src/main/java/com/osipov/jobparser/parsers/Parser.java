package com.osipov.jobparser.parsers;

import com.osipov.jobparser.models.Vacancy;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public abstract class Parser {
//    @Value("${web.userAgent}")
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 YaBrowser/23.11.0.0 Safari/537.36";

    public abstract List<Vacancy> parse(String url) throws IOException;

    protected Document getHtmlCode(String url) throws IOException {
        return Jsoup.connect(url).userAgent(userAgent).get();
    }

    protected boolean validateParameters(String... parameters){
        AtomicBoolean flag = new AtomicBoolean(true);
        Arrays.stream(parameters).forEach(i ->{
            if (i == null | i.isEmpty()) flag.set(false);
        });
        return flag.get();
    }
}
