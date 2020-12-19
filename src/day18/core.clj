(ns day18.core
  (:require [instaparse.core :as insta]
            [util :refer [parse-long]]
            [clojure.string :as str]))

(def input (slurp "src/day18/input.txt"))

(def parser
  (insta/parser
    "
    <string> = expr+
    expr = val <ws> op <ws> val
    <val> = expr | nested-expr | num
    <nested-expr> = <left> expr <right>
    left = '('
    right = ')'
    <ws> = #'[ \\t]+'
    num = #'\\d+'
    op = '*' | '+'
    "))

(def transformations {:num  parse-long
                      :op   symbol
                      :expr (comp (fn [[x y z]] (list y x z)) list)})

(defn part1 []
  (->> input
       (str/split-lines)
       (map parser)
       (map #(first (insta/transform transformations %)))
       (map eval)
       (apply +)))

(println (part1))
