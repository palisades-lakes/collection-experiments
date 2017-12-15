(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.sizeof
  
  "Estimate RAM usage for various containers."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-14"}
  
  (:require [palisades.lakes.collex.containers :as containers]
            [palisades.lakes.collex.scripts.defs :as defs])
  
  (:import [com.carrotsearch.sizeof RamUsageEstimator]))
;; clj12g src\scripts\clojure\palisades\lakes\collex\scripts\sizeof.clj 
;;----------------------------------------------------------------
(doseq [container
        [containers/array-of-int 
         containers/lazy-sequence
         containers/persistent-list 
         containers/persistent-vector
         containers/array-list
         containers/immutable-list]]
  (doseq [^long n (take 5 (iterate (partial * 4) (* 8 8 8 8 8)))]
    (let [data (container defs/uint n)
          sizeof (RamUsageEstimator/sizeOf data)
          dname (.getCanonicalName (class data))]
      (println dname n sizeof (float (/ sizeof n))))))
;;----------------------------------------------------------------
