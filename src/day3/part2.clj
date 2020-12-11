(ns day3.part2
  (:require [clojure.string :as str]))

(def input (vec (map vec (str/split-lines (slurp "src/day3/input.txt")))))
(def row-len (count (get input 0)))
(def row-count (count input))
(def slopes [[1 1] [3 1] [5 1] [7 1] [1 2]])

(defn trees [input n row col col-step row-step]
  (let [c (-> input (get row) (get col))
        n (cond-> n (= c \#) inc)
        row (+ row row-step)
        col (mod (+ col col-step) row-len)]
    (if (>= row row-count)
      n
      (recur input n row col col-step row-step))))

(reduce (fn [prod [col-step row-step]]
          (* prod (trees input 0 0 0 col-step row-step)))
        1
        slopes)
