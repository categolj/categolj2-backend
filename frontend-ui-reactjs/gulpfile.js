var browserify = require('browserify');
var gulp = require('gulp');
var source = require('vinyl-source-stream');
var reactify = require('reactify');
var buffer = require('vinyl-buffer');
var uglify = require('gulp-uglify');

gulp.task('browserify', function () {
    return browserify('./app/index.jsx', {
        debug: true,
        transform: [reactify]
    })
        .bundle()
        .pipe(source('bundle.js'))
        .pipe(buffer())
        .pipe(uglify())
        .pipe(gulp.dest('./dest'));
});

gulp.task('html', function () {
    return gulp.src('./assets/**/*')
        .pipe(gulp.dest('./dest'));
});

gulp.task('build', function () {
    gulp.start(['browserify', 'html']);
});

gulp.task('watch', ['build'], function () {
    gulp.watch(['./app/**/*.js*'], ['browserify']);
    gulp.watch(['./assets/**/*.html'], ['html']);
});

