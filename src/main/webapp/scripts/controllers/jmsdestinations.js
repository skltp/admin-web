/*
 * Copyright (c) 2014 Center for eHalsa i samverkan (CeHis).
 * 								<http://cehis.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
'use strict';

angular.module('jmsDestinationsControllers', []).
	controller('jmsDestinationsCtrl', ['$scope', '$http', '$location', 'Restangular', function($scope, $http, $location, Restangular) {
		 Restangular.one(getPath($location) + 'api/jmsdestinationsstats').getList().then(function(jmsDestinations) {
	      $scope.jmsDestinations = jmsDestinations;
	    }, function() {
	      console.log("There was an error getting the destination");
	    });
  }]);


function getPath(location) {
	var path = "";
	console.log("Host is " + location.host());
	
	//Strip the path of the hash and everything that follows it
	path = location.absUrl().substr(0, location.absUrl().lastIndexOf("#"));
	path = path.substr(0).split('/');
	var noBlankStrings = _.filter(path, function(s) { return s !== ""; }); //Using lodash to filter out emtpy strings
	var lastNonEmptyElement = noBlankStrings[noBlankStrings.length - 1];
	
	if(_.contains(lastNonEmptyElement, location.host())) {
		console.log("Setting path to blank");
		path = "";
	} else {
		path = lastNonEmptyElement + "/";
		console.log("Setting path to: " + path);
	}
	
	return path;
}