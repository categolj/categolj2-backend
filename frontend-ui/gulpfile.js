var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var del = require('del');
var minifycss = require('gulp-minify-css');
var connect = require('gulp-connect');
var uglify = require('gulp-uglify');
var buffer = require('vinyl-buffer');

var paths = {
    scripts: ['app/js/**/*.js'],
    html: ['app/**/*.html'],
    images: 'app/img/**/*',
    css: ['app/css/*.css'],
    target: 'dist'
};

gulp.task('clean', function (cb) {
    del([paths.target], cb);
});

gulp.task('highlight', function () {
    return gulp.src('app/highlight/**/*')
        .pipe(gulp.dest(paths.target + '/highlight'));
});

gulp.task('css', function () {
    return gulp.src(paths.css)
        .pipe(minifycss({root: paths.css, noRebase: true}))
        .pipe(gulp.dest(paths.target + '/css'));
});

gulp.task('html', function () {
    return gulp.src(paths.html)
        .pipe(gulp.dest(paths.target));
});

gulp.task('images', function () {
    return gulp.src(paths.images)
        .pipe(gulp.dest(paths.target + '/img'));
});

gulp.task('browserify', function () {
    return browserify('./app/js/index.js', {debug: true})
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(buffer())
        .pipe(uglify())
        .pipe(gulp.dest(paths.target + '/js'));
});

gulp.task('watch', function () {
    gulp.watch(paths.scripts, ['browserify']);
    gulp.watch(paths.css, ['css']);
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
    gulp.start(['browserify', 'images', 'css', 'html', 'highlight']);
});