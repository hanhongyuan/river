(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('WorkRoadDialogController', WorkRoadDialogController);

    WorkRoadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WorkRoad', 'Inspector'];

    function WorkRoadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WorkRoad, Inspector) {
        var vm = this;

        vm.workRoad = entity;
        vm.clear = clear;
        vm.save = save;
        vm.inspectors = Inspector.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.workRoad.id !== null) {
                WorkRoad.update(vm.workRoad, onSaveSuccess, onSaveError);
            } else {
                WorkRoad.save(vm.workRoad, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('riverApp:workRoadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
