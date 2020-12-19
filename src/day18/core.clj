(ns day18.core
  (:require [instaparse.core :as insta]
            [util :refer [parse-long]]
            [clojure.string :as str]))

(def input "1 + 2 * 3 + 4 * 5 + 6")
;(def input "1 + (2 * 3) + (4 * (5 + 6))")
;(def input "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")
(def input "2 * 3 + (4 * 5)")
(def input "5 + (8 * 3 + 9 + 3 * 4 * 3)")
(def input "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")
(def input "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")
(def input (slurp "src/day18/input.txt"))

;2 * 3 + (4 * 5)
(* 2 (+ 3 (* 4 5)))

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

(def parser2
  (insta/parser
    "
    <string> = expr+
    <expr> = mul-expr | nested-expr | add-expr
    add-expr = add-val <ws> add <ws> add-val
    mul-expr = mul-val <ws> mul <ws> mul-val
    add = '+'
    mul = '*'
    <mul-val> = nested-expr | expr | num
    <add-val> = add-expr | nested-expr | num
    <nested-expr> = <left> expr <right>
    left = '('
    right = ')'
    <ws> = #'[ \\t]+'
    num = #'\\d+'
    "))

(def transformations {:num      parse-long
                      :op       symbol
                      :add      symbol
                      :mul      symbol
                      :add-expr (comp (fn [[x y z]] (list y x z)) list)
                      :mul-expr (comp (fn [[x y z]] (list y x z)) list)
                      :expr     (comp (fn [[x y z]] (list y x z)) list)})

(defn part1 []
  (->> input
       (str/split-lines)
       (map parser2)
       (map #(first (insta/transform transformations %)))
       (map eval)
       (apply +)))

(println #p (part1))
