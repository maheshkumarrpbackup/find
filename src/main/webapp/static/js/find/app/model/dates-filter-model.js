define([
    'backbone',
    'moment'
], function(Backbone, moment) {

    var DateRange = {
        custom: 'custom',
        year: 'year',
        month: 'month',
        week: 'week'
    };

    var dateRangeDescription = {
        year:  {maxDate: moment(), minDate: moment().subtract(1, 'years')},
        month: {maxDate: moment(), minDate: moment().subtract(1, 'months')},
        week: {maxDate: moment(), minDate: moment().subtract(1, 'weeks')}
    };

    return Backbone.Model.extend({
        defaults: {
            minDate: null,
            maxDate: null,
            dateRange: null
        },

        initialize: function(options) {
            this.queryModel = options.queryModel;

            this.listenTo(this, 'change', function() {
                this.queryModel.set({
                    minDate: this.get('minDate'),
                    maxDate: this.get('maxDate')
                })
            })
        },

        setDateRange: function(dateRange) {
            if(dateRange === DateRange.custom) {
                this.set({
                    minDate: this.get('mixDate'),
                    maxDate: this.get('maxDate'),
                    dateRange: DateRange.custom
                });
            } else if(dateRange) {
                var dateRangeProperties = dateRangeDescription[dateRange] || {};

                this.set({
                    minDate: dateRangeProperties.minDate,
                    maxDate: dateRangeProperties.maxDate,
                    dateRange: dateRange
                });
            } else {
                this.set({
                    minDate: null,
                    maxDate: null,
                    dateRange: null
                });
            }
        },

        setMinDate: function(date) {this.set({
            dateRange: DateRange.custom,
            minDate: date
        });
        },

        setMaxDate: function(date) {
            this.set({
                dateRange: DateRange.custom,
                maxDate: date
            });
        }
    }, {
        DateRange: DateRange
    });
});