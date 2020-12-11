(ns day11.core
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [selections]]
            [medley.core :refer [find-first indexed]]))

;; Common
(def input (slurp "src/day11/input.txt"))
(def directions (->> (selections [identity dec inc] 2) (rest) (map vec) (vec)))

(defn get-next-state-fn [count-fn allowed-count]
  (fn [m] (vec (for [[y row] (indexed m)]
                 (vec (for [[x seat] (indexed row)]
                        (cond
                          (and (= \L seat) (zero? (count-fn m x y))) \#
                          (and (= \# seat) (>= (count-fn m x y) allowed-count)) \L
                          :else seat)))))))

(defn occupied-count [seats]
  (count (filter #(= \# %) seats)))

(defn solve [count-fn allowed-count]
  (->> input
       (str/split-lines)
       (iterate (get-next-state-fn count-fn allowed-count))
       (partition 2 1)
       (find-first (fn [[prev curr]] (= prev curr)))
       (first)
       (flatten)
       (occupied-count)))

;; Part 1
(defn adjacent-count [m x y]
  (->> directions
       (map (fn [[x-fn y-fn]] (-> m (get (y-fn y)) (get (x-fn x)))))
       (occupied-count)))

;; Part 2
(defn seen-count [m x y]
  (->> directions
       (map (fn [[x-fn y-fn]]
              (->> (iterate (fn [[x y]] [(x-fn x) (y-fn y)]) [x y])
                   (rest)
                   (map (fn [[x y]] (-> m (get y) (get x))))
                   (find-first #(not= \. %)))))
       (occupied-count)))

;; Answers
(println (solve adjacent-count 4))
(println (solve seen-count 5))
