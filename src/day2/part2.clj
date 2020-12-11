(ns day2.part2)

(def input (slurp "src/day2/input.txt"))
(re-seq #"(\d+)-(\d+) (\w): (\w+)" input)

(->> input
     (re-seq #"(\d+)-(\d+) (\w): (\w+)")
     (filter (fn [[_ lo hi c pwd]]
               (let [v (vec pwd)
                     c (.charAt c 0)
                     lo? (= c (get v (dec (Integer. lo))))
                     hi? (= c (get v (dec (Integer. hi))))]
                 (or (and hi? (not lo?))
                     (and lo? (not hi?))))))
     (count))
