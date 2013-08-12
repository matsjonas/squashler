/**
 * Initiate typeahead plugin on all player typeahead fields.
 */
$(function() {
    $('input.player-typeahead').typeahead({
        name: 'accounts',
        remote: {
            url: 'players/%QUERY',
            rateLimitWait: 200,
            filter: function(parsedResponse) {
                var datums = [];
                for (var i = 0 ; i < parsedResponse.result.length ; i++) {
                    var datum = {
                        value: parsedResponse.result[i ].name,
                        tokens: [parsedResponse.result[i ].name]
                    };
                    datums.push(datum);
                }
                return datums;
            }
        }
    });
});