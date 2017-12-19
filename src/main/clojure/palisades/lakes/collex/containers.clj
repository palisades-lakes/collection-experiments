(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.containers
  
  {:doc "Factories for collections with 'random' elements."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-18"}
  
  (:import [java.util ArrayList  Collection List]
           [com.google.common.collect ImmutableList]
           [clojure.lang IFn 
            IPersistentList IPersistentVector LazySeq]))
;;----------------------------------------------------------------
;; container generators
;;----------------------------------------------------------------
(defn lazy-sequence ^LazySeq [^IFn generator ^long n]
  (repeatedly n generator))
;;----------------------------------------------------------------
(defn realized ^List [^IFn generator ^long n]
  (doall (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn array-list ^IPersistentList [^IFn generator ^long n]
  (ArrayList. ^Collection (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn immutable-list ^IPersistentList [^IFn generator ^long n]
  (ImmutableList/copyOf ^Iterable (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn array-of-int ^ints [^IFn generator ^long n]
  (into-array Integer/TYPE (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn array-of-boxed-int [^IFn generator ^long n]
  (into-array Integer (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn array-of-float ^floats [^IFn generator ^long n]
  (into-array Float/TYPE (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn array-of-boxed-float [^IFn generator ^long n]
  (into-array Float (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn persistent-list ^IPersistentList [^IFn generator ^long n]
  (into '() (lazy-sequence generator n)))
;;----------------------------------------------------------------
(defn persistent-vector ^IPersistentVector [^IFn generator ^long n]
  (into [] (lazy-sequence generator n)))
;;----------------------------------------------------------------
;; just returns a PersistentVector
#_(defn conjing [^IFn generator ^long n]
   (let [s (transient [])]
     (loop [s s
            n (int n)]
       (if (> 0 n)
         (persistent! s)
         (recur (conj! s (generator)) (dec n))))))
;;----------------------------------------------------------------

