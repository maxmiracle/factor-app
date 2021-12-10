package org.maxvas.factorapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Новостная статья.
 */
@AllArgsConstructor
@Data
public class ArticleData {

    /**
     * URL статьи.
     */
    private String link;

    /**
     * Заголовок статьи.
     */
    private String title;

    /**
     * Текст статьи.
     */
    private String text;

    /**
     * Статус загрузки статьи из интернета.
     */
    private boolean isSuccess;

    /**
     * Описание ошибки.
     */
    private String error;
}
