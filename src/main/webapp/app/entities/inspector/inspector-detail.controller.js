(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('InspectorDetailController', InspectorDetailController);

    InspectorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Inspector', 'WorkRoad', 'Report', 'Message'];

    function InspectorDetailController($scope, $rootScope, $stateParams, previousState, entity, Inspector, WorkRoad, Report, Message) {
        var vm = this;

        vm.inspector = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('riverApp:inspectorUpdate', function(event, result) {
            vm.inspector = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
