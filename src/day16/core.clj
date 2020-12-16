(ns day16.core
  (:require [clojure.string :as str]
            [medley.core :refer [indexed]]
            [clojure.set :as set]))

(def input (slurp "src/day16/input.txt"))

;; Common
(defn parse-tickets [s]
  (->> s (str/split-lines) (rest)
       (map (fn [row] (map #(Long. %) (re-seq #"\d+" row))))))

(defn parse-input []
  (let [[rules my-ticket nearby-tickets] (str/split input #"\n\n")
        rules (->> rules
                   (re-seq #"(.+): (\d+)-(\d+) or (\d+)-(\d+)")
                   (map rest)
                   (map (fn [[field & nums]] [field (map #(Long. %) nums)]))
                   (into {}))
        my-ticket (vec (first (parse-tickets my-ticket)))
        nearby-tickets (parse-tickets nearby-tickets)]
    [rules my-ticket nearby-tickets]))

(defn matches-rule? [[_field [lo1 hi1 lo2 hi2]] n]
  (or (<= lo1 n hi1) (<= lo2 n hi2)))

(defn matches-some-rule? [rules n]
  (some #(matches-rule? % n) rules))

;; Part 1
(defn part1 [rules tickets]
  (->> tickets
       (mapcat (fn [ticket-vals]
                 (remove #(matches-some-rule? rules %) ticket-vals)))
       (apply +)))

;; Part 2
(defn matching-rule-fields [rules column]
  (->> rules
       (filter (fn [rule]
                 (every? #(matches-rule? rule %) column)))
       (map first)
       (set)))

(defn part2 [rules my-ticket tickets]
  (->> tickets
       (filter (fn [ticket-vals]
                 (every? #(matches-some-rule? rules %) ticket-vals)))
       (apply map vector)
       (map (fn [column] (matching-rule-fields rules column)))
       (cons #{})
       (indexed)
       (sort-by (comp count second))
       (partition 2 1)
       (map (fn [[[_i1 v1] [i2 v2]]]
              [(dec i2) (first (set/difference v2 v1))]))
       (filter #(str/starts-with? (second %) "departure"))
       (map first)
       (map #(get my-ticket %))
       (apply *)))

;; Answers
(defn solve []
  (let [[rules my-ticket tickets] (parse-input)]
    (println (part1 rules tickets))
    (println (part2 rules my-ticket tickets))))

(solve)
