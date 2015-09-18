(ns time-calc.core
  (:require [cljs.nodejs :as nodejs]))

(def fs (js/require "fs"))

(nodejs/enable-util-print!)

(defn make-tasks [text] [])

(defn summarize-tasks [tasks] {"mots" 3.75 "atrog" 4.25})

(defn report-summary [summary] (doseq [[k v] (sort (map identity summary))]
                                 (println k v)))

(defn -main [& args]
  (let [time-file-name (if args
                         (first args)
                         "time.txt")]
    (.readFile fs time-file-name (fn [error text]
                                   (if error
                                     (throw error)
                                     (-> text (make-tasks) (summarize-tasks) (report-summary)))))
    (println "Hello, Node.js World! (Surprisingly prints first.)")))

(set! *main-cli-fn* -main)

