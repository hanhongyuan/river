(function() {
    'use strict';
    angular
        .module('riverApp')
        .factory('WorkRoad', WorkRoad);

    WorkRoad.$inject = ['$resource'];

    function WorkRoad ($resource) {
        var resourceUrl =  'api/work-roads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
