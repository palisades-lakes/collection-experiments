(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.defs
  
  {:doc "Benchmarks for multiple dispatch alternatives."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.collex.transduce :as tr])
  (:import [clojure.lang IFn$L]))
;;----------------------------------------------------------------
;; element generators
;;----------------------------------------------------------------
(let [urp (prng/uniform-random-provider 
            "seeds/Well44497b-2017-07-25.edn")
      umin -100.0
      umax 100.0]
  (def ^IFn$L uint (prng/uniform-int -100 100 urp)))
;;----------------------------------------------------------------
;; reducers
;;----------------------------------------------------------------
;; sum the squares of the even values
;; idiomatic clojure here, not faster accumulators (yet)
(defn- square ^long [^long i] (* i i))

(def composed (tr/composed + square even?))
(def reduce-map-filter (tr/reduce-map-filter + square even?))
(def transducer (tr/transducer + square even?))
(def manual (tr/manual + square even?))
;;----------------------------------------------------------------
