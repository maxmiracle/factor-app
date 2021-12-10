package org.maxvas.factorapp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "factors")
@Data
public class Factor {
    @Id
    private String id;

    /**
     * Ссылка на статью.
     */
    private String link;

    /**
     * Дата публикации.
     */
    private Date articleDate;

    /**
     * Дата создания записи
     */
    private Date createDate;

    /**
     * Core NLP analysis
     */
    CoreNLPArticleStatistics coreNLPArticleStatistics;


}
