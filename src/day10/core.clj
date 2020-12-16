(ns day10.core
  (:require [clojure.string :as str]))

(def input (slurp "src/day10/input.txt"))

(defn add-device [coll]
  (cons (+ 3 (apply max coll)) coll))

(def adapters (->> input
                   (str/split-lines)
                   (map #(Long. %))
                   (add-device)
                   (cons 0)
                   (sort)))

;; Part 1
(defn part1 []
  (let [freqs (->> adapters
                   (partition 2 1)
                   (map (fn [[x y]] (- y x)))
                   (frequencies))]
    (* (get freqs 1) (get freqs 3))))

;; Part 2
(defn part2 []
  (let [graph (->> adapters
                   (partition-all 4 1)
                   (map (fn [[x & xs]] [x (vec (remove #(< 3 (- % x)) xs))]))
                   (into {}))
        [goal & vs] (reverse adapters)]
    (loop [[v & vs] vs
           v->count {goal 1}]
      (let [n (apply + (map v->count (get graph v)))]
        (if (seq vs)
          (recur vs (assoc v->count v n))
          n)))))

(println (part1))
(println (part2))
