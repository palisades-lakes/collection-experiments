(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.defs
  
  {:doc "Benchmarks for multiple dispatch alternatives."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-15"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.fm.iterator :as iterator]
            [palisades.lakes.collex.arrays :as arrays]
            [palisades.lakes.collex.transduce :as tr]))
;;----------------------------------------------------------------
;; element generators
;;----------------------------------------------------------------
(let [urp (prng/uniform-random-provider 
            "seeds/Well44497b-2017-07-25.edn")
      umin -1024
      umax 1024
      uint0 (prng/uniform-int umin umax urp)
      ufloat0 (prng/uniform-float (float umin) (float umax) urp)]
  (defn uint ^Integer [] (Integer/valueOf (int (uint0))))
  (defn ufloat ^Float [] (Float/valueOf (float (ufloat0)))))
;;----------------------------------------------------------------
;; reducers
;;----------------------------------------------------------------
;; sum the squares of the even values
;; idiomatic clojure here, not faster accumulators (yet)
(defn- sq ^long [^long i] (* i i))
(defn- non-negative? [^long i] (<= (long 0) i))

(def composed (tr/composed + sq non-negative?))
(def reduce-map-filter (tr/reduce-map-filter + sq non-negative?))
(def transducer (tr/transducer + sq non-negative?))
(def manual (tr/manual + sq non-negative?))
;;----------------------------------------------------------------
  