(ns day4.part1
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (slurp "src/day4/input.txt"))
(def required-fields #{"byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid"})

(->> (str/split input #"\n\n")
     (map #(set (map second (re-seq #"(\w+):\S+" %))))
     (filter (partial set/subset? required-fields))
     (count))
