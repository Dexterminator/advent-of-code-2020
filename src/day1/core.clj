(ns day1.core
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [medley.core :as m]))

(def input (map edn/read-string (str/split-lines (slurp "src/day1/input.txt"))))
(first (for [x input y input z input :when (= (+ x y z) 2020)] (* x y z)))
