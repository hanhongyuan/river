(function() {
    'use strict';
    angular
        .module('riverApp')
        .factory('Inspector', Inspector);

    Inspector.$inject = ['$resource'];

    function Inspector ($resource) {
        var resourceUrl =  'api/inspectors/:id';

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
