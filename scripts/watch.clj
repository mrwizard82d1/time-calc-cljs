(require '[cljs.build.api])

(cljs.build.api/watch "src"
                      {:main 'time-calc.core
                       :output-to "out/time_calc.js"
                       :output-dir "out"
                       :target :nodejs})
