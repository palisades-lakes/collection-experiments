(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.transduce
  
  {:doc "Alternate implementations of reduce-map-filter."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-18"}
  (:require [palisades.lakes.collex.arrays :as arrays])
  (:import [java.util ArrayList Iterator]
           [clojure.lang IFn IPersistentList IPersistentVector]))
;;----------------------------------------------------------------
;; TODO: handle general init values
;; TODO: primitive type hints?

(defn composed ^IFn [^IFn r ^IFn m ^IFn f]
  (comp (partial reduce r) (partial map m) (partial filter f)))

(defn reduce-map-filter ^IFn [^IFn r ^IFn m ^IFn f]
  (fn rmf [s] (reduce r (map m (filter f s)))))

;; !!!compose backwards for transducers!!!
(defn transducer ^IFn [^IFn r ^IFn m ^IFn f]
  (let [fm (comp (filter f) (map m))]
    (fn transducer-rmf [s] (transduce fm r s))))

(defn manual ^IFn [^IFn r ^IFn m ^IFn f]
  (let [rr (fn [a x]
             (if (f x)
               (r a (m x))
               a))]
    (fn manual-rmf 
      ([init s] (reduce rr init s))
      ([s] 
        (let [x (first s)]
          (if (f x) 
            (manual-rmf (m x) (rest s))
            (recur (rest s))))))))
;;----------------------------------------------------------------
;; a special case for benchmarking, could turn into a macro?
(defn inline [s]
  (cond 
    (instance? ArrayList s)
    (let [s ^ArrayList s
          n (int (.size s))]
      (loop [i (int 0)
             sum (double 0.0)]
        (if (>= i n)
          sum
          (let [si (double (.get s i))]
            (if (<= 0 si)
              (recur (+ i 1) (+ sum (* si si)))
              (recur (+ i 1) sum))))))
    (or (instance? IPersistentVector s)
        (instance? IPersistentList s))
      (loop [s s
             sum (double 0.0)]
        (if (empty? s)
          sum
          (recur (rest s) (+ sum (double (first s))))))
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
