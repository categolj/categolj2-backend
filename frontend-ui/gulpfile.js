var gulp = require('gulp');
var concat = require('gulp-concat');
var flatten = require('gulp-flatten');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var imagemin = require('gulp-imagemin');
var sourcemaps = require('gulp-sourcemaps');
var minifycss = require('gulp-minify-css');
var ngAnnotate = require('gulp-ng-annotate');
var gulpFilter = require('gulp-filter');
var mainBowerFiles = require('main-bower-files');
var karma = require('gulp-karma');
var connect = require('gulp-connect');
var jshint = require('gulp-jshint');
var del = require('del');


var paths = {
    scripts: ['app/js/**/*.js'],
    unittest: ['test/unit/**/*.js'],
    html: ['app/**/*.html'],
    images: 'app/img/**/*',
    css: {
        files: ['app/css/*.css'],
        root: 'app/css'
    },
    bower_components: 'bower_components',
    target: 'dist'//'target/classes/META-INF/resources'
};

gulp.task('bower', function () {
    var jsFilter = gulpFilter('*.js');
    var cssFilter = gulpFilter('*.css');
    var fontFilter = gulpFilter(['*.eot', '*.woff', '*.svg', '*.ttf']);
    return gulp.src(mainBowerFiles())
        .pipe(jsFilter)
        .pipe(uglify())
        //.pipe(concat('vendor.js'))
        .pipe(gulp.dest(paths.target + '/js'))
        .pipe(jsFilter.restore())
        .pipe(cssFilter)
        .pipe(minifycss())
        //.pipe(concat('vendor.css'))
        .pipe(gulp.dest(paths.target + '/css'))
        .pipe(cssFilter.restore())
        .pipe(fontFilter)
        .pipe(flatten())
        .pipe(gulp.dest(paths.target + '/fonts'))
});

gulp.task('clean', function (cb) {
    del([paths.target], cb);
});

gulp.task('highlight', function () {
    return gulp.src('app/highlight/**/*')
        .pipe(gulp.dest(paths.target + '/highlight'));
});

gulp.task('scripts', function () {
    return gulp.src(paths.scripts)
        .pipe(ngAnnotate())
        // TODO sourcemap
        //.pipe(sourcemaps.init())
        .pipe(uglify({compress: {sequences: false, join_vars: false}}))
        .pipe(concat('app.js'))
        //.pipe(sourcemaps.write())
        .pipe(gulp.dest(paths.target + '/js'));
});

gulp.task('lint', function () {
    gulp.src(paths.scripts)
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

gulp.task('images', function () {
    return gulp.src(paths.images)
        .pipe(imagemin({optimizationLevel: 5}))
        .pipe(gulp.dest(paths.target + '/img'));
});

gulp.task('css', function () {
    return gulp.src(paths.css.files)
        .pipe(minifycss({root: paths.css.root, noRebase: true}))
        .pipe(gulp.dest(paths.target + '/css'));
});

gulp.task('html', function () {
    return gulp.src(paths.html)
        .pipe(gulp.dest(paths.target));
});

gulp.task('watch', function () {
    gulp.watch(paths.bower_components, ['bower']);
    gulp.watch(paths.scripts, ['scripts']);
    gulp.watch(paths.css.files, ['css']);
    gulp.watch(paths.html, ['html']);
    gulp.watch(paths.images, ['images']);
});

gulp.task('server', ['watch'], function () {
    connect.server({
        root: paths.target,
        port: 8888,
        livereload: true
    })
});

gulp.task('build', ['clean'], function () {
    gulp.start(['scripts', 'images', 'css', 'html', 'highlight', 'bower']);
});