
angular.module('parcour', ['requestParcours']).
	config(function($routeProvider) {
		$routeProvider.
			when('/', {controller:IndexCtrl, templateUrl:'listLignes.html'}).
			when('/ligne/:lineId', {controller:ListParcoursCtrl, templateUrl:'listParcours.html'}).
			otherwise({redirectTo:'/'});
	});

function ListParcoursCtrl($scope, $routeParams, Parcour) {
	
	$scope.parcours = Parcour.query({lineId:$routeParams.lineId});
	
	$scope.updatePositionBus = function(lineId, macroDirection) {
		$.getJSON('/data/positionbus/' + lineId + '/' + macroDirection, function(data) {
			$.each(data, function(index, value){
				$('#' + lineId + '_' + macroDirection + '_' + value + '_bus').addClass('bus');
			});
		});
	}
}

function IndexCtrl($scope) {
	
	
}


angular.module('requestParcours', ['ngResource']).
	factory('Parcour', function($resource) {
		var Parcour = $resource('/data/parcours/:lineId');

		return Parcour;
	});
	
