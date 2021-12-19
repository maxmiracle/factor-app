package org.maxvas.factorapp;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.maxvas.factorapp.common.DateRange;
import org.maxvas.factorapp.config.ArticleProcessConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
@Slf4j
class FactorApplicationTests {

    private final static LocalDate dateToIndex = LocalDate.of(2014, 1, 1);
    @Autowired
    ArticleProcessConfig.ArticleIndexer articleIndexer;

    @Test
    void contextLoads() {
    }

    @Test
    void processArticle() {
        articleIndexer.indexArticle(dateToIndex);
    }

//    @Test
//    void convertDate() {
//        DateTimeFormatter urlPartFormatter = DateTimeFormatter.ofPattern("yyyy/LLL/dd").withLocale(Locale.ENGLISH);
//        String year = "2014";
//        String mon = "jan";
//        String dayOfMonth = "01";
//        LocalDate date = LocalDate.parse(String.join("/", year, mon.substring(0, 1).toUpperCase() + mon.substring(1), dayOfMonth), urlPartFormatter);
//    }


    @SneakyThrows
    @Test
    void processArticleForMonth(){
        LocalDate startDate = LocalDate.of(2021, 01, 01);
        LocalDate endDate = LocalDate.of(2021, 12, 16);
        log.info("start process");
        for (LocalDate d : new DateRange(startDate, endDate)){
            articleIndexer.indexArticle(d);
        }
        log.info("finish process");
    }
}
