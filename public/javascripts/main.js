angular.module('squashlerApp', ['ngResource'])

    .controller('mainCtrl', function ($scope, GameGroups, Games) {

        $scope.settings = {
            group : undefined
        };

        $scope.gamegroups = [];
        $scope.games = [];

        GameGroups.query(function(response) {
            $scope.gamegroups = response;
        });

        $scope.$watch('settings.group', function() {
            if ($scope.settings.group) {
                Games.query({id: $scope.settings.group.id}, function(response) {
                    $scope.games = response;
                });
            }
        });



    })

    .factory('GameGroups', function ($resource) {
        return $resource('/rest/gamegroups', {})
    })

    .factory('Games', function ($resource) {
        return $resource('/rest/games/:id', {})
    });
