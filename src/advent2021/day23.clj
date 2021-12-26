(ns advent2021.day23
  (:require [advent-utils.core :as u]))

;; Position labeling scheme
;; "#############"
;; "#01 2 3 4 56#"
;; "###7#9#b#d###"
;; "  #8#a#c#e#  "
;; "  #########  "

(def day23-sample1
  ;; "#############"
  ;; "#...........#"
  ;; "###B#C#B#D###"
  ;; "  #A#D#C#A#  "
  ;; "  #########  "
  {7  {:type :b}
   8  {:type :a}
   9  {:type :c}
   10 {:type :d}
   11 {:type :b}
   12 {:type :c}
   13 {:type :d}
   14 {:type :a}})

(def day23-input
;; "#############"
;; "#...........#"
;; "###D#D#A#A###"
;; "  #C#C#B#B#  "
;; "  #########  "
  {7  {:type :d}
   8  {:type :c}
   9  {:type :d}
   10 {:type :c}
   11 {:type :a}
   12 {:type :b}
   13 {:type :a}
   14 {:type :b}})

(def paths
  {0
   {1 [1]
    2 [1 2]
    3 [1 2 3]
    4 [1 2 3 4]
    5 [1 2 3 4 5]
    6 [1 2 3 4 5 6]
    7 [1 7]
    8 [1 7 8]
    9 [1 2 9]
    10 [1 2 9 10]
    11 [1 2 3 11]
    12 [1 2 3 11 12]
    13 [1 2 3 4 13]
    14 [1 2 3 4 13 14]}
   1 {0 [0]
      2 [2]
      3 [2 3]
      4 [2 3 4]
      5 [2 3 4 5]
      6 [2 3 4 5 6]
      7 [7]
      8 [7 8]
      9 [2 9]
      10 [2 9 10]
      11 [2 3 11]
      12 [2 3 11 12]
      13 [2 3 4 13]
      14 [2 3 4 13 14]}
   2 {0 [1 0]
      1 [1]
      3 [3]
      4 [3 4]
      5 [3 4 5]
      6 [3 4 5 6]
      7 [7]
      8 [7 8]
      9 [9]
      10 [9 10]
      11 [3 11]
      12 [3 11 12]
      13 [3 4 13]
      14 [3 4 13 14]}
   3 {0 [2 1 0]
      1 [2 1]
      2 [2]
      4 [4]
      5 [4 5]
      6 [4 5 6]
      7 [2 7]
      8 [2 7 8]
      9 [9]
      10 [9 10]
      11 [11]
      12 [11 12]
      13 [4 13]
      14 [4 13 14]}
   4 {0 [3 2 1 0]
      1 [3 2 1]
      2 [3 2]
      3 [3]
      5 [5]
      6 [5 6]
      7 [3 2 7]
      8 [3 2 7 8]
      9 [3 9]
      10 [3 9 10]
      11 [11]
      12 [11 12]
      13 [13]
      14 [13 14]}
   5 {0 [4 3 2 1 0]
      1 [4 3 2 1]
      2 [4 3 2]
      3 [4 3]
      4 [4]
      6 [6]
      7 [4 3 2 7]
      8 [4 3 2 7 8]
      9 [4 3 9]
      10 [4 3 9 10]
      11 [4 11]
      12 [4 11 12]
      13 [13]
      14 [13 14]}
   6 {0 [5 4 3 2 1 0]
      1 [5 4 3 2 1]
      2 [5 4 3 2]
      3 [5 4 3]
      4 [5 4]
      5 [5]
      7 [5 4 3 2 7]
      8 [5 4 3 2 7 8]
      9 [5 4 3 9]
      10 [5 4 3 9 10]
      11 [5 4 11]
      12 [5 4 11 12]
      13 [5 13]
      14 [5 13 14]}
   7 {0 [1 0]
      1 [1]
      2 [2]
      3 [2 3]
      4 [2 3 4]
      5 [2 3 4 5]
      6 [2 3 4 5 6]
      8 [8]
      9 [2 9]
      10 [2 9 10]
      11 [2 3 11]
      12 [2 3 11 12]
      13 [2 3 4 13]
      14 [2 3 4 13 14]}
   8 {0 [7 1 0]
      1 [7 1]
      2 [7 2]
      3 [7 2 3]
      4 [7 2 3 4]
      5 [7 2 3 4 5]
      6 [7 2 3 4 5 6]
      7 [7]
      9 [7 2 9]
      10 [7 2 9 10]
      11 [7 2 3 11]
      12 [7 2 3 11 12]
      13 [7 2 3 4 13]
      14 [7 2 3 4 13 14]}
   9 {0 [2 1 0]
      1 [2 1]
      2 [2]
      3 [3]
      4 [3 4]
      5 [3 4 5]
      6 [3 4 5 6]
      7 [2 7]
      8 [2 7 8]
      10 [10]
      11 [3 11]
      12 [3 11 12]
      13 [3 4 13]
      14 [3 4 13 14]}
   10 {0 [9 2 1 0]
       1 [9 2 1]
       2 [9 2]
       3 [9 3]
       4 [9 3 4]
       5 [9 3 4 5]
       6 [9 3 4 5 6]
       7 [9 2 7]
       8 [9 2 7 8]
       9 [9]
       11 [9 3 11]
       12 [9 3 11 12]
       13 [9 3 4 13]
       14 [9 3 4 13 14]}
   11 {0 [3 2 1 0]
       1 [3 2 1]
       2 [3 2]
       3 [3]
       4 [4]
       5 [4 5]
       6 [4 5 6]
       7 [3 2 7]
       8 [3 2 7 8]
       9 [3 9]
       10 [3 9 10]
       12 [12]
       13 [4 13]
       14 [4 13 14]}
   12 {0 [11 3 2 1 0]
       1 [11 3 2 1]
       2 [11 3 2]
       3 [11 3]
       4 [11 4]
       5 [11 4 5]
       6 [11 4 5 6]
       7 [11 3 2 7]
       8 [11 3 2 7 8]
       9 [11 3 9]
       10 [11 3 9 10]
       11 [11]
       13 [11 4 13]
       14 [11 4 13 14]}
   13 {0 [4 3 2 1 0]
       1 [4 3 2 1]
       2 [4 3 2]
       3 [4 3]
       4 [4]
       5 [5]
       6 [5 6]
       7 [4 3 2 7]
       8 [4 3 2 7 8]
       9 [4 3 9]
       10 [4 3 9 10]
       11 [4 11]
       12 [4 11 12]
       14 [14]}
   14 {0 [13 4 3 2 1 0]
       1 [13 4 3 2 1]
       2 [13 4 3 2]
       3 [13 4 3]
       4 [13 4]
       5 [13 5]
       6 [13 5 6]
       7 [13 4 3 2 7]
       8 [13 4 3 2 7 8]
       9 [13 4 3 9]
       10 [13 4 3 9 10]
       11 [13 4 11]
       12 [13 4 11 12]
       13 [13]}})

