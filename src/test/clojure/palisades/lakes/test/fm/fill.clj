(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.test.fm.fill
  
  {:doc "Tests for a <code>fill</code> generic function, implemented with 
         <a href=\"https://github.com/palisades-lakes/faster-multimethods\">
         faster-multimethods</a>."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:require [clojure.test :as test]
            [clojure.pprint :as pp]
            [palisades.lakes.collex.arrays :as arrays]
            [palisades.lakes.fm.fill :as fill])
  
  (:import [java.util ArrayList Iterator]
           [clojure.lang IFn IFn$D IFn$L LazySeq
            PersistentList PersistentVector]
           [com.google.common.collect ImmutableList]))
;;----------------------------------------------------------------
(let [^java.util.Iterator lcount (.iterator ^Iterable (iterate inc 0))] 
  (defn lcounter [] (.next lcount)))
(let [^Iterator dcount (.iterator ^Iterable (iterate inc 0.0))] 
  (defn dcounter ^double [] (double (.next dcount))))
;;----------------------------------------------------------------
(test/deftest tests
  (let [c00 (fill/fill (double-array 0) lcounter 2)
        c01 (fill/fill (double-array 0) dcounter 2)
        c02 (fill/fill (make-array Long 0) lcounter 2)
        c03 (fill/fill (make-array Double 0) dcounter 2)
        c04 (fill/fill (ArrayList. 0) lcounter 2)
        c05 (fill/fill (ArrayList. 0) dcounter 2)
        c06 (fill/fill (ImmutableList/of) lcounter 2)
        c07 (fill/fill (ImmutableList/of) dcounter 2)
        c08 (fill/fill [] lcounter 2)
        c09 (fill/fill [] dcounter 2)
        c10 (fill/fill (list) lcounter 2)
        c11 (fill/fill (list) dcounter 2)
        c12 (fill/fill fill/prototype-LazySeq lcounter 2)
        c13 (fill/fill fill/prototype-LazySeq dcounter 2)]
    (pp/pprint [c00 c01 c02 c03 c04 c05 c06 c07 c08 c09 c10 c11
                c12 c13])
    (test/is (arrays/double-array? c00))
    (test/is (arrays/double-array? c01))
    (test/is (= Long (arrays/element-type c02)))
    (test/is (= Double (arrays/element-type c03)))
    (test/is (instance? ArrayList c04))
    (test/is (instance? ArrayList c05))
    (test/is (instance? ImmutableList c06))
    (test/is (instance? ImmutableList c07))
    (test/is (instance? PersistentVector c08))
    (test/is (instance? PersistentVector c09))
    (test/is (instance? PersistentList c10))
    (test/is (instance? PersistentList c11))
    (test/is (instance? LazySeq c12))
    (test/is (instance? LazySeq c13))))
;;----------------------------------------------------------------
