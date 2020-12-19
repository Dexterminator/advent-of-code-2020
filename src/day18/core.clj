(ns day18.core
  (:require [instaparse.core :as insta]
            [util :refer [parse-long]]
            [clojure.string :as str]))

(def input (slurp "src/day18/input.txt"))

(def base
  "<string> = expr+
   <nested-expr> = <left> expr <right>
   left = '('
   right = ')'
   <ws> = #'[ \\t]+'
   mul = '*'
   add = '+'
   num = #'\\d+'")

(def parser1
  (insta/parser
    (str base
         "expr = val <ws> op <ws> val
         <val> = expr | nested-expr | num
         <op> = add | mul")))

(def parser2
  (insta/parser
    (str base
         "<expr> = add-expr | mul-expr
         add-expr = add-val <ws> add <ws> add-val
         mul-expr = mul-val <ws> mul <ws> mul-val
         <mul-val> = nested-expr | expr | num
         <add-val> = add-expr | nested-expr | num")))

(def lispify (comp (fn [[x y z]] (list y x z)) list))

(def transformations {:num      parse-long
                      :add      symbol
                      :mul      symbol
                      :add-expr lispify
                      :mul-expr lispify
                      :expr     lispify})

(defn solve [parser]
  (->> input
       (str/split-lines)
       (map parser)
       (map #(first (insta/transform transformations %)))
       (map eval)
       (apply +)))

(println (solve parser1))
(println (solve parser2))
