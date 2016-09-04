(function() {
    'use strict';

    angular
        .module('riverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('work-road', {
            parent: 'entity',
            url: '/work-road',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'riverApp.workRoad.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-road/work-roads.html',
                    controller: 'WorkRoadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workRoad');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('work-road-detail', {
            parent: 'entity',
            url: '/work-road/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'riverApp.workRoad.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/work-road/work-road-detail.html',
                    controller: 'WorkRoadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workRoad');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkRoad', function($stateParams, WorkRoad) {
                    return WorkRoad.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'work-road',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('work-road-detail.edit', {
            parent: 'work-road-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-road/work-road-dialog.html',
                    controller: 'WorkRoadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkRoad', function(WorkRoad) {
                            return WorkRoad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-road.new', {
            parent: 'work-road',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-road/work-road-dialog.html',
                    controller: 'WorkRoadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                roadName: null,
                                points: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('work-road', null, { reload: 'work-road' });
                }, function() {
                    $state.go('work-road');
                });
            }]
        })
        .state('work-road.edit', {
            parent: 'work-road',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-road/work-road-dialog.html',
                    controller: 'WorkRoadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkRoad', function(WorkRoad) {
                            return WorkRoad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-road', null, { reload: 'work-road' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('work-road.delete', {
            parent: 'work-road',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/work-road/work-road-delete-dialog.html',
                    controller: 'WorkRoadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkRoad', function(WorkRoad) {
                            return WorkRoad.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('work-road', null, { reload: 'work-road' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
