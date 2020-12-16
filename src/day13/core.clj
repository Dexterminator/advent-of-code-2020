(ns day13.core
  (:require [medley.core :refer [indexed find-first]]))

;(def input (slurp "src/day13/input.txt"))
;(def input "939\n7,13,x,x,59,x,31,19")
(def input "939\n17,x,13,19")
;(def input "939\n67,7,59,61")
;(def input "939\n67,x,7,59,61")
;(def input "939\n67,7,x,59,61")
;(def input "939\n1789,37,47,1889")
(def nums (->> input (re-seq #"\d+") (map #(Integer. %))))

(defn part1 []
  (let [[ts & ids] nums]
    (->> ids
         (map (fn [id] [id (- id (rem ts id))]))
         (apply min-key second)
         (apply *))))

(println (part1))

(defn ts-seq [ts]
  (map #(*' ts %) (range)))

(defn part2 []
  (let [[first-ts & ids] (->> input
                              (re-seq #"\d+|x")
                              (rest)
                              (indexed)
                              (remove #(= "x" (second %)))
                              (map (fn [[i n]] [i (Integer. n)]))
                              (sort-by second >))]
    (find-first
      #(every? (fn [[i n]] (= i (- n (mod % n)))) ids)
      (ts-seq first-ts))))

(println (part2))
