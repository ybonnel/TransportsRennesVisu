
angular.module('parcour', ['requestParcours', 'requestLignes']).
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

function IndexCtrl($scope, Lignes) {
	$scope.lignes = Lignes.query();
	
}


angular.module('requestParcours', ['ngResource']).
	factory('Parcour', function($resource) {
		var Parcour = $resource('/data/parcours/:lineId');

		return Parcour;
	});

angular.module('requestLignes', ['ngResource']).
	factory('Lignes', function($resource) {
		var Lignes = $resource('/data/lignes');

		return Lignes;
	});
	
