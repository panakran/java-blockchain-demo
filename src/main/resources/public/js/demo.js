angular.module("app", [])
        .controller("Ctrl", Ctrl);

function Ctrl($scope){
    $scope.test="Hello";
}