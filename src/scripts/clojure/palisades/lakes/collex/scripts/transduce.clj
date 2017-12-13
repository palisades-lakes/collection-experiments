(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.transduce
  
  "Use criterium for alternative reduce-map-filter 
   implementations."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-12"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.transduce :as tr]
            [palisades.lakes.collex.scripts.defs :as defs]))
;;----------------------------------------------------------------
(def options {} #_{:n 1024 :samples 4})
(bench/bench 
  [defs/generate-vector defs/uint]
  [defs/protocols
   defs/instanceof
   defs/instancefn
   defs/defmulti
   defs/hashmaps
   defs/signatures
   defs/nohierarchy
   defs/dynafun 
   defs/dynamap]
  options)
;;----------------------------------------------------------------
#_(shutdown-agents)
#_(System/exit 0)
