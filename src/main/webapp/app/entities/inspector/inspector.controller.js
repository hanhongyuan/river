(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('InspectorController', InspectorController);

    InspectorController.$inject = ['$scope', '$state', 'Inspector'];

    function InspectorController ($scope, $state, Inspector) {
        var vm = this;
        
        vm.inspectors = [];

        loadAll();

        function loadAll() {
            Inspector.query(function(result) {
                vm.inspectors = result;
            });
        }
    }
})();
