'use strict';

describe('Controller Tests', function() {

    describe('Inspector Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockInspector, MockWorkRoad, MockReport, MockMessage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockInspector = jasmine.createSpy('MockInspector');
            MockWorkRoad = jasmine.createSpy('MockWorkRoad');
            MockReport = jasmine.createSpy('MockReport');
            MockMessage = jasmine.createSpy('MockMessage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Inspector': MockInspector,
                'WorkRoad': MockWorkRoad,
                'Report': MockReport,
                'Message': MockMessage
            };
            createController = function() {
                $injector.get('$controller')("InspectorDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'riverApp:inspectorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
