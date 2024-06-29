package com.osipov.jobparser.parsers;

import com.osipov.jobparser.models.Profession;
import com.osipov.jobparser.models.Vacancy;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Data
public abstract class Parser {
    @Value("${user.agent.from.env}")
    private String userAgent;

    protected String address;

    public abstract List<Vacancy> parse(Profession profession) throws IOException;

    protected Document getHtmlCode(String url) throws IOException {
        return Jsoup.connect(url).userAgent(userAgent).get();
    }

    protected boolean validateParameters(String... parameters){
        for (String s : parameters){
            if (s == null){
                messageAboutFail(parameters);
                return false;
            }
            if (s.isEmpty()) {
                messageAboutFail(parameters);
                return false;
            }
        }
        return true;
    }
    private void messageAboutFail(String... parameters){
        System.out.println("Не удалось добавить : \n\t" + Arrays.toString(parameters));
    }
}
