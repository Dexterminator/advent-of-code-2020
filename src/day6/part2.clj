(ns day6.part2
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(apply + (map #(->> % (str/split-lines) (map set) (apply set/intersection) (count))
              (str/split (slurp "src/day6/input.txt") #"\n\n")))
