var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var reactify = require('reactify');

gulp.task('browserify', function () {
    return browserify('./app/index.jsx', {
        debug: true,
        transform: [reactify]
    })
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(gulp.dest('./dest'));
});

gulp.task('html', function () {
    return gulp.src('./assets/**/*.html')
        .pipe(gulp.dest('./dest'));
});

gulp.task('build', function () {
    gulp.start(['browserify', 'html']);
});

gulp.task('watch', ['build'], function () {
    gulp.watch(['./app/**/*.js*'], ['browserify']);
    gulp.watch(['./assets/**/*.html'], ['html']);
});

