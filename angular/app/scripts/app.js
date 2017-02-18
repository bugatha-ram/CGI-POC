'use strict';

var cgiWebApp = angular.module('cgi-web-app', [ 'pascalprecht.translate','ngSessionStorage', 'ui.router', 'ngWebSocket', 'ngMessages' ]);

var HOST = 'localhost';

cgiWebApp.constant('urls', {
  // have to be change depending of the environment
  BASE: 'http://' + HOST + ':8080',
  HOSTNAME: HOST
}).config([ '$translateProvider', '$urlRouterProvider', '$stateProvider', function($translateProvider, $urlRouterProvider, $stateProvider) {

  $translateProvider.useStaticFilesLoader({
    prefix : 'language/locale-', // path to translations files
    suffix : '.json' // suffix, currently- extension of the translations
  });
  $translateProvider.preferredLanguage('en');

  //if no url is specified redirect to login
  $urlRouterProvider.otherwise('login');

  $stateProvider.state('login', {
    url : '/login',
    views:{
      //'header': {
      //},
      'pageContent':{
        templateUrl: '/views/login.html',
        controller: 'loginController'
      }/*,
      'footer':{
      }*/
  }
  }).state('registration', {
    url : '/registration',
    views:{
      //'header': {
      //},
      'pageContent':{
        templateUrl: '/views/registration.html',
        controller: 'registrationCtrl'
      }/*,
      'footer':{
      }*/
    }
  // controller: 'signupController'
  }).state('profile', {
    url : '/profile',
    views:{
      'header': {
        templateUrl: 'views/userHeader.html',
        controller: 'profileController'
      },
      'pageContent':{
        templateUrl: 'views/userProfile.html',
        controller: 'profileController'
      },
      'footer':{
        templateUrl: 'views/userFooter.html'
      }
    }
    //controller : 'customizationCtrl'
  }).state('restricted', {
    url : '/restricted',
    views:{
      //'header': {
      //},
      'pageContent':{
        templateUrl: 'views/restricted.html',
        controller: 'loginController'
      }/*,
      'footer':{
      }*/
    }
    // controller: 'RestrictedController'
  });
}]);

//get geo localization
//TODO verify if the user is sign in and if he accept the geoloclization
navigator.geolocation.getCurrentPosition(function(position) {
    
}
