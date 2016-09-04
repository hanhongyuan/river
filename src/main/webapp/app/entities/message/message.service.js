(function() {
    'use strict';
    angular
        .module('riverApp')
        .factory('Message', Message);

    Message.$inject = ['$resource', 'DateUtils'];

    function Message ($resource, DateUtils) {
        var resourceUrl =  'api/messages/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createDateTime = DateUtils.convertDateTimeFromServer(data.createDateTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
