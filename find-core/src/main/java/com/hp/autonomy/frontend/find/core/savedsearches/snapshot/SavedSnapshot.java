package com.hp.autonomy.frontend.find.core.savedsearches.snapshot;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hp.autonomy.frontend.find.core.savedsearches.SavedSearch;
import com.hp.autonomy.frontend.find.core.savedsearches.SavedSearchType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import java.util.List;

@Entity
@DiscriminatorValue(SavedSearchType.Values.SNAPSHOT)
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonDeserialize(builder = SavedSnapshot.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavedSnapshot extends SavedSearch {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = StoredStateTable.NAME, joinColumns = {
            @JoinColumn(name = "search_id")
    })
    @Column(name = StoredStateTable.Column.STATE_TOKEN)
    private List<String> stateTokens;

    @Column(name = Table.Column.TOTAL_RESULTS)
    private Long resultCount;

    private SavedSnapshot(final Builder builder) {
        super(builder);
        stateTokens = builder.stateToken;
        resultCount = builder.resultCount;
    }

    @NoArgsConstructor
    @Setter
    @Accessors(chain = true)
    public static class Builder extends SavedSearch.Builder<SavedSnapshot> {
        private List<String> stateToken;
        private Long resultCount;

        public Builder(final SavedSnapshot snapshot) {
            super(snapshot);

            stateToken = snapshot.stateTokens;
            resultCount = snapshot.resultCount;
        }

        @Override
        public SavedSnapshot build() {return new SavedSnapshot(this);}
    }
}
