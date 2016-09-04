(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('WorkRoadController', WorkRoadController);

    WorkRoadController.$inject = ['$scope', '$state', 'WorkRoad'];

    function WorkRoadController ($scope, $state, WorkRoad) {
        var vm = this;
        
        vm.workRoads = [];

        loadAll();

        function loadAll() {
            WorkRoad.query(function(result) {
                vm.workRoads = result;
            });
        }
    }
})();
