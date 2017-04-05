'use strict';

/* Controllers */

// Controller for the main page (list of drawings)
function MainController($scope, DrawingService, WssService) {
    // get location of WS service
    $scope.location = WssService.get();
    // obtain drawings from the RESTful service
    $scope.drawings = DrawingService.query();

    // deletes a single drawing by ID
    $scope.remove = function ($drawingId) {
        DrawingService.remove({drawingId: $drawingId});
    };

    // adds a new drawing
    $scope.addDrawing = function () {
        var newDrawing = new DrawingService({name: $scope.drawingName});
        $scope.drawingName = '';
        newDrawing.$save();
    };

    // listens to server-sent events for the list of drawings
    $scope.eventSource = new EventSource("/api/drawings/events");

    var eventHandler = function (event) {
        $scope.drawings = DrawingService.query();
    };


    $scope.eventSource.addEventListener("create", eventHandler, false);
    $scope.eventSource.addEventListener("update", eventHandler, false);
    $scope.eventSource.addEventListener("delete", eventHandler, false);


    // clean up
    $scope.$on("$destroy", function (event) {
        $scope.eventSource.close();
    });
}

