(ns day14.core
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [subsets]]
            [medley.core :refer [indexed]]))

(def input (slurp "src/day14/input.txt"))

;; Common
(defn apply-ops [ops n]
  (reduce (fn [n [i bit-fn]] (bit-fn n i)) n ops))

(defn parse-mask-line [line]
  (re-find #"\S{36}" line))

(defn parse-mem-line [line]
  (let [[_ addr n] (re-matches #"mem\[(\d+)\] = (\d+)" line)]
    [(Long. addr) (Long. n)]))

(defn solve-fn [get-mask-fn write-value-fn]
  (fn []
    (->> input
         (str/split-lines)
         (reduce (fn [{:keys [mask] :as state} line]
                   (if-let [mask-line (parse-mask-line line)]
                     (assoc state :mask (get-mask-fn mask-line))
                     (update state :mem write-value-fn mask (parse-mem-line line))))
                 {:mask [] :mem {}})
         :mem
         (map val)
         (apply +))))

;; Part 1
(def c->op {\0 bit-clear \1 bit-set})
(defn get-ops [mask-str]
  (->> mask-str
       (reverse)
       (map-indexed (fn [i c] (when (contains? c->op c) [i (c->op c)])))
       (remove nil?)))

(defn write-value [mem mask [addr n]]
  (assoc mem addr (apply-ops mask n)))

(def part1 (solve-fn get-ops write-value))

;; Part 2
(defn index-subset->ops [indices subset]
  (map #(if (contains? subset %) [% bit-set] [% bit-clear])
       indices))

(defn get-ops-2 [mask-str]
  (let [groups (->> mask-str (reverse) (indexed) (group-by second))
        fixed-indices (get groups \1)
        floating-indices (map first (get groups \X))]
    {:fixed-ops    (map (fn [[i _]] [i bit-set]) fixed-indices)
     :floating-ops (->> floating-indices (subsets) (map set) (map (partial index-subset->ops floating-indices)))}))

(defn get-addrs [floating-ops fixed-ops addr]
  (map (fn [ops] (apply-ops fixed-ops (apply-ops ops addr))) floating-ops))

(defn write-value-2 [mem {:keys [floating-ops fixed-ops]} [addr n]]
  (reduce #(assoc %1 %2 n)
          mem
          (get-addrs floating-ops fixed-ops addr)))

(def part2 (solve-fn get-ops-2 write-value-2))

;; Answers
(println (part1))
(println (part2))
