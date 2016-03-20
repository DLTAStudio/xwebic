/**
 * Created by dscreve on 25/01/2016.
 */
// Gruntfile.js

// our wrapper function (required by grunt and its plugins)
// all configuration goes inside this function
module.exports = function(grunt) {

    // ===========================================================================
    // CONFIGURE GRUNT ===========================================================
    // ===========================================================================
    grunt.initConfig({

        // get the configuration info from package.json ----------------------------
        // this way we can use things like name and version (pkg.name)
        pkg: grunt.file.readJSON('package.json'),


        bowercopy: {
            options: {
                // Bower components folder will be removed afterwards
                srcPrefix:"app/bower_components",
                clean: true
            },
            debug: {
                options: {
                    destPrefix: "dist/debug/bower_components"
                },
                files: {
                    'angular/angular.js': 'angular/angular.js',
                    'angular-route/angular-route.js':'angular-route/angular-route.js',
                    'jquery/dist/jquery.js':'jquery/dist/jquery.js',
                    'angular-messages/angular-messages.js':'angular-messages/angular-messages.js',
                    'bootstrap/dist/css/bootstrap.css':'bootstrap/dist/css/bootstrap.css',
                    'bootstrap/dist/css/bootstrap-theme.css':'bootstrap/dist/css/bootstrap-theme.css',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.eot':'bootstrap/dist/fonts/glyphicons-halflings-regular.eot',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.woff':'bootstrap/dist/fonts/glyphicons-halflings-regular.woff',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.svg':'bootstrap/dist/fonts/glyphicons-halflings-regular.svg',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.woff2':'bootstrap/dist/fonts/glyphicons-halflings-regular.woff2',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.ttf':'bootstrap/dist/fonts/glyphicons-halflings-regular.ttf',
                    'bootstrap/dist/js/bootstrap.js':'bootstrap/dist/js/bootstrap.js',
                    'angular-bootstrap/ui-bootstrap-tpls.js':'angular-bootstrap/ui-bootstrap-tpls.js',
                    'angular-bootstrap/ui-bootstrap.js':'angular-bootstrap/ui-bootstrap.js',
                    'angular-loading-bar/build/loading-bar.js':'angular-loading-bar/build/loading-bar.js',
                    'angular-loading-bar/build/loading-bar.css':'angular-loading-bar/build/loading-bar.css',
                    'moment/moment.js':'moment/moment.js',
                    'moment-duration-format/lib/moment-duration-format.js':'moment-duration-format/lib/moment-duration-format.js',
                    'angular-translate/angular-translate.js':'angular-translate/angular-translate.js',
                    'angular-translate-loader-static-files/angular-translate-loader-static-files.js':'angular-translate-loader-static-files/angular-translate-loader-static-files.js',
                    'Chart.js/Chart.js':'Chart.js/Chart.js',
                    'angular-chart.js/dist/angular-chart.js':'angular-chart.js/dist/angular-chart.js',
                    'angular-chart.js/dist/angular-chart.css':'angular-chart.js/dist/angular-chart.css',
                }
            },
            release: {
                options: {
                    destPrefix: "dist/release/bower_components"
                },
                files: {

                    'angular/angular.js': 'angular/angular.min.js',
                    'angular-route/angular-route.js':'angular-route/angular-route.min.js',
                     'jquery/dist/jquery.js':'jquery/dist/jquery.min.js',
                    'angular-messages/angular-messages.js':'angular-messages/angular-messages.min.js',
                    'bootstrap/dist/css/bootstrap.css':'bootstrap/dist/css/bootstrap.min.css',
                    'bootstrap/dist/css/bootstrap-theme.css':'bootstrap/dist/css/bootstrap-theme.min.css',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.eot':'bootstrap/dist/fonts/glyphicons-halflings-regular.eot',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.woff':'bootstrap/dist/fonts/glyphicons-halflings-regular.woff',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.svg':'bootstrap/dist/fonts/glyphicons-halflings-regular.svg',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.woff2':'bootstrap/dist/fonts/glyphicons-halflings-regular.woff2',
                    'bootstrap/dist/fonts/glyphicons-halflings-regular.ttf':'bootstrap/dist/fonts/glyphicons-halflings-regular.ttf',
                    'bootstrap/dist/js/bootstrap.js':'bootstrap/dist/js/bootstrap.min.js',
                    'angular-bootstrap/ui-bootstrap-tpls.js':'angular-bootstrap/ui-bootstrap-tpls.min.js',
                    'angular-bootstrap/ui-bootstrap.js':'angular-bootstrap/ui-bootstrap.min.js',
                    'angular-loading-bar/build/loading-bar.js':'angular-loading-bar/build/loading-bar.min.js',
                    'angular-loading-bar/build/loading-bar.css':'angular-loading-bar/build/loading-bar.min.css',
                    'moment/moment.js':'moment/min/moment.min.js',
                    'moment-duration-format/lib/moment-duration-format.js':'moment-duration-format/lib/moment-duration-format.js',
                    'angular-translate/angular-translate.js':'angular-translate/angular-translate.min.js',
                    'angular-translate-loader-static-files/angular-translate-loader-static-files.js':'angular-translate-loader-static-files/angular-translate-loader-static-files.min.js',
                    'Chart.js/Chart.js':'Chart.js/Chart.min.js',
                    'angular-chart.js/dist/angular-chart.js':'angular-chart.js/dist/angular-chart.min.js',
                    'angular-chart.js/dist/angular-chart.css':'angular-chart.js/dist/angular-chart.min.css',


                }
            }
        },

        clean: {
            debug: ["dist/debug"],
            release: ["dist/release"],
        },
        copy: {
            debug: {
                files:[
                { expand: true,
                    cwd: 'app/',
                    src: ['js/**','css/**','images/**','locales/**','views/**','modules/**','app.js','index.html',
                        'fonts/**','components/**','environnement/shared_module.js'],
                    dest: 'dist/debug',
                    nonull: true }
                ]
            },
            release: {
                files:[
                    { expand: true,
                        cwd: 'app/',
                        src: ['js/**','css/**','images/**','locales/**','views/**','index.html',
                            'fonts/**'],
                        dest: 'dist/release',
                        nonull: true },
                    { expand: true,
                        cwd: 'app/environnement/release',
                        src: ['shared_module.js'],
                        dest: 'dist/release/environnement',
                        nonull: true },

                ]
            }
        },

        uglify: {
            release: {
                files:[
                    { expand: true,
                        cwd: 'app/',
                        src: ['app.js','modules/*.js','components/version/*.js'],
                        dest: 'dist/release',
                        nonull: true }
                ]
            },
        }

    });

    // ===========================================================================
    // LOAD GRUNT PLUGINS ========================================================
    // ===========================================================================
    // we can only load these if they are in our package.json
    // make sure you have run npm install so our app can find these

  //  grunt.loadNpmTasks('grunt-contrib-jshint');
  //  grunt.loadNpmTasks('grunt-contrib-less');
  //  grunt.loadNpmTasks('grunt-contrib-cssmin');
  //  grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-bowercopy');


    // this task will only run the dev configuration
    grunt.registerTask('debug', ['clean:debug','copy:debug','bowercopy:debug']);

    // only run production configuration
    grunt.registerTask('release', ['clean:release','copy:release','bowercopy:release','uglify:release']);


    // all of our configuration will go here
    // this default task will go through all configuration (dev and production) in each task
    grunt.registerTask('default', ['release','debug']);

};