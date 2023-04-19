(ns bindicator.core
  (:gen-class)
  (:require [clojure.java.shell :as sh]
            [cheshire.core :as json]))

(def active-days #{"SATURDAY" "SUNDAY" "MONDAY"})

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
      (and (= 12 (days-between now next-col))
           (= "WEDNESDAY" (day-of-week now)))
      (do (prn "veg box")
          (sh/sh "./scripts/veg-box.sh"))

      (not (active-days (day-of-week now)))
      (do (prn "no bins any time soon")
          (sh/sh "./scripts/no-bins.sh"))

      (< (days-between now next-col) 7)
      (do (prn "both bins")
          (if (= "MONDAY" (day-of-week now))
            (sh/sh "./scripts/both-bins-monday.sh")
            (sh/sh "./scripts/both-bins.sh")))

      :else
      (do (prn "just recycling")
          (if (= "MONDAY" (day-of-week now))
            (sh/sh "./scripts/just-recycling-monday.sh")
            (sh/sh "./scripts/just-recycling.sh")))))
  (System/exit 0))
