(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.transduce
  
  {:doc "Alternate implementations of reduce-map-filter."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:import [clojure.lang IFn]))
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
            (manual-rmf x (rest s))
            (recur (rest s))))))))
;;----------------------------------------------------------------

