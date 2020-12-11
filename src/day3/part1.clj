(ns day3.part1
  (:require [clojure.string :as str]))

(def input (vec (map vec (str/split-lines (slurp "src/day3/input.txt")))))
(def row-len (count (get input 0)))
(def row-count (count input))

(defn trees [input n row col]
  (let [c (-> input (get row) (get col))
        n (cond-> n (= c \#) inc)
        row (inc row)
        col (mod (+ col 3) row-len)]
    (if (= row row-count)
      n
      (recur input n row col))))

(trees input 0 0 0)
