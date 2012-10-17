
angular.module('parcour', ['requestParcours']).
	config(function($routeProvider) {
		$routeProvider.
			when('/', {controller:ListParcoursCtrl, templateUrl:'listParcours.html'}).
			otherwise({redirectTo:'/'});
	});

function ListParcoursCtrl($scope, Parcour) {
	$scope.parcours = Parcour.query();
}


angular.module('requestParcours', ['ngResource']).
	factory('Parcour', function($resource) {
		var Parcour = $resource('/visu/data/0003');

		return Parcour;
	});
