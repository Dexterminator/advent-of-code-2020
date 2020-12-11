(ns day9.core
  (:require [clojure.string :as str]
            [medley.core :refer [queue find-first]]
            [clojure.edn :as edn]))

(def input (->> (slurp "src/day9/input.txt")
                (str/split-lines)
                (map edn/read-string)))

(defn valid? [[q nums]]
  (let [n (peek nums)]
    (find-first some? (for [x q y q :when (and (not= x y) (= (+ x y) n))] :valid))))

(defn next-step [[q nums]]
  [(conj (pop q) (peek nums))
   (pop nums)])

(defn part1 []
  (->> (iterate next-step (map queue (split-at 25 input)))
       (find-first (complement valid?))
       (second)
       (peek)))

(defn part2 [goal]
  (->> input
       (filter #(< % goal))
       (iterate rest)
       (mapcat #(map (fn [x] (take (inc x) %)) (range (count %))))
       (find-first #(= goal (apply + %)))
       ((juxt (partial apply min) (partial apply max)))
       (apply +)))

(let [goal (part1)]
  (println goal)
  (println (part2 goal)))
