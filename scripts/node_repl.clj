(require 'cljs.repl)
(require 'cljs.build.api)
(require 'cljs.repl.node)

;; Compile all scripts in the "src" directory and write the result to "out/main.js."
(cljs.build.api/build "src"
                      {:main 'time-calc.core
                       :output-to "out/time_calc.js"
                       :output-dir "out"
                       :verbose true})

;; Invoke the node.js REPL.
(cljs.repl/repl (cljs.repl.node/repl-env)
                :watch "src"
                :output-dir "out")
