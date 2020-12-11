(ns day7.part2
  (:require [clojure.string :as str]))

(def input (slurp "src/day7/input.txt"))

(def g (->> (str/split-lines input)
            (map (fn [row]
                   (when-not (str/includes? row "no other")
                     [(->> row (re-find #"(\S+ \S+) bag") (second))
                      (->> row
                           (re-seq #"(\d+) (\S+ \S+) bag?")
                           (map (fn [[_ n v]] {:n (Integer. n) :v v}))
                           (vec))])))
            (filter some?)
            (into {})))

(defn dfs [s]
  (loop [vertices [] stack [s]]
    (if (empty? stack)
      vertices
      (let [v (peek stack)
            neighbors (map #(update % :n * (:n v))
                           (get g (:v v)))]
        (recur
          (conj vertices v)
          (into (pop stack) neighbors))))))

(->> (dfs {:n 1 :v "shiny gold"})
     (map :n)
     (apply +)
     (dec))
