angular.module('squashlerApp', ['ngResource'])

    .controller('mainCtrl', function ($scope, GameGroup) {

        $scope.settings = {
            group: undefined
        };

        $scope.gamegroups = GameGroup.query();
        $scope.players = [];
        $scope.games = [];

        $scope.createGroup = function() {
            var group = new GameGroup();
            group.name = $scope.newGroupName;
            group.$save(function() {
                $scope.newGroupName = null;
                $scope.gamegroups = GameGroup.query();
            });
        };

        $scope.updateGroup = function(group) {
            group.$save(function() {
                $scope.gamegroups = GameGroup.query();
            });
        };

        $scope.deleteGroup = function(group) {
            group.$delete(function() {
                $scope.gamegroups = GameGroup.query();
            });
        };

    })

    .factory('GameGroup', ['$resource', function ($resource) {
        return $resource("./rest/gamegroup/:id", { id: '@id' });
    }])

    .factory('Player', ['$resource', function ($resource) {
        return $resource("./rest/player/:id");
    }]);
