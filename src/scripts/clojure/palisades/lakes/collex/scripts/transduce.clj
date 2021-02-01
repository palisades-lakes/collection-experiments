(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.transduce
  
  "Use criterium for alternative reduce-map-filter 
   implementations."
  {:author "palisades dot lakes at gmail dot com"
   :version "2021-01-31"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.transduce :as tr]
            [palisades.lakes.collex.containers :as containers]
            [palisades.lakes.collex.scripts.defs :as defs]))
;; clj src\scripts\clojure\palisades\lakes\collex\scripts\transduce.clj 
;;----------------------------------------------------------------
(doseq [^long n (mapv inc (take 6 (iterate (partial * 4) 256)))]
  (let [options {:n n
                 :benchmark "transduce"
                 :pause 60
                 :warmup-jit-period (* 16 1024 1024 1024)}]
    (doseq [container
            [containers/array-of-boxed-float
             containers/array-of-float 
             containers/array-list
             containers/immutable-list
             containers/persistent-vector
             containers/persistent-list
             containers/lazy-sequence
             containers/realized]]
      (println (bench/fn-name container) n)
      (bench/bench 
        [container defs/ufloat]
        [tr/inline
         defs/manual
         defs/transducer
         defs/reduce-map-filter
         defs/composed]
        options))
    #_(doseq [container
              [containers/array-of-boxed-int
               #_containers/array-of-boxed-float
               #_containers/array-of-float 
               containers/array-of-int 
               containers/array-list
               containers/immutable-list
               containers/persistent-vector
               containers/persistent-list
               containers/lazy-sequence
               containers/realized]]
        (println (bench/fn-name container) n)
        (bench/bench 
          [container defs/uint]
          [defs/inline
           defs/manual
           defs/transducer
           defs/reduce-map-filter
           defs/composed]
          options))))
;;----------------------------------------------------------------
(shutdown-agents)
(System/exit 0)
