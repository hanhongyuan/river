(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('WorkRoadDeleteController',WorkRoadDeleteController);

    WorkRoadDeleteController.$inject = ['$uibModalInstance', 'entity', 'WorkRoad'];

    function WorkRoadDeleteController($uibModalInstance, entity, WorkRoad) {
        var vm = this;

        vm.workRoad = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WorkRoad.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
