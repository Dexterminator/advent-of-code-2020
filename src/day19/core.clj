(ns day19.core
  (:require [instaparse.core :as insta]
            [clojure.string :as str]))

(def input (str/split (slurp "src/day19/input.txt") #"\n\n"))

(defn solve [[rules msgs-str]]
  (let [parser (insta/parser rules :start :0)
        match? #(not (insta/failure? (insta/parse parser %)))]
    (->> msgs-str (str/split-lines) (filter match?) (count))))

(defn part1 []
  (solve input))

(defn part2 []
  (let [[rules msgs-str] input]
    (solve [(-> rules
                (str/replace "8: 42" "8: 42 | 42 8")
                (str/replace "11: 42 31" "11: 42 31 | 42 11 31"))
            msgs-str])))

(println (part1))
(println (part2))