(def distances
  ; 0 1 2 3 4 5  6 7  8 9 a b c d e
  [[0 1 3 5 7 9 10 3  4 5 6 7 8 9 10] ;0
   [1 0 2 4 6 8  9 2  3 4 5 6 7 8 9] ;1
   [3 2 0 2 4 6  7 2  3 2 3 4 5 6 7] ;2
   [5 4 2 0 2 4  5 4  5 2 3 2 3 4 5] ;3
   [7 6 4 2 0 2  3 6  7 4 5 2 3 2 3] ;4
   [9 8 6 4 2 0  1 8  9 6 7 4 5 2 3] ;5
   [10 9 7 5 3 1 0 9 10 7 8 5 6 3 4] ;6
   [3 2 2 4 6 8  9 0  1 4 5 6 7 8 9] ;7
   [4 3 3 5 7 9 10 1  0 5 6 7 8 9 10] ;8
   [5 4 2 2 4 6  7 4  5 0 1 4 5 6 7] ;9
   [6 5 3 3 5 7  8 5  6 1 0 5 6 7 8] ;a 10 
   [7 6 4 2 2 4  5 6  7 4 5 0 1 4 5] ;b 11
   [8 7 5 3 3 5  6 7  8 5 6 1 0 5 6] ;c 12
   [9 8 6 4 2 2  3 8  9 6 7 4 5 0 1] ;d 13
   [10 9 7 5 3 3 4 9 10 7 8 5 6 1 0] ;e 14
   ])

(defn initialize
  [input]
  (u/fmap #(assoc % :moves 0 :cost 0) input))

(defn next-moves-from-pos
  [open pos]
  (map first (filter #(every? open (second %)) (get paths pos))))

(defn next-moves
  [state]
  (let [open (complement (set (keys state)))]
    (map (partial next-moves-from-pos open) (keys state))))

;; I just sketched this solution out by hand on paper
(def day23-input-soln
  [[13 1]
   [14 5]
   [9 14]
   [7 13]
   [11 4]
   [12 2]
   [10 12]
   [2 10]
   [8 11]
   [1 8]
   [4 7]
   [5 9]])

(def move-multiplier
  {:a 1
   :b 10
   :c 100
   :d 1000})

(defn new-state
  [state [from to]]
  (let [{:keys [type moves cost]} (get state from)
        dist (get-in distances [from to])]
    (-> state
        (dissoc from)
        (assoc to {:type type
                   :moves (inc moves)
                   :cost (+ cost (* (move-multiplier type) dist))}))))

(defn cost-of-moves
  [input path]
  (->> (reduce new-state (initialize input) path)
       vals
       (map :cost)
       (reduce +)))

(defn day23-part1-soln
  []
  (cost-of-moves day23-input day23-input-soln))