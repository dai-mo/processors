module.exports = function(config){
  config.set({

    basePath : './',

    files : [
      '../src/main/webapp/app/bower_components/angular/angular.js',
      '../src/main/webapp/app/bower_components/angular-route/angular-route.js',
      '../src/main/webapp/app/bower_components/angular-mocks/angular-mocks.js',
      '../src/main/webapp/app/components/**/*.js',
      '../src/main/webapp/app/*.js',
      '../src/main/webapp/app/mobilise/**/*.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter'
            ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};
