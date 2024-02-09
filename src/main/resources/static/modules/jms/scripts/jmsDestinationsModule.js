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

angular.module('jmsDestinationsModule', []).
	controller('jmsDestinationsCtrl', ['$scope', '$http', '$location', '$interval', 'Restangular', function($scope, $http, $location, $interval, Restangular) {

    console.log("jmsDestinationsCtrl setup, v1");

    getStatusFromServer();
    getAppversionFromServer();

    var reloadIntervalInSecs = 10;

    var timerReload = $interval(function () {
            getStatusFromServer()
        },
        reloadIntervalInSecs * 1000);

    var timerCountdown = $interval(function () {
            $scope.jmsReloadInSecs = $scope.jmsReloadInSecs - 1;
        },
        1000);

    // listen on DOM destroy (removal) event, and cancel the next UI update
    // to prevent updating time ofter the DOM element was removed.
    $scope.$on('$destroy', function () {

        $interval.cancel(timerReload);
        $interval.cancel(timerCountdown);
    });

    function getStatusFromServer() {
        Restangular.one(getPath($location) + 'api/jmsdestinationsstats').getList().then(function (jmsDestinations) {
        console.log("There was no error getting the destination");
            $scope.jmsDestinations = jmsDestinations;
            $scope.jmsReloadInSecs = reloadIntervalInSecs;
        }, function () {
            console.log("There was an error getting the destination");
        });
    }

    function getAppversionFromServer() {
            Restangular.one(getPath($location) + 'api/appVersion').get().then(function (appVersion) {
                $scope.appVersion = appVersion;
            }, function () {
                console.log("There was an error getting the app version");
            });
        }

  }]);