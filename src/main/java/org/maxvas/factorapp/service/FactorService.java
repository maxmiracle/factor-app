package org.maxvas.factorapp.service;

import lombok.AllArgsConstructor;
import org.maxvas.factorapp.entity.ArticleData;
import org.maxvas.factorapp.entity.CoreNLPArticleStatistics;
import org.maxvas.factorapp.entity.Factor;
import org.maxvas.factorapp.metric.CoreNLPMetric;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.maxvas.factorapp.common.HeaderConstants.HEADER_DATE_FROM_LINK;

@Service
@AllArgsConstructor
public class FactorService {

    private final CoreNLPMetric coreNLPMetric;

    public Message<Factor> processArticle(Message message){
        ArticleData article = (ArticleData) message.getPayload();
        CoreNLPArticleStatistics coreNLPArticleStatistics = coreNLPMetric.processArticle(article.getText());
        Factor factor = new Factor();
        factor.setCoreNLPArticleStatistics(coreNLPArticleStatistics);
        LocalDate dateFromLink = (LocalDate)message.getHeaders().get(HEADER_DATE_FROM_LINK);
        factor.setArticleDate(Date.from(dateFromLink.atStartOfDay(ZoneId.of("UTC")).toInstant()));
        factor.setCreateDate(Date.from(Instant.now()));
        factor.setLink(article.getLink());
        return MessageBuilder.withPayload(factor)
                .copyHeaders(message.getHeaders())
                .build();
    }
}
