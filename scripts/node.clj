(require 'cljs.build.api)

;; Compile JavaScript from the files in the "src" directory targeted to run on node.js. Because of the way node.js
;; resolves references, we cannot put the output JavaScript file in the "out" directory.
(cljs.build.api/build "src"
                      {:main 'time-calc.core
                       :output-to "out/time_calc.js"
                       :output-dir "out"
                       :target :nodejs})

;; To utilize the source mapping support provided by node.js, remember to install the "source-map-support" package. One
;; invokes the command `npm install source-map-support`.
