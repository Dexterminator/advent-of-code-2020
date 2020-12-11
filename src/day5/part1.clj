(ns day5.part1)

(def input (slurp "src/day5/input.txt"))
(def spec->fn {\B second \F first \L first \R second})

(defn find-n [spec n]
  (first (reduce (fn [seats curr-spec]
                   (let [new-seats (split-at (/ (count seats) 2) seats)]
                     ((spec->fn curr-spec) new-seats)))
                 (range n)
                 spec)))

(->> (re-seq #"(\S{7})(\S{3})" input)
     (map (fn [[_ row-spec col-spec]]
            (+ (* 8 (find-n row-spec 128))
               (find-n col-spec 8))))
     (apply max))
