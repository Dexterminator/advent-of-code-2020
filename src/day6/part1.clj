(ns day6.part1
  (:require [clojure.string :as str]))

(def input (slurp "src/day6/input.txt"))

(->> (str/split input #"\n\n")
     (map #(count (distinct (re-seq #"\S" %))))
     (apply +))
