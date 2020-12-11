(ns day8.part2)

(def input (slurp "src/day8/input.txt"))

(def program (->> (re-seq #"(\S+) ([\+|-]\d+)" input)
                  (map (fn [[_ op n]]
                         [op (Integer. n)]))
                  (vec)))
(def program-len (count program))

(def op->fn
  {"nop" (fn [mem _] (update mem :i inc))
   "jmp" #(update %1 :i + %2)
   "acc" #(-> %1 (update :acc + %2) (update :i inc))})

(defn exec [{:keys [i seen acc] :as mem} p]
  (let [[op n] (get p i)
        op-fn (op->fn op)]
    (cond
      (contains? seen i) nil
      (= i (dec program-len)) acc
      :else (recur (-> mem (update :seen conj i) (op-fn n)) p))))

(defn check [i]
  (let [[op _] (get program i)
        mut-program (assoc-in program [i 0] (case op
                                              "nop" "jmp"
                                              "jmp" "nop"
                                              "acc"))]
    (exec {:i 0 :acc 0 :seen #{}} mut-program)))

(some check (range (count program)))
