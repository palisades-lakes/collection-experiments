(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.inline
  
  "Use criterium for alternative reduce-map-filter 
   implementations."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-17"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.transduce :as tr]
            [palisades.lakes.collex.containers :as containers]
            [palisades.lakes.collex.scripts.defs :as defs]))
;; cljy src\scripts\clojure\palisades\lakes\collex\scripts\inline.clj 
;; clj12g src\scripts\clojure\palisades\lakes\collex\scripts\inline.clj 
;;----------------------------------------------------------------
;; OOM in 12G at 128M
(let [^long n  (nth (iterate (partial * 4) (* 8 8 8 8 8)) 3)
      options {:n n
               :benchmark "inline"
               :pause 8
               :warmup-jit-period (* 8 1024 1024 1024)}]
  (bench/bench
    [containers/persistent-vector defs/ufloat]
    [tr/inline
     defs/transducer]
    options))
;;----------------------------------------------------------------
(shutdown-agents)
(System/exit 0)
