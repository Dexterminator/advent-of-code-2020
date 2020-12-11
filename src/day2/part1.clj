(ns day2.part1)

(def input (slurp "src/day2/input.txt"))
(->> input
     (re-seq #"(\d+)-(\d+) (\w): (\w+)")
     (map (fn [[_ lo hi c pwd]]
            (<= (Integer. lo) (get (frequencies pwd) (first c) 0) (Integer. hi))))
     (count))
