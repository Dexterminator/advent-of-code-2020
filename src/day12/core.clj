(ns day12.core
  (:require [clojure.core.matrix :refer [mmul]]))

(def input (slurp "src/day12/input.txt"))

;; Common
(def instructions (->> input (re-seq #"(\w)(\d+)") (map (fn [[_ c n]] [(first c) (Integer. n)]))))
(def char->deg {\E 0 \N 90 \W 180 \S 270})
(def deg->update {0 [+ 0] 90 [+ 1] 180 [- 0] 270 [- 1]})
(def rot-dir->fn {\R - \L +})
(def rots (set (keys rot-dir->fn)))
(def nesw (set (keys char->deg)))

(defn update-pos [deg pos n]
  (let [[update-fn axis] (deg->update deg)]
    (update pos axis update-fn n)))

;; Part1
(defn update-rotation [rot n rot-dir]
  (mod ((rot-dir->fn rot-dir) rot n) 360))

(defn part1 [[x y :as pos] rot [[c n] & is]]
  (if (some? c)
    (cond
      (nesw c) (recur (update-pos (char->deg c) pos n) rot is)
      (rots c) (recur pos (update-rotation rot n c) is)
      (= c \F) (recur (update-pos rot pos n) rot is))
    (+ (Math/abs x) (Math/abs y))))

;; Part2
(def deg->matrix {90 [[0 -1] [1 0]] 180 [[-1 0] [0 -1]] 270 [[0 1] [-1 0]]})

(defn move-to-waypoint [[sx sy] [wx wy] n]
  [(+ sx (* wx n)) (+ sy (* wy n))])

(defn rotate-waypoint [dir n waypoint-pos]
  (let [deg (mod ((rot-dir->fn dir) n) 360)
        matrix (deg->matrix deg)]
    (mmul matrix waypoint-pos)))

(defn part2 [[sx sy :as ship-pos] waypoint-pos [[c n] & is]]
  (if (some? c)
    (cond
      (nesw c) (recur ship-pos (update-pos (char->deg c) waypoint-pos n) is)
      (rots c) (recur ship-pos (rotate-waypoint c n waypoint-pos) is)
      (= c \F) (recur (move-to-waypoint ship-pos waypoint-pos n) waypoint-pos is))
    (Math/round (+ (Math/abs sx) (Math/abs sy)))))

;; Answers
(println (part1 [0 0] 0 instructions))
(println (part2 [0 0] [10 1] instructions))
