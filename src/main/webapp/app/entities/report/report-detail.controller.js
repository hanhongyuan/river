(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('ReportDetailController', ReportDetailController);

    ReportDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Report', 'Inspector'];

    function ReportDetailController($scope, $rootScope, $stateParams, previousState, entity, Report, Inspector) {
        var vm = this;

        vm.report = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('riverApp:reportUpdate', function(event, result) {
            vm.report = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
