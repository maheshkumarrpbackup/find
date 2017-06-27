/*
 * Copyright 2017 Hewlett-Packard Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

package com.hp.autonomy.frontend.find.core.savedsearches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SharedToUserServiceImpl implements SharedToUserService {
    private final SharedToUserRepository sharedToUserRepository;

    @Autowired
    public SharedToUserServiceImpl(final SharedToUserRepository sharedToUserRepository) {
        this.sharedToUserRepository = sharedToUserRepository;
    }

    @Override
    public SharedToUser save(final SharedToUser sharedToUser) {
        // merge in the created date if it exists
        final SharedToUser existingSharedToUser = sharedToUserRepository.findOne(sharedToUser.getId());
        sharedToUser.merge(existingSharedToUser);

        return sharedToUserRepository.save(sharedToUser);
    }
}
