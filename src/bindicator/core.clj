(ns bindicator.core
  (:gen-class)
  (:require [clojure.java.shell :as sh]
            [cheshire.core :as json]))

(def active-days #{"FRIDAY" "SATURDAY" "SUNDAY" "MONDAY"})

(defn day-of-week
  [dt]
  (.toString (.getDayOfWeek dt)))

(defn now
  []
  (java.time.LocalDateTime/now))

(defn general-waste
  [data]
  (first (filter #(= "180L General Waste" (:containerName %)) data)))

(defn parse
  [output]
  (json/parse-string output keyword))

(defn next-collection-date
  []
  ;; Hopefully the subscription key we're using here stays valid
  (-> (sh/sh "./scripts/get-data.sh")
      :out
      parse
      :data
      general-waste
      :collection
      first
      :nextCollectionDate
      java.time.LocalDateTime/parse))

(defn days-between
  [now then]
  (.until now then java.time.temporal.ChronoUnit/DAYS))

(defn -main
  []
  (let [now (now)
        next-col (next-collection-date)]
    (cond
      (not (active-days (day-of-week now)))
      (do (prn "no bins any time soon")
          (sh/sh "./scripts/no-bins.sh"))

      (< (days-between now next-col) 7)
      (do (prn "both bins")
          (sh/sh "./scripts/both-bins.sh"))

      :else
      (do (prn "just recycling")
          (sh/sh "./scripts/just-recycling.sh"))))
  (System/exit 0))
