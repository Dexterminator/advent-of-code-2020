(ns day15.core)

(def input [6 13 1 15 2 0])

(defn nth-number [number]
  (->> (range (count input) number)
       (reduce (fn [[prev-spoken spoken->turn] prev-turn]
                 [(if-let [pprev-turn (spoken->turn prev-spoken)] (- prev-turn pprev-turn) 0)
                  (assoc spoken->turn prev-spoken prev-turn)])
               [(last input) (into {} (map-indexed (fn [i n] [n (inc i)]) input))])
       (first)))

(println (nth-number 2020))
(println (nth-number 30000000))
