(ns day12.core)

(def input "F10\nN3\nF7\nR90\nF11")
(def input (slurp "src/day12/input.txt"))
(def instructions (->> input (re-seq #"(\w)(\d+)") (map (fn [[_ c n]] [c (Integer. n)]))))

(def char->deg {"E" 0 "N" 90 "W" 180 "S" 270})
(def deg->update {0 [+ :x] 90 [+ :y] 180 [- :x] 270 [- :y]})
(def rot-dir->fn {"R" - "L" +})
(def rots (set (keys rot-dir->fn)))
(def nesw (set (keys char->deg)))

(defn update-rotation [rot n rot-dir]
  (mod ((rot-dir->fn rot-dir) rot n) 360))

(defn update-pos [deg pos n]
  (let [[update-fn axis] (deg->update deg)]
    (update pos axis update-fn n)))

(defn navigate [pos rot [[c n] & is]]
  (if (some? c)
    (cond
      (nesw c) (recur (update-pos (char->deg c) pos n) rot is)
      (rots c) (recur pos (update-rotation rot n c) is)
      (= c "F") (recur (update-pos rot pos n) rot is))
    (+ (Math/abs (:x pos)) (Math/abs (:y pos)))))

(println (navigate {:x 0 :y 0} 0 instructions))
