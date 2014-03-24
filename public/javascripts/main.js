angular.module('squashlerApp', ['ngResource'])

    .controller('mainCtrl', function ($scope, GameGroup, Player, Game) {

        $scope.settings = {
            group: undefined
        };

        $scope.gamegroups = GameGroup.query();
        $scope.players = Player.query();
        $scope.games = [];


        $scope.$watch('settings.group', function() {
            if ($scope.settings.group) {
                $scope.games = Game.query({ gid: $scope.settings.group.id });
            }
        });


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

        $scope.createGame = function() {
            var game = new Game();
            game.date = $scope.newGameDate;
            game.gameGroupId = $scope.settings.group.id;
            game.playerLeft = $scope.newGamePlayerLeft;
            game.playerRight = $scope.newGamePlayerRight;
            game.pointsLeft = $scope.newGamePointsLeft;
            game.pointsRight = $scope.newGamePointsRight;
            game.$save(function() {
                $scope.newGamePlayerLeft = null;
                $scope.newGamePlayerRight = null;
                $scope.newGamePointsLeft = null;
                $scope.newGamePointsRight = null;
                $scope.games = Game.query({ gid: $scope.settings.group.id });
            });
        };

        $scope.updateGame = function(game) {
            game.$save(function() { $scope.games = Game.query(); });
        };

        $scope.deletePlayer = function(game) {
            game.$delete(function() { $scope.games = Game.query(); });
        };

    })

    .factory('GameGroup', ['$resource', function ($resource) {
        return $resource("./rest/gamegroup/:id", { id: '@id' });
    }])

    .factory('Player', ['$resource', function ($resource) {
        return $resource("./rest/player/:id", { id: '@id' });
    }])

    .factory('Game', ['$resource', function ($resource) {
        return $resource("./rest/gamegroup/:gid/game/:id", { gid: '@gameGroupId', id: '@id' });
    }]);
