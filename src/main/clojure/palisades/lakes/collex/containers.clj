(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.containers
  
  {:doc "Factories for collections with 'random' elements."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:import [java.util ArrayList  Collection List]
           [com.google.common.collect ImmutableList]
           [clojure.lang IFn 
            IPersistentList IPersistentVector LazySeq]))
;;----------------------------------------------------------------
;; container generators
;;----------------------------------------------------------------
(defn array-list ^IPersistentList [^IFn generator ^long n]
  (ArrayList. ^Collection (repeatedly n generator)))
;;----------------------------------------------------------------
(defn immutable-list ^IPersistentList [^IFn generator ^long n]
  (ImmutableList/copyOf ^Iterable (repeatedly n generator)))
;;----------------------------------------------------------------
(defn persistent-list ^IPersistentList [^IFn generator ^long n]
  (into '() (repeatedly n generator)))
;;----------------------------------------------------------------
(defn persistent-vector ^IPersistentVector [^IFn generator ^long n]
  (into [] (repeatedly n generator)))
;;----------------------------------------------------------------
(defn lazy-sequence ^LazySeq [^IFn generator ^long n]
  (repeatedly n generator))
;;----------------------------------------------------------------

