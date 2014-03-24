angular.module('squashlerApp', ['ngResource'])

    .controller('mainCtrl', function ($scope, GameGroup, Player) {

        $scope.settings = {
            group: undefined
        };

        $scope.gamegroups = GameGroup.query();
        $scope.players = Player.query();
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
            group.$save(function() { $scope.gamegroups = GameGroup.query(); });
        };

        $scope.deleteGroup = function(group) {
            group.$delete(function() { $scope.gamegroups = GameGroup.query(); });
        };

        $scope.createPlayer = function() {
            var player = new Player();
            player.name = $scope.newPlayerName;
            player.$save(function() {
                $scope.newPlayerName = null;
                $scope.players = Player.query();
            });
        };

        $scope.updatePlayer = function(player) {
            player.$save(function() { $scope.players = Player.query(); });
        };

        $scope.deletePlayer = function(player) {
            player.$delete(function() { $scope.players = Player.query(); });
        };

    })

    .factory('GameGroup', ['$resource', function ($resource) {
        return $resource("./rest/gamegroup/:id", { id: '@id' });
    }])

    .factory('Player', ['$resource', function ($resource) {
        return $resource("./rest/player/:id", { id: '@id' });
    }]);
