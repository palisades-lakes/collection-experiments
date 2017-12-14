(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.transduce
  
  "Use criterium for alternative reduce-map-filter 
   implementations."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.containers :as containers]
            [palisades.lakes.collex.scripts.defs :as defs]))
;; clj12g src\scripts\clojure\palisades\lakes\collex\scripts\transduce.clj 
;;----------------------------------------------------------------
(def options {:n (* 1024 1024)
              :benchmark "transduce"
              :pause 8
              ;;:samples 4
              :warmup-jit-period (* 32 1024 1024 1024)})
(bench/bench 
  [containers/persistent-vector defs/uint]
  [defs/composed
   defs/reduce-map-filter
   defs/transducer
   defs/manual]
  options)
;;----------------------------------------------------------------
(shutdown-agents)
(System/exit 0)
