/*
 * Copyright 2015-2017 Hewlett Packard Enterprise Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

define([
    'underscore',
    'find/app/model/find-base-collection'
], function(_, FindBaseCollection) {
    'use strict';

    function defaultCurrentRangeAttributes(absoluteRange) {
        return absoluteRange.max === absoluteRange.min
            // The current max must always be greater than the current min for bars to be visible
            // on the widgets. If there is only one value for the field, the absolute max will equal
            // the absolute min. In this case, default to a range spanning 1 around this value.
            ? {
                currentMin: absoluteRange.min - 0.5,
                currentMax: absoluteRange.min + 0.5
            }
            // It is not possible to specify inclusive upper ranges when fetching parametric values from IDOL.
            : {
                currentMin: absoluteRange.min,
                // To display the extreme values, default to a range 1% larger than the data.
                currentMax: absoluteRange.max + 0.01 * (absoluteRange.max - absoluteRange.min)
            };
    }

    // The currentMin and currentMax attributes are the current range displayed on numeric/date widgets.
    return FindBaseCollection.extend({
        url: 'api/public/fields/parametric',

        model: FindBaseCollection.Model.extend({
            defaults: _.extend({
                min: 0,
                max: 0,
                totalValues: 0
            }, defaultCurrentRangeAttributes({min: 0, max: 0})),

            parse: function(response) {
                return _.defaults(defaultCurrentRangeAttributes(response), response);
            },

            getDefaultCurrentRange: function() {
                return defaultCurrentRangeAttributes(this.pick('min', 'max'));
            }
        })
    });
});
