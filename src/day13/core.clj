(ns day13.core)

(def input (slurp "src/day13/input.txt"))
(def input "939\n7,13,x,x,59,x,31,19")
(def nums (->> input (re-seq #"\d+") (map #(Integer. %))))

(defn part1 []
  (let [[ts & ids] nums]
    (->> ids
         (map (fn [id] [id (- id (rem ts id))]))
         (apply min-key second)
         (apply *))))

(println (part1))


(defn part2 []
  (let [[_ & ids] nums]
    ids))

(println (part2))
