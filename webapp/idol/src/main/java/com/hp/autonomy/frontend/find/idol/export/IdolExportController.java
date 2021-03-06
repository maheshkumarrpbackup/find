/*
 * Copyright 2015 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.frontend.find.idol.export;

import com.autonomy.aci.client.services.AciErrorException;
import com.hp.autonomy.frontend.find.core.export.ExportController;
import com.hp.autonomy.frontend.find.core.export.service.ExportFormat;
import com.hp.autonomy.frontend.find.core.export.service.ExportServiceFactory;
import com.hp.autonomy.frontend.find.core.export.service.PlatformDataExportService;
import com.hp.autonomy.frontend.find.core.web.ControllerUtils;
import com.hp.autonomy.frontend.find.core.web.RequestMapper;
import com.hp.autonomy.searchcomponents.core.search.StateTokenAndResultCount;
import com.hp.autonomy.searchcomponents.idol.search.IdolDocumentsService;
import com.hp.autonomy.searchcomponents.idol.search.IdolQueryRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

abstract class IdolExportController extends ExportController<IdolQueryRequest, AciErrorException> {

    private final IdolDocumentsService documentsService;

    protected IdolExportController(final RequestMapper<IdolQueryRequest> requestMapper,
                                   final ControllerUtils controllerUtils,
                                   final ExportServiceFactory<IdolQueryRequest, AciErrorException> exportServiceFactory,
                                   final IdolDocumentsService documentsService) {
        super(requestMapper, controllerUtils, exportServiceFactory);
        this.documentsService = documentsService;
    }

    @Override
    protected void export(final OutputStream outputStream,
                          final IdolQueryRequest queryRequest,
                          final Collection<String> selectedFieldNames) throws AciErrorException, IOException {
        final StateTokenAndResultCount stateTokenAndResultCount = documentsService.getStateTokenAndResultCount(queryRequest.getQueryRestrictions(), queryRequest.getMaxResults(), false);

        final IdolQueryRequest queryRequestWithStateToken = queryRequest.toBuilder()
                .queryRestrictions(queryRequest.getQueryRestrictions().toBuilder()
                        .stateMatchId(stateTokenAndResultCount.getTypedStateToken().getStateToken())
                        .build())
                .build();

        final ExportFormat exportFormat = getExportFormat();
        final PlatformDataExportService<IdolQueryRequest, AciErrorException> exportService = exportServiceFactory.getPlatformDataExportService(exportFormat)
                .orElseThrow(() -> new UnsupportedOperationException("Query result export not supported for format " + exportFormat.name()));
        exportService.exportQueryResults(outputStream, queryRequestWithStateToken, exportFormat, selectedFieldNames, stateTokenAndResultCount.getResultCount());
    }
}
