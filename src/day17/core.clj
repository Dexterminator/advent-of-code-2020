(ns day17.core
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [selections]]
            [medley.core :refer [indexed]]))

(def input (slurp "src/day17/input.txt"))

(def directions
  (memoize
    (fn [dims]
      (->> (selections [identity dec inc] dims) (rest) (map vec) (vec)))))

(def get-adjacent-coords
  (memoize
    (fn [dims coord]
      (map (fn [direction]
             (mapv (fn [f val] (f val)) direction coord))
           (directions dims)))))

(defn get-adjacent-active-count [dims m coord]
  (->> (get-adjacent-coords dims coord)
       (map #(get m %))
       (filter #(= \# %))
       (count)))

(defn initial-state [dims]
  (->> (indexed (str/split-lines input))
       (mapcat (fn [[y row]]
                 (map (fn [[x cube]] [(if (= dims 3) [x y 0] [x y 0 0]) cube])
                      (indexed row))))
       (into {})))

(defn get-indices [dims m]
  (->> m
       (map key)
       (mapcat #(get-adjacent-coords dims %))
       (distinct)))

(defn next-state [dims m]
  (->> (get-indices dims m)
       (map (fn [coord]
              (let [active? (= (get m coord) \#)
                    adjacent-active-count (get-adjacent-active-count dims m coord)]
                (when (or (and active? (#{2 3} adjacent-active-count))
                          (and (not active?) (= 3 adjacent-active-count)))
                  [coord \#]))))
       (remove nil?)
       (into {})))

(defn active-count [m]
  (->> m
       (vals)
       (filter #(= \# %))
       (count)))

(defn solve [dims]
  (active-count (nth (iterate (partial next-state dims) (initial-state dims)) 6)))

;; Answers
(println (solve 3))
(println (solve 4))
