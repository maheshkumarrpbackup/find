/*
 * Copyright 2016 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.frontend.find.core.savedsearches;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavedSearch {
    private final Long id;
    private final String title;
    private final String inputText;
    private final Set<String> relatedConcepts;
    private final Set<NameAndDomain> indexes;
    private final Set<FieldAndValue> parametricValues;
    private final DateTime minDate;
    private final DateTime maxDate;
    private final DateTime dateCreated;
    private final DateTime dateModified;

    @JsonCreator
    public SavedSearch(
            @JsonProperty("id") final Long id,
            @JsonProperty("title") final String title,
            @JsonProperty("inputText") final String inputText,
            @JsonProperty("relatedConcepts") final Set<String> relatedConcepts,
            @JsonProperty("indexes") final Set<NameAndDomain> indexes,
            @JsonProperty("parametricValues") final Set<FieldAndValue> parametricValues,
            @JsonProperty("minDate") final DateTime minDate,
            @JsonProperty("maxDate") final DateTime maxDate,
            @JsonProperty("dateCreated") final DateTime dateCreated,
            @JsonProperty("dateModified") final DateTime dateModified
    ) {
        this.id = id;
        this.title = title;
        this.inputText = inputText;
        this.relatedConcepts = relatedConcepts;
        this.indexes = indexes;
        this.parametricValues = parametricValues;
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class Builder {
        private Long id;
        private String title;
        private String inputText;
        private Set<NameAndDomain> indexes;
        private Set<FieldAndValue> parametricValues;
        private DateTime minDate;
        private DateTime maxDate;
        private DateTime dateCreated;
        private DateTime dateModified;
        private Set<String> relatedConcepts;

        public Builder(final SavedSearch search) {
            id = search.id;
            title = search.title;
            inputText = search.inputText;
            indexes = search.indexes;
            parametricValues = search.parametricValues;
            minDate = search.minDate;
            maxDate = search.maxDate;
            dateCreated = search.dateCreated;
            dateModified = search.dateModified;
            relatedConcepts = search.relatedConcepts;
        }

        public SavedSearch build() {
            return new SavedSearch(
                    id,
                    title,
                    inputText,
                    relatedConcepts,
                    indexes,
                    parametricValues,
                    minDate,
                    maxDate,
                    dateCreated,
                    dateModified
            );
        }
    }
}
