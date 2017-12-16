(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.collex.arrays
  {:doc "Array utilities."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-15"})
;;----------------------------------------------------------------
;; primitive arrays
;;----------------------------------------------------------------
(def ^Class boolean-array-type (let [a (boolean-array 0)] (class a)))
(defn boolean-array? [x] (instance? boolean-array-type x))
(def ^Class byte-array-type (let [a (byte-array 0)] (class a)))
(defn byte-array? [x] (instance? byte-array-type x))
(def ^Class char-array-type (let [a (char-array 0)] (class a)))
(defn char-array? [x] (instance? char-array-type x))
(def ^Class double-array-type (let [a (double-array 0)] (class a)))
(defn double-array? [x] (instance? double-array-type x))
(def ^Class float-array-type (let [a (float-array 0)] (class a)))
(defn float-array? [x] (instance? float-array-type x))
(def ^Class int-array-type (let [a (int-array 0)] (class a)))
(defn int-array? [x] (instance? int-array-type x))
(def ^Class long-array-type (let [a (long-array 0)] (class a)))
(defn long-array? [x] (instance? long-array-type x))
(def ^Class short-array-type (let [a (short-array 0)] (class a)))
(defn short-array? [x] (instance? short-array-type x))
;;----------------------------------------------------------------
;; more general arrays
;;----------------------------------------------------------------
(def ^Class object-array-type (let [a (object-array 0)] (class a)))
(defn object-array? [x] (instance? object-array-type x))

(defn element-type ^Class [x]
  (let [c (.getClass ^Object x)]
    (assert (.isArray c))
    (.getComponentType c)))

(defn array? 
  ([x] (and x (.isArray (.getClass ^Object x))))
  ([x ^Class c]
    (and (array? x) 
         (.equals c (element-type x)))))

(defn elements-assignable-from? 
  [x ^Class c]
  (and (array? x) 
       (.isAssignableFrom (element-type x) c)))
;;----------------------------------------------------------------
