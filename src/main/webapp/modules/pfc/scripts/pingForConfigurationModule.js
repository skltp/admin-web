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

angular.module('pingForConfigurationModule', []).
    controller('pingForConfigurationCtrl', ['$scope', '$http', '$location', '$interval', 'Restangular', function($scope, $http, $location, $interval, Restangular) {

    console.log("pingForConfigurationCtrl setup, v7");

    getStatusFromServer();

    // Wait for the initial call to complete until we perform the initialization based on the response
    var unbindWatcher = $scope.$watch(
        "pingForConfiguration", function() {

            console.log( "Watcher called." );
            init();

            console.log("Unregister watcher");
            unbindWatcher();
        }
    );


    function init() {
        var timerReload = $interval(function () {
                getStatusFromServer()
            },
                $scope.pingForConfiguration.pingIntervalSecs * 1000);

        var timerCountdown = $interval(function () {
                $scope.pfcReloadInSecs = $scope.pfcReloadInSecs - 1;
            },
            1000);

        // listen on DOM destroy (removal) event, and cancel the next UI update
        // to prevent updating time ofter the DOM element was removed.
        $scope.$on('$destroy', function () {

            $interval.cancel(timerReload);
            $interval.cancel(timerCountdown);
        });
    }

    function getStatusFromServer() {
        Restangular.one(getPath($location) + 'api/pingforconfigurationstats').get().then(function(pingForConfiguration) {

            for (var i = 0, l = pingForConfiguration.list.length; i < l; i++) {
                var pfc = pingForConfiguration.list[i];
                pfc.statusImage = getStatusImage(pfc.status);
            }

            $scope.pingForConfiguration = pingForConfiguration;
            var reloadSecs = pingForConfiguration.pingIntervalSecs;
            $scope.pfcReloadInSecs = reloadSecs;

        }, function() {
            console.log("There was an error getting the pingForConfiguration");
        });

    }

    function getStatusImage(state) {

        var image = "error";

        switch(state) {
            case "Ok":
                image = "ok";
                break;

            case "Failure":
                image = "error";
                break;

            case "Timeout":
                image = "warning";
                break;

            case "Unknown":
                image = "info";
                break;
        }
        return "modules/pfc/images/" + image + ".png";
    }

}]);




/*
TODO: ML Place in common JS-file, app.js?

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
*/