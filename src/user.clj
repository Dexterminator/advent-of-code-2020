(ns user)

(require 'hashp.core)
(use 'debux.core)

(defmethod print-method clojure.lang.PersistentQueue [q, w]
  (print-method (seq q) w))
