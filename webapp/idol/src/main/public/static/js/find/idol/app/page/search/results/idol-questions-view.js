/*
 * Copyright 2016-2017 Hewlett Packard Enterprise Development Company, L.P.
 * Licensed under the MIT License (the "License"); you may not use this file except in compliance with the License.
 */

define([
    'underscore',
    'jquery',
    'backbone',
    'find/idol/app/model/answer-bank/idol-answered-questions-collection',
    'js-whatever/js/list-view',
    'text!find/templates/app/page/search/results/questions-container.html',
    'i18n!find/nls/bundle'
], function(_, $, Backbone, AnsweredQuestionsCollection, ListView, questionsTemplate, i18n) {
    'use strict';

    const MAX_SIZE = 1;
    const CROPPED_SUMMARY_CHAR_LENGTH = 300;

    return Backbone.View.extend({
        events: {
            'click .read-more': function(e) {
                const $target = $(e.currentTarget);
                const $summary = $target.siblings('.summary-text');
                $summary.toggleClass('result-summary');

                const isResultSummary = $summary.hasClass('result-summary');
                $target.text(isResultSummary ? i18n['app.more'] : i18n['app.less']);
                $summary.children('.extended-answer')
                    .toggleClass('hide', isResultSummary);
                $target.siblings('.summary-text')
                    .children('.ellipsis')
                    .toggleClass('hide', !isResultSummary);
            }
        },

        initialize: function(options) {
            this.answeredQuestionsCollection = new AnsweredQuestionsCollection();
            this.queryModel = options.queryModel;
            this.loadingTracker = options.loadingTracker;
            this.clearLoadingSpinner = options.clearLoadingSpinner;

            this.template = _.template(questionsTemplate);
        },

        render: function() {
            this.$('[data-toggle="tooltip"]').tooltip('destroy');

            const html = this.answeredQuestionsCollection.map(function(answeredQuestion) {
                const croppedAnswer = answeredQuestion.get('answer').slice(0, CROPPED_SUMMARY_CHAR_LENGTH);
                const extendedAnswer = answeredQuestion.get('answer').slice(CROPPED_SUMMARY_CHAR_LENGTH);

                return this.template({
                    i18n: i18n,
                    model: answeredQuestion,
                    croppedAnswer: croppedAnswer,
                    extendedAnswer: extendedAnswer,
                    showMoreButton: answeredQuestion.get('answer').length > CROPPED_SUMMARY_CHAR_LENGTH
                });
            }, this).join('');

            this.$el.html(html);
            this.$('[data-toggle="tooltip"]').tooltip({
                container: 'body',
                placement: 'top'
            });

            return this;
        },

        fetchData: function() {
            this.loadingTracker.questionsFinished = false;
            this.$el.empty();

            this.answeredQuestionsCollection.fetch({
                data: {
                    text: this.queryModel.get('queryText'),
                    maxResults: MAX_SIZE
                },
                reset: true,
                success: _.bind(function() {
                    this.render();
                    this.loadingTracker.questionsFinished = true;
                    this.clearLoadingSpinner();
                }, this),
                error: _.bind(function() {
                    this.loadingTracker.questionsFinished = true;
                    this.clearLoadingSpinner();
                }, this)
            }, this);
        },

        remove: function() {
            this.$('[data-toggle="tooltip"]').tooltip('destroy');
            Backbone.View.prototype.remove.call(this);
        }
    });
});
