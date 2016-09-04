(function() {
    'use strict';

    angular
        .module('riverApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state'];

    function HomeController ($scope, Principal, LoginService, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        $scope.labels = ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"];
        $scope.series = ['今年', '去年'];
        $scope.data = [
            [65, 59, 80, 81, 56, 55, 40, 65, 59, 80, 81, 56],
            [28, 48, 40, 19, 86, 27, 90, 28, 48, 40, 19, 86]
        ];
        $scope.onClick = function (points, evt) {
            console.log(points, evt);
        };
        $scope.datasetOverride = [{ yAxisID: 'y-axis-1' }, { yAxisID: 'y-axis-2' }];
        $scope.options = {
            scales: {
                yAxes: [
                    {
                        id: 'y-axis-1',
                        type: 'linear',
                        display: true,
                        position: 'left'
                    },
                    {
                        id: 'y-axis-2',
                        type: 'linear',
                        display: true,
                        position: 'right'
                    }
                ]
            }
        };

        $scope.insLabels = ["巡视员A", "巡视员B", "巡视员C", "巡视员C", "巡视员C", "巡视员C", "巡视员C", "巡视员C", "巡视员C"];
        $scope.insData = [300, 500, 100, 150, 100, 80, 230, 310, 200];

        $scope.barColors = ['#45b7cd', '#ff6384', '#ff8e72'];

        $scope.barLabels = ['巡视员A', '巡视员B', '巡视员C', '巡视员D', '巡视员E', '巡视员F', '巡视员G', '巡视员H', '巡视员I', '巡视员J',
            '巡视员K', '巡视员L', '巡视员M', '巡视员N', '巡视员O', '巡视员P', '巡视员Q', '巡视员R', '巡视员S', '巡视员T'];
        $scope.barData = [
            [65, 59, 80, 81, 56, 55, 40, 59, 80, 81, 56, 55, 40, 81, 56, 55, 40, 59, 80, 81]
        ];

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
