(ns day5.part2
  (:require [medley.core :refer [find-first]]))

(def input (slurp "src/day5/input.txt"))
(def spec->fn {\B second \F first \L first \R second})

(defn find-n [spec n]
  (first (reduce (fn [seats curr-spec]
                   (let [new-seats (split-at (/ (count seats) 2) seats)]
                     ((spec->fn curr-spec) new-seats)))
                 (range n)
                 spec)))

(->> (re-seq #"(\S{7})(\S{3})" input)
     (map (fn [[_ row-spec col-spec]]
            row-spec
            (+ (* 8 (find-n row-spec 128))
               (find-n col-spec 8))))
     (sort)
     (partition 2 1)
     (find-first (fn [[x y]] (not= 1 (- y x))))
     ((comp inc first)))
