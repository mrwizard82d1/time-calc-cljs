(ns time-calc.core
  (:require [cljs.nodejs :as nodejs])
  (:use [clojure.string :only [split, split-lines]]))

(defrecord Task [start end details])

(defrecord TimePoint [hour min])

(defrecord TimeInterval [hours mins])

(def fs (js/require "fs"))

(nodejs/enable-util-print!)

(defn line->task
  "Convert a single line to a task."
  [a-line]
  (let [[start-text details] (clojure.string/split a-line #"[ \t]")
        start (TimePoint. (js/parseInt (subs start-text 0 2))
                          (js/parseInt (subs start-text 2)))
        end start]
    (Task. start end details)))

(defn zip
  "Equivalent of Python zip()."
  [& colls]
  (apply map vector colls))

(defn join-tasks
  "Creates a contiguous time range for tasks."
  [tasks]
  (let [zipped-tasks (zip tasks (drop 1 tasks))]
    (map #(Task. (:start (%1 0)) (:start (%1 1)) (:details (%1 0)))
         zipped-tasks)))

(defn lines->tasks
  "Convert a sequences of lines to tasks."
  [line-seq]
  (let [raw-tasks (map line->task line-seq)]
    (join-tasks raw-tasks)))

(defn extract-lines-from-text
  "Read all lines from some text."
  [text]
  (clojure.string/split-lines text))

(defn make-tasks [text] (lines->tasks (extract-lines-from-text text)))

(defn task-duration
  "Calcluate the duration of a task."
  [task]
  (let [{start-hours :hour start-mins :min} (:start task)
        {end-hours :hour end-mins :min} (:end task)]
    (if (< end-mins start-mins)
      (TimeInterval. (- (- end-hours 1) start-hours)
                     (- (+ end-mins 60) start-mins))
      (TimeInterval. (- end-hours start-hours)
                     (- end-mins start-mins)))))

(defn add-interval
  "Adds two TimeInteval instances."
  [interval-1 interval-2]
  (let [hours-sum (+ (:hours interval-1) (:hours interval-2))
        minutes-sum (+ (:mins interval-1) (:mins interval-2))]
    (if (> minutes-sum 59)
      (TimeInterval. (+ 1 hours-sum) (- minutes-sum 60))
      (TimeInterval. hours-sum minutes-sum))))

(defn between?
  "Is to-test in [lower, upper)?"
  [to-test lower upper]
  (and (>= to-test lower) (< to-test upper)))

(defn interval->decimal-interval
  "Converts a TimeInterval to a decimal value of hours."
  [{hours :hours mins :mins}]
  (cond (between? mins 0 8) hours
        (between? mins 8 23) (+ hours 0.25)
        (between? mins 23 38) (+ hours 0.5)
        (between? mins 39 54) (+ hours 0.75)
        (between? mins 53 60) (+ hours 1)))

(defn summarize-tasks
  "Summarize a sequence of tasks."
  [task-seq]
  (let [interval-map (reduce
                       #(assoc %1
                         (:details %2)
                         (add-interval (task-duration %2)
                                       (get %1 (:details %2)
                                            (TimeInterval. 0 0))))
                       {} task-seq)]
    (into {} (for [[d i] interval-map]
               [d (interval->decimal-interval i)]))))

;; (defn summarize-tasks [tasks] {"mots" 3.75 "atrog" 4.25})

(defn report-summary [summary]
  "Reports the summary of tasks."
  (doseq [[details duration]
          (sort-by first summary)]
    (println details (float duration))))

(defn -main [& args]
  (let [time-file-name (if args
                         (first args)
                         "time.txt")]
    (.readFile fs time-file-name (fn [error text]
                                   (if error
                                     (throw error)
                                     (-> text
                                         (make-tasks)
                                         (summarize-tasks)
                                         (report-summary)))))
    (println "Hello, Node.js World! (Surprisingly prints first.)")))

(set! *main-cli-fn* -main)

