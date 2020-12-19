(ns day13.core
  (:require [medley.core :refer [indexed]]
            [util :refer [parse-long]]))

(def input (slurp "src/day13/input.txt"))

(defn part1 []
  (let [[ts & ids] (->> input (re-seq #"\d+") (map parse-long))]
    (->> ids
         (map (fn [id] [id (- id (rem ts id))]))
         (apply min-key second)
         (apply *))))

(defn abs [n]
  (if (neg? n) (- n) n))

(defn extended-gcd [a b]
  (cond (zero? a) [(abs b) 0 1]
        (zero? b) [(abs a) 1 0]
        :else (loop [s 0 s0 1 t 1 t0 0 r (abs b) r0 (abs a)]
                (if (zero? r)
                  [r0 s0 t0]
                  (let [q (quot r0 r)]
                    (recur (- s0 (* q s)) s
                           (- t0 (* q t)) t
                           (- r0 (* q r)) r))))))

(defn solve [n a]
  (let [prod (apply * n)
        reducer (fn [sum [ni ai]]
                  (let [p (quot prod ni)
                        [_ inv-p] (extended-gcd p ni)]
                    (+ sum (* ai inv-p p))))
        sum-prod (reduce reducer 0 (map vector n a))]
    (mod sum-prod prod)))

(defn part2 []
  (let [ids (->> input
                 (re-seq #"\d+|x")
                 (rest)
                 (indexed)
                 (remove #(= "x" (second %)))
                 (map (fn [[i n]]
                        (let [n (parse-long n)]
                          [n (- n i)]))))
        [n a] (apply map vector ids)]
    (solve n a)))

(println (part1))
(println (part2))
