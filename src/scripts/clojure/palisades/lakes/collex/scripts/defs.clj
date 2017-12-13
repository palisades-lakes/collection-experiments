(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.scripts.defs
  
  {:doc "Benchmarks for multiple dispatch alternatives."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-12"}
  
  (:require [palisades.lakes.bench.prng :as prng]
            [palisades.lakes.bench.generators :as g]
            [palisades.lakes.bench.core :as bench]
            [palisades.lakes.collex.transduce :as tr])
  
  (:import [clojure.lang IFn IPersistentVector]))
;;----------------------------------------------------------------
;; sum the squares of the even values
;; idiomatic clojure here, not faster accumulators (yet)
(defn- square ^long [^long i] (* i i))
(def composed (tr/composed + square even?))
(def reduce-map-filter (tr/reduce-map-filter + square even?))
(def transducer (tr/transducer + square even?))
(def manual (tr/manual + square even?))
;;----------------------------------------------------------------
(let [urp (prng/uniform-random-provider 
            "seeds/Well44497b-2017-07-25.edn")
      umin -100.0
      umax 100.0]
  (def ^IFn$L uint (prng/uniform-int -100 100 urp)))
;;----------------------------------------------------------------
(defn generate-list ^IPersistentVector [^IFn generator ^long n]
  (into '() (repeatedly n generator)))
;;----------------------------------------------------------------
(defn generate-vector ^IPersistentVector [^IFn generator ^long n]
  (into [] (repeatedly n generator)))
;;----------------------------------------------------------------
;; TODO: use spec to check generators sequence?
(defn generate-datasets
  
  "`generators` is a sequence of functions,
   with an even number of elements, like 
   `[container-generator0 element-generator0 
     container-generator1 element-generator1 ...]`
   
   These are called as if
   `[(container-generator0 element-generator0 nelements)
     (container-generator1 element-generator1 nelements) ...]`,
   returning `[dataset0 dataset1 ...]`, the arguments for a 
   function `f` to be benchmarked, which is done
   as if calling `(apply f [dataset0 dataset1 ...])`.

   This is done `nthreads` times, returning a vector of 
   `nthreads` vectors of data sets `[datasets0 datasets1 ...]`
   
   Finally, the benchmarking calls `(apply f datasetsi)`
   in parallel, in `nthreads` threads. The benchmark time
   is the time for all threads to complete."
  
  ([generators nelements nthreads]
    (assert (even? (count generators)))
    (assert (every? ifn? generators))
    
    (pp/pprint (partition 2 (map fn-name generators)))
    
    {:nelements nelements
     :nthreads nthreads
     :generators generators 
     :data
     (repeatedly 
       nthreads 
       (fn thread-datasets []
         (mapv 
           (fn dataset [[^IFn container-generator ^IFn element-generator]]
             (container-generator element-generator nelements))
           (partition 2 generators))))})
  
  ([generators nelements]
    (generate-datasets generators nelements (default-nthreads))))
;;----------------------------------------------------------------
(defn bench-single
  ([generators fns ^Map options]
    (let [options (merge defaults options)
          n (int (:n options))
          pause (int (:pause options))]
      (assert (every? ifn? generators))
      (assert (every? ifn? fns))
      (println (s/join " " (map fn-name generators)))
      (println n) 
      (println (.toString (java.time.LocalDateTime/now))) 
      (Thread/sleep (* pause 1000)) 
      (time
        (with-open [w (log-writer *ns* generators n)]
          (binding [*out* w]
            (print-system-info w)
            (println "generate-datasets")
            (let [data-map (time (generate-datasets generators n))
                  nthreads (int (:nthreads data-map (default-nthreads)))
                  pool (Executors/newFixedThreadPool nthreads)]
              (try
                (reduce
                  (fn add-record [records record]
                    (if record
                      (let [records (conj records record)]
                        (bench/write-tsv records (data-file *ns* generators n))
                        records)
                      records))
                  []
                  (map
                    (fn benchmark-one-fn [f]
                      (Thread/sleep (* pause 1000)) 
                      (println (.toString (java.time.LocalDateTime/now))) 
                      (time (criterium pool f data-map options)))
                    fns))
                (finally (.shutdown pool)))))))))
  ([generators fns] (bench generators fns {})))  
;;----------------------------------------------------------------
