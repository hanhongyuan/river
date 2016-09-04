(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('WorkRoadDetailController', WorkRoadDetailController);

    WorkRoadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WorkRoad', 'Inspector'];

    function WorkRoadDetailController($scope, $rootScope, $stateParams, previousState, entity, WorkRoad, Inspector) {
        var vm = this;

        vm.workRoad = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('riverApp:workRoadUpdate', function(event, result) {
            vm.workRoad = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
