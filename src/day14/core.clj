(ns day14.core
  (:require [clojure.string :as str]))

(def input (slurp "src/day14/input.txt"))
;(def input "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\nmem[8] = 11\nmem[7] = 101\nmem[8] = 0")

(defn get-mask [line]
  (->> line
       (re-find #"\S{36}")
       (reverse)
       (map-indexed (fn [i c]
                      (case c
                        \0 [i bit-clear]
                        \1 [i bit-set]
                        nil)))
       (remove nil?)))

(defn write-value [mem line mask]
  (let [[_ addr n] (re-matches #"mem\[(\d+)\] = (\d+)" line)]
    (assoc mem addr (reduce (fn [n [i bit-fn]] (bit-fn n i)) (Integer. n) mask))))

(defn part1 []
  (->> input
       (str/split-lines)
       (reduce (fn [{:keys [mask] :as state} line]
                 (if (str/starts-with? line "ma")
                   (assoc state :mask (get-mask line))
                   (update state :mem write-value line mask)))
               {:mask []
                :mem  {}})
       :mem
       (map val)
       (apply +)))

(println (part1))

