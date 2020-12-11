(ns day7.part1
  (:require [clojure.string :as str]))

(def input (slurp "src/day7/input.txt"))

(def g (->> (str/split-lines input)
            (map (fn [row]
                   (let [[x & xs] (map second (re-seq #"(\S+ \S+) bag" row))]
                     (when (not= ["no other"] xs)
                       [x (vec xs)]))))
            (filter some?)
            (into {})))

(defn dfs [s]
  (loop [stack [s] visited #{s}]
    (if (empty? stack)
      visited
      (let [neighbors (get g (peek stack))]
        (recur
          (into (pop stack) (remove visited neighbors))
          (into visited neighbors))))))

(->> (keys (dissoc g "shiny gold"))
     (map dfs)
     (filter #(contains? % "shiny gold"))
     (count))

(defn part2 []
  (let [graph (->> adapters
                   (partition-all 4 1)
                   (map (fn [[x & xs]] [x (vec (remove #(< 3 (- % x)) xs))]))
                   (into {}))
        vs (reverse adapters)
        goal (first vs)]
    (loop [[v & vs] (rest vs)
           v->count {goal 1}]
      (let [n (apply + (map v->count (get graph v)))]
        (if (seq vs)
          (recur vs (assoc v->count v n))
          n)))))
