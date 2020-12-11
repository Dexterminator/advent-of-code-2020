(ns day4.part2
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (slurp "src/day4/input.txt"))

(def field->valid-fn {"byr" #(<= 1920 (Integer. %) 2002)
                      "iyr" #(<= 2010 (Integer. %) 2020)
                      "eyr" #(<= 2020 (Integer. %) 2030)
                      "hgt" #(let [[_ n unit] (re-matches #"([0-9]+)(cm|in)" %)
                                   n (try (Integer. n) (catch Exception _ 0))]
                               (case unit
                                 "cm" (<= 150 n 193)
                                 "in" (<= 59 n 76)
                                 false))
                      "hcl" #(re-matches #"#[0-9|a-f]{6}" %)
                      "ecl" #(re-matches #"amb|blu|brn|gry|grn|hzl|oth" %)
                      "pid" #(re-matches #"0*[0-9]{9}" %)
                      "cid" (constantly true)})

(def required-fields (disj (set (keys field->valid-fn)) "cid"))

(defn all-valid? [m]
  (every? (fn [[k v]]
            ((get field->valid-fn k) v))
          m))

(->> (str/split input #"\n\n")
     (map #(into {} (map (fn [[_ k v]] [k v]) (re-seq #"(\w+):(\S+)" %))))
     (filter #(set/subset? required-fields (set (keys %))))
     (filter all-valid?)
     (count))
