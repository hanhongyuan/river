(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('InspectorDeleteController',InspectorDeleteController);

    InspectorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inspector'];

    function InspectorDeleteController($uibModalInstance, entity, Inspector) {
        var vm = this;

        vm.inspector = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Inspector.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
