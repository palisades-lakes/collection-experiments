(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.transduce
  
  "Use criterium for alternative reduce-map-filter 
   implementations."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-15"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.containers :as containers]
            [palisades.lakes.collex.scripts.defs :as defs]))
;; clj12g src\scripts\clojure\palisades\lakes\collex\scripts\transduce.clj 
;;----------------------------------------------------------------
(doseq [^long n (take 6 (drop 2 (iterate (partial * 4) (* 8 8 8 8 8))))]
  (let [options {:n n
                 :benchmark "transduce"
                 :pause 8
                 :warmup-jit-period (* 8 1024 1024 1024)}]
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
        [defs/inline
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
