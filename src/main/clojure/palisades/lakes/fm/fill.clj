(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.fm.fill
  
  {:doc "A generic 'collection' <code>fill</code> function,
         implemented with 
         <a href=\"https://github.com/palisades-lakes/faster-multimethods\">
         faster-multimethods</a>."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-11"}
  
  (:require [palisades.lakes.multimethods.core :as fm]
            [palisades.lakes.collex.arrays :as arrays])
  (:import [java.util ArrayList LinkedList]
           [clojure.lang IFn IFn$D IFn$L LazySeq
            IPersistentList IPersistentVector]
           [com.google.common.collect ImmutableList]))
;;----------------------------------------------------------------
;; TODO: are there performance implications to the arg order,
;; given the fact that prototype is only used for method lookup?

(fm/defmulti fill
  
  "Fill a new 'collection' with <code>n</code> values generated
   by repeated calls to a no-argument function 
   <code>generator</code>. For example, <code>generator</code>
   may be an encapsulated iterator over some other 'collection'.
   Another common case is a pseudo-random element generator for
   test or benchmarking data.
   <p>
   <code>prototype</code> is used to 
   determine what kind of 'collection' is returned, and is not
   modified, even if mutable and the right size. Methods are 
   defined for array prototypes as well as normal collections."
  
  {} #_{:arglists '([prototype ^IFn generator ^long n])}
  
  ;; TODO: use generator class in method lookup, to support
  ;; Iterator/Iterable, java.util.function.Function, etc.?
  
  (fn fill-dispatch ^Class [prototype ^IFn generator ^long n]
    (class prototype)))
;;----------------------------------------------------------------
(fm/defmethod fill 
  arrays/double-array-type
  [^doubles _ ^IFn generator ^long n]
  (let [x (double-array n)]
    (if (instance? IFn$D x)
      (dotimes [i (int n)] 
        (aset-double x i (.invokePrim ^IFn$D generator)))
      (dotimes [i (int n)] 
        (aset-double x i (double (generator)))))
    x))
;;----------------------------------------------------------------
(fm/defmethod fill 
  arrays/object-array-type
  [^objects prototype ^IFn generator ^long n]
  (let [^objects x (make-array (arrays/element-type prototype) n)]
    (dotimes [i (int n)] (aset x i (generator)))
    x))
;;----------------------------------------------------------------
(fm/defmethod fill 
  ArrayList
  [^ArrayList _ ^IFn generator ^long n]
  
  (let [x (ArrayList. n)]
    (dotimes [i (int n)] (.add x (generator)))
    x))
;;----------------------------------------------------------------
(fm/defmethod fill 
  LinkedList
  [^LinkedList _ ^IFn generator ^long n]
  
  (let [x (LinkedList.)]
    (dotimes [i (int n)] (.add x (generator)))
    x))
;;----------------------------------------------------------------
(fm/defmethod fill 
  ImmutableList
  [^ImmutableList _ ^IFn generator ^long n]
  
  (let [x (ImmutableList/builder)]
    (dotimes [i (int n)] (.add x (generator)))
    (.build x)))
;;----------------------------------------------------------------
(def prototype-LazySeq (take 0 (iterate identity 0)))
(assert (instance? LazySeq prototype-LazySeq))
(fm/defmethod fill 
  LazySeq
  [^LazySeq _ ^IFn generator ^long n]
  (repeatedly n generator))
;;----------------------------------------------------------------
(fm/defmethod fill 
  IPersistentVector
  [^IPersistentVector _ ^IFn generator ^long n]
  (into [] (repeatedly n generator)))
;;----------------------------------------------------------------
(fm/defmethod fill 
  IPersistentList
  [^IPersistentList _ ^IFn generator ^long n]
  (into '() (repeatedly n generator)))
;;----------------------------------------------------------------


