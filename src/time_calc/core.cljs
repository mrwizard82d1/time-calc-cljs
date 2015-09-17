(ns time-calc.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(defn -main [& args]
  (println "Hello, Node.js World!"))

(set! *main-cli-fn* -main)

