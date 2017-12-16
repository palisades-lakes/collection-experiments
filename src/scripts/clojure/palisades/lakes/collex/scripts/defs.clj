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
            [palisades.lakes.collex.transduce :as tr])
  (:import [java.util Iterator]
           [clojure.lang IFn$L IFn$LO]))
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

(defn inline [s]
  (cond 
    (instance? Iterable s)
    (let [^Iterator it (.iterator ^Iterable s)]
      (loop [sum (long 0)]
        (if-not (.hasNext it)
          sum
          (let [si (long (.next it))]
            (if (<= 0 si)
              (recur (+ sum (* si si)))
              (recur sum))))))
    (arrays/int-array? s)
    (let [^ints a s
          n (int (alength a))]
      (loop [i (int 0)
             sum (long 0)]
        (if (<= n i)
          sum
          (let [ai (aget a i)]
            (if (<= 0 ai)
              (recur (inc i) (+ sum (* ai ai)))
              (recur (inc i)sum))))))
    (arrays/array? s Integer)
    (let [^objects a s
          n (int (alength a))]
      (loop [i (int 0)
             sum (long 0)]
        (if (<= n i)
          sum
          (let [ai (.intValue ^Integer (aget a i))]
            (if (<= 0 ai)
              (recur (inc i) (+ sum (* ai ai)))
              (recur (inc i)sum))))))
      (arrays/float-array? s)
      (let [^floats a s
            n (int (alength a))]
        (loop [i (int 0)
               sum (double 0.0)]
          (if (<= n i)
            sum
            (let [ai (double (aget a i))]
              (if (<= 0.0 ai)
                (recur (inc i) (+ sum (* ai ai)))
                (recur (inc i) sum))))))
      (arrays/array? s Float)
      (let [^objects a s
            n (int (alength a))]
        (loop [i (int 0)
               sum (double 0.0)]
          (if (<= n i)
            sum
            (let [ai (.doubleValue ^Float (aget a i))]
              (if (<= 0 ai)
                (recur (inc i) (+ sum (* ai ai)))
                (recur (inc i)sum))))))))
  ;;----------------------------------------------------------------
  