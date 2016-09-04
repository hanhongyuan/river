(function() {
    'use strict';

    angular
        .module('riverApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inspector', {
            parent: 'entity',
            url: '/inspector',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'riverApp.inspector.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inspector/inspectors.html',
                    controller: 'InspectorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inspector');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('inspector-detail', {
            parent: 'entity',
            url: '/inspector/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'riverApp.inspector.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inspector/inspector-detail.html',
                    controller: 'InspectorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inspector');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Inspector', function($stateParams, Inspector) {
                    return Inspector.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inspector',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inspector-detail.edit', {
            parent: 'inspector-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inspector/inspector-dialog.html',
                    controller: 'InspectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inspector', function(Inspector) {
                            return Inspector.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inspector.new', {
            parent: 'inspector',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inspector/inspector-dialog.html',
                    controller: 'InspectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userName: null,
                                deviceSeq: null,
                                workDay: null,
                                workTime: null,
                                offWorkTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inspector', null, { reload: 'inspector' });
                }, function() {
                    $state.go('inspector');
                });
            }]
        })
        .state('inspector.edit', {
            parent: 'inspector',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inspector/inspector-dialog.html',
                    controller: 'InspectorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inspector', function(Inspector) {
                            return Inspector.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inspector', null, { reload: 'inspector' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inspector.delete', {
            parent: 'inspector',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inspector/inspector-delete-dialog.html',
                    controller: 'InspectorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inspector', function(Inspector) {
                            return Inspector.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inspector', null, { reload: 'inspector' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
