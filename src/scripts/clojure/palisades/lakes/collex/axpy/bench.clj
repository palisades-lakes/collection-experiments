(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.multix.axpy.bench
  
  "Use criterium for alternative collection implementations."
  {:author "palisades dot lakes at gmail dot com"
   :version "2017-12-08"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.multix.axpy.defs :as defs]))
;;----------------------------------------------------------------
(def options {} #_{:n 1024 :samples 4})
;; array element types [D22 D2 D2]
(bench/bench 
   []
   [defs/invokevirtual
    defs/invokeinterface
    defs/protocols
    defs/defmulti
    defs/nohierarchy
    defs/dynafun]
   options)
;; array element types [LinearFunction Vector Vector]
;; (* 1/6 1/6 1/6): 1/216 change of repeated calls
(bench/bench 
   [g/d2s defs/d2
    g/linearfunctions defs/m22 
    g/vectors defs/v2 
    g/vectors defs/v2]
   [defs/invokeinterface
    defs/protocols
    defs/instanceof
    defs/instancefn
    defs/defmulti
    defs/hashmaps
    defs/signatures
    defs/nohierarchy
    defs/dynafun 
    defs/dynamap]
   options)
;; array element types [Object Object Object]
;; (* 1/6 1/6 1/6): 1/216 change of repeated calls
(bench/bench 
  [prng/objects defs/m22 
   prng/objects defs/v2 
   prng/objects defs/v2]
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
