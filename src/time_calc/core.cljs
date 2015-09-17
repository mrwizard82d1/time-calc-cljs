(ns time-calc.core
  (:require [cljs.nodejs :as nodejs]))

(def fs (js/require "fs"))

(nodejs/enable-util-print!)

(defn -main [& args]
  (.readFile fs "time.txt" (fn [err data]
                             (if err
                               (throw err)
                               (println (str data)))))
  ;; Note that this next line executes FIRST.
  (println "Hello, Node.js World! (Surprisingly prints first.)"))

(set! *main-cli-fn* -main)

