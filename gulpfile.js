var gulp    = require("gulp"),
    plugins = require("gulp-load-plugins")(),
    pkg     = require("./package.json"),
    paths   = {
        src: {
            css: "./src/main/resources/static/sass"
        },
        dist: {
            css: "./src/main/resources/static/css"
        }
    },
    config  = {
        autoprefixer: {
            browsers: ["last 2 versions", "> 1%"]
        }
    };

// Build the Sass
gulp.task("build:css", function() {
    return gulp.src(paths.src.css + "/**/*")
        // Prevents Sass from hanging on errors (syntax error, etc.)
        .pipe(plugins.plumber())
        .pipe(plugins.sourcemaps.init())
        // Need to use sass.sync() to work with Plumber correctly
        .pipe(plugins.sass.sync())
        .pipe(plugins.autoprefixer(config.autoprefixer))
        .pipe(plugins.sourcemaps.write("./"))
        .pipe(gulp.dest(paths.dist.css));
});

// Clean the Sass build directory
gulp.task("clean:css", function() {
    return gulp.src(paths.dist.css, { read: false })
        .pipe(plugins.clean());
})

gulp.task("build", ["build:css"]);

gulp.task("clean", ["clean:css"]);

gulp.task("watch", function() {
    gulp.watch(paths.src.css+ "/**/*", ["build:css"]);
});
