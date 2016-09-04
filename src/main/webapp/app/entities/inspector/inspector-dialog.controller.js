(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('InspectorDialogController', InspectorDialogController);

    InspectorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inspector', 'WorkRoad', 'Report', 'Message'];

    function InspectorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Inspector, WorkRoad, Report, Message) {
        var vm = this;

        vm.inspector = entity;
        vm.clear = clear;
        vm.save = save;
        vm.workroads = WorkRoad.query();
        vm.reports = Report.query();
        vm.messages = Message.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.inspector.id !== null) {
                Inspector.update(vm.inspector, onSaveSuccess, onSaveError);
            } else {
                Inspector.save(vm.inspector, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('riverApp:inspectorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
