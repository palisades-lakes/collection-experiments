(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.sizeof
  
  "Estimate RAM usage for various containers."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-15"}
  
  (:require [clojure.string :as s]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.containers :as containers]
            [palisades.lakes.collex.scripts.defs :as defs])
  
  (:import [com.carrotsearch.sizeof RamUsageEstimator]))
;; clj12g src\scripts\clojure\palisades\lakes\collex\scripts\sizeof.clj > sizeof.csv 
;;----------------------------------------------------------------
(println "container,type,length,size,per-element")
(let [general [containers/array-list
               containers/immutable-list
               containers/persistent-vector
               containers/persistent-list
               containers/lazy-sequence
               containers/realized]
      nn (take 5 (iterate (partial * 4) (* 8 8 8 8 8)))]
  (doseq [container (concat [containers/array-of-float 
                             containers/array-of-boxed-float]
                            general)]
    (doseq [^long n nn]
      (let [fname (bench/fn-name container)
            data (container defs/ufloat n)
            sizeof (RamUsageEstimator/sizeOf data)
            dname (.getCanonicalName (class data))]
        (println 
          (s/join 
            "," [fname dname n sizeof (float (/ sizeof n))])))))
  (doseq [container (concat [containers/array-of-int 
                             containers/array-of-boxed-int]
                            general)]
    (doseq [^long n nn]
      (let [fname (bench/fn-name container)
            data (container defs/uint n)
            sizeof (RamUsageEstimator/sizeOf data)
            dname (.getCanonicalName (class data))]
        (println 
          (s/join 
            "," [fname dname n sizeof (float (/ sizeof n))]))))))
;;----------------------------------------------------------------
