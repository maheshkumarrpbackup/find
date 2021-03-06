/*
 * Copyright 2015-2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.frontend.find.core.export.service;

import lombok.Getter;

@Getter
public enum ExportFormat {
    CSV("text/csv", "csv"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx");

    private final String mimeType;
    private final String extension;

    ExportFormat(final String mimeType, final String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }
}
