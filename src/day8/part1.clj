(ns day8.part1)

(def input (slurp "src/day8/input.txt"))

(def program (->> (re-seq #"(\S+) ([\+|-]\d+)" input)
                  (map (fn [[_ op n]] [op (Integer. n)]))
                  (vec)))

(def op->fn
  {"nop" (fn [mem _] (update mem :i inc))
   "jmp" #(update %1 :i + %2)
   "acc" #(-> %1 (update :acc + %2) (update :i inc))})

(defn exec [{:keys [i seen acc] :as mem}]
  (let [[op n] (get program i)
        op-fn (op->fn op)]
    (if (contains? seen i)
      acc
      (recur (-> mem (update :seen conj i) (op-fn n))))))

(exec {:i 0 :acc 0 :seen #{}})
