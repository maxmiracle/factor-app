package org.maxvas.factorapp.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.maxvas.factorapp.entity.ArticleData;
import org.maxvas.factorapp.entity.Factor;
import org.maxvas.factorapp.repository.FactorRepository;
import org.maxvas.factorapp.service.FactorService;
import org.maxvas.factorapp.service.ListArticlesService;
import org.maxvas.factorapp.service.TheGuardianArticleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.Message;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.maxvas.factorapp.common.HeaderConstants.HEADER_DATE_FROM_LINK;

@Configuration
@AllArgsConstructor
@Slf4j
public class ArticleProcessConfig {

    private final ListArticlesService listArticlesService;

    private final TheGuardianArticleService theGuardianArticleService;

    private final FactorService factorService;

    private final FactorRepository factorRepository;

    @Bean
    public IntegrationFlow articles() {
        return flow -> flow
                .enrichHeaders(h -> h.headerFunction("newsDate", message -> message.getPayload()))
                .<LocalDate, List<String>>transform(date -> listArticlesService.getLinksByDate(date))
                .split()
//                .<Message, Message>transform(Message.class, message -> {
//                    return MutableMessageBuilder.fromMessage(message)
//                            .setHeader(MessageHeaders.ID, UUID.randomUUID())
//                            .build();
//                })
                .enrichHeaders(h -> h.headerFunction("link", message -> message.getPayload()))
                .enrichHeaders(h -> h.headerFunction(HEADER_DATE_FROM_LINK, message -> ListArticlesService.getDateFromLink((String) message.getPayload())))
                .<String, ArticleData>transform(link -> theGuardianArticleService.getArticleData(link))
                .<ArticleData>filter(p -> p.isSuccess())
                .<ArticleData>filter(p -> Objects.isNull(factorRepository.findByLink(p.getLink())))
                .<Message, Message>transform(Message.class, message -> factorService.processArticle(message))
                .handle(message -> {
                            log.info("handle {}", message);
                            factorRepository.save((Factor)message.getPayload());
                        }
                );
    }

    @MessagingGateway
    public interface ArticleIndexer {

        @Gateway(requestChannel = "articles.input")
        void indexArticle(LocalDate date);

    }


}
