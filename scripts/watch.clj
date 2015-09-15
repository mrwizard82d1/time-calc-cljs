(require '[cljs.build.api :as b])

(b/watch "src"
  {:main 'time-calc.core
   :output-to "out/time_calc.js"
   :output-dir "out"})
