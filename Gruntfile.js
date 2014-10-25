
module.exports = function(grunt) {

    var package = require('./package.json');

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        clean: {
            docs: {
                src: ["docs"]
            },
            buildid: {
                src: ["BUILD.id"]
            }
        },
        copy: {
            jekyll: {
                files: [
                    { expand: true, cwd: "bower_components/syfen-doc-js/jekyll/_includes", src: "*.html", dest: "docs/source/_includes" }
                ]
            },
            doccss: {
                files: [
                    { expand: true, cwd: "bower_components/syfen-doc-js/css", src: "*.*", dest: "docs/site/css" }
                ]
            },
            doclavaassets: {
                files: [
                    { expand: true, cwd: "bower_components/syfen-doc-js/doclava/assets", src: [ "*.*", "**/*.*" ], dest: "docs/source/doclava" }
                ]
            },
            javadocs: {
                files: [
                    { expand: true, cwd: "target/site/apidocs/docs", src: [ "*.*", "**/*.*" ], dest: "docs/source/project/version/api" }
                ]
            },
            readmecss: {
                files: [
                    { expand: true, cwd: "bower_components/github-markdown-css", src: "*.css", dest: "docs/site/css" }
                ]
            },
            readmetemplate: {
                files: [
                    { expand: true, cwd: "bower_components/syfen-doc-js/readme", src: "index.hbt", dest: "docs/tmp" }
                ]
            },
            readmefile: {
                files: [
                    { expand: true, cwd: "docs/tmp", src: "README.html", rename: function() { return "docs/source/project/version/index.html" } }
                ]
            }
        },
        'compile-handlebars': {
            readme: {
                template: 'docs/tmp/index.hbt',
                templateData: {
                    title: '<%= pkg.name %>',
                    version: '<%= pkg.version %>'
                },
                output: 'docs/tmp/index.jst'
            },
            navlocal: {
                template: 'bower_components/syfen-doc-js/nav/header.hbt',
                templateData: {
                    title: '<%= pkg.name %>',
                    version: '<%= pkg.version %>',
                    navItems: [
                        { label: 'Readme', url: '/project/version' },
                        { label: 'API', url: '/project/version/api' }
                    ]
                },
                output: 'docs/source/_includes/header.html'
            },
            nav: {
                template: 'bower_components/syfen-doc-js/nav/header.hbt',
                templateData: {
                    title: '<%= pkg.name %>',
                    version: '<%= pkg.version %>',
                    navItems: [
                        { label: 'Readme', url: '/meld/<%= pkg.version %>/' },
                        { label: 'API', url: '/meld/<%= pkg.version %>/api' }
                    ]
                },
                output: 'docs/source/_includes/header.html'
            }
        },
        markdown: {
            all: {
                files: [ {
                    expand: true,
                    src: 'README.md',
                    dest: 'docs/tmp',
                    ext: '.html'
                } ],
                options: {
                    template: 'docs/tmp/index.jst',
                    markdownOptions: {
                        gfm: true,
                        highlight: 'manual',
                        codelines: {
                            before: '<span>',
                            after: '</span>'
                        }
                    }
                }
            }
        },
        jekyll: {
            options: {
                src: 'docs/source/',
            },
            dist: {
                options: {
                    dest: 'docs/site',
                    raw: 'name: syfen-meld\n' +
                         'markdown: redcarpet\n' +
                         'highlighter: pygments\n' +
                         'permalink: :categories/:title.html\n' +
                         'version: "<%= pkg.version%>"',
                    layouts: 'bower_components/syfen-doc-js/jekyll/_layouts'
                }
            }
        }

    });

    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-compile-handlebars');
    grunt.loadNpmTasks('grunt-markdown');
    grunt.loadNpmTasks('grunt-jekyll');

    var _writeBuildInfo = function() {
        grunt.file.write("BUILD.id", package.version);
    };
    grunt.registerTask("writeBuildInfo", _writeBuildInfo);

    grunt.registerTask("init", function() {
        grunt.file.mkdir("docs");
    });

    grunt.registerTask('local', [ 'clean', 'init', 'writeBuildInfo', 'copy:doclavaassets', 'copy:javadocs', 'copy:readmetemplate', 'compile-handlebars:readme', 'markdown', 'copy:readmefile', 'copy:jekyll', 'compile-handlebars:navlocal', 'jekyll', 'copy:doccss', 'copy:readmecss' ]);

    grunt.registerTask('default', [ 'clean', 'init', 'writeBuildInfo', 'copy:doclavaassets', 'copy:javadocs', 'copy:readmetemplate', 'compile-handlebars:readme', 'markdown', 'copy:readmefile', 'copy:jekyll', 'compile-handlebars:nav', 'jekyll' ]);
}