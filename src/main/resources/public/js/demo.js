angular.module("app", [])
        .controller("Ctrl", Ctrl);

function Ctrl($scope, $http) {
    $scope.test = "Hello";
    $scope.blockchain = [];
    $scope.users = [];
    $scope.getBlockchain = function () {

        $http({
            method: 'GET',
            url: '/blockchain'
        }).then(function successCallback(response) {
            $scope.blockchain = response.data;

        }, function errorCallback(response) {
        });
    };
    $scope.getUsers = function () {

        $http({
            method: 'GET',
            url: '/getusers'
        }).then(function successCallback(response) {
            $scope.users = response.data;

        }, function errorCallback(response) {
        });
    };

}