(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.fm.map
  
  {:doc "A generic, usually eager <code>map</code> function,
         implemented with 
         <a href=\"https://github.com/palisades-lakes/faster-multimethods\">
         faster-multimethods</a>."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:refer-clojure :exclude [map])
  (:require [palisades.lakes.multimethods.core :as fm]
            [palisades.lakes.collex.arrays :as arrays]
            [palisades.lakes.collex.iterator :as iterator])
  (:import [java.util ArrayList Collections LinkedList]
           [clojure.lang IFn IFn$D IFn$L LazySeq
            IPersistentList IPersistentVector]
           [com.google.common.collect ImmutableList]))
;;----------------------------------------------------------------
(fm/defmulti map
  
  "Create a new data structure with the same shape as 
   <code>things0</code>, with each value <code>x0</code> replaced
   by <code>(f x0)</code> (or <code>(f x0 x1)</code>, etc., 
   depending on the number of data structure arguments in the
   call to <code>map</code>." 
  
  {:arglists '([^IFn f things0]
                [^IFn f things0 things1]
                [^IFn f things0 things1 things2]
                [^IFn f things0 things1 things2 things3])}
  ;; TODO: skip dispatching on (class f)?
  fm/signature)
;;----------------------------------------------------------------
(fm/defmethod map 
  (fm/to-signature IFn Iterator) 
  [^IFn f ^Iterator things]
  (let [a (ArrayList.)]
    (while (.hasNext things)
      (let [nxt (.next things)]
        (.add a (f nxt))))
    (Collections/unmodifiableList a)))

(fm/defmethod map 
  (fm/to-signature IFn Iterable) 
  [^IFn f ^Iterable things]
  (map f (.iterator things)))

(fm/defmethod map 
  (fm/to-signature IFn Collection) 
  [^IFn f ^Collection things]
  
  (let [a (ArrayList. (.size things))
        it (.iterator things)]
    (while (.hasNext it)
      (let [nxt (.next it)]
        (.add a (f nxt))))
    (Collections/unmodifiableList a)))

;; Note: requires f to be a function that takes 2 args.
;; Could use destructuring of Map$Entry 
;; --- need to compare performance.
(fm/defmethod map
  (fm/to-signature IFn Map)
  [^IFn f ^Map things]
  (let [m (HashMap. (.size things))
        it (iterator/iterator/iterator things)]
    (while (.hasNext it)
      (let [^Map$Entry nxt (.next it)
            k (.getKey nxt)
            v0 (.getValue nxt)
            v1 (f k v0)]
        (.put m k v1)))
    (Collections/unmodifiableMap m)))

(fm/prefer-method map 
                  (fm/to-signature IFn Map) 
                  (fm/to-signature IFn Object))

(fm/prefer-method map 
                  (fm/to-signature IFn Map) 
                  (fm/to-signature IFn Iterable))

(fm/defmethod map 
  (fm/to-signature IFn Seqable) 
  [^IFn f ^Seqable things]
  (let [a (if (counted? things) 
            (ArrayList. (count things))
            (ArrayList.))
        it (iterator/iterator things)]
    (while (.hasNext it)
      (let [nxt (.next it)]
        (.add a (f nxt))))
    (Collections/unmodifiableList a)))

(fm/prefer-method map 
                  (fm/to-signature IFn Seqable)
                  (fm/to-signature IFn Collection))
(fm/prefer-method map
                  (fm/to-signature IFn Seqable) 
                  (fm/to-signature IFn Iterable))
(fm/prefer-method map 
                  (fm/to-signature IFn Map)
                  (fm/to-signature IFn Seqable))

(fm/defmethod map 
  (fm/to-signature IFn IPersistentMap) 
  [^IFn f ^IPersistentMap things]
  (let [m (if (counted? things)
            (HashMap. (count things))
            (HashMap.))
        it (iterator/iterator things)]
    (while (.hasNext it)
      (let [^Map$Entry nxt (.next it)
            k (.getKey nxt)
            v0 (.getValue nxt)
            v1 (f k v0)]
        (.put m k v1)))
    (Collections/unmodifiableMap m)))

(fm/prefer-method map 
                  (fm/to-signature IFn IPersistentMap) 
                  (fm/to-signature IFn Collection))
(fm/prefer-method map 
                  (fm/to-signature IFn IPersistentMap) 
                  (fm/to-signature IFn Iterable))
(fm/prefer-method map 
                  (fm/to-signature IFn IPersistentMap) 
                  (fm/to-signature IFn Map))
(fm/prefer-method map 
                  (fm/to-signature IFn IPersistentMap)
                  (fm/to-signature IFn Seqable))

;;----------------------------------------------------------------
;; 3 args

(fm/defmethod map 
  (fm/to-signature IFn Iterator Iterator) 
  [^IFn f ^Iterator things0 ^Iterator things1]
  (let [a (ArrayList.)]
    (while (and (.hasNext things0) (.hasNext things1))
      (.add a (f (.next things0) (.next things1))))
    (Collections/unmodifiableList a)))

(fm/defmethod map 
  (fm/to-signature IFn Object Object) 
  [^IFn f ^Object things0 ^Object things1]
  (map f (iterator/iterator things0) (iterator/iterator things1)))

(fm/defmethod map 
  (fm/to-signature IFn Collection Collection) 
  [^IFn f ^Collection things0 ^Collection things1]
  (let [a (ArrayList.)
        i0 (iterator/iterator things0)
        i1 (iterator/iterator things1)]
    (while (and (.hasNext i0) (.hasNext i1))
      (.add a (f (.next i0) (.next i1))))
    (Collections/unmodifiableList a)))
;;----------------------------------------------------------------
;; 4 args

(fm/defmethod map 
  (fm/to-signature IFn Object Object Object) 
  [^IFn f ^Object things0 ^Object things1 ^Object things2]
  (let [a (ArrayList.)
        i0 (iterator/iterator things0)
        i1 (iterator/iterator things1)
        i2 (iterator/iterator things2)]
    (while (and (.hasNext i0) (.hasNext i1) (.hasNext i2))
      (.add a (f (.next i0) (.next i1) (.next i2))))
    (Collections/unmodifiableList a)))

(fm/defmethod map 
  (fm/to-signature IFn Collection Collection Collection) 
  [^IFn f 
   ^Collection things0 
   ^Collection things1 
   ^Collection things2]
  (let [a (ArrayList. (min (.size things0) 
                           (.size things1) 
                           (.size things2)))
        i0 (iterator/iterator things0)
        i1 (iterator/iterator things1)
        i2 (iterator/iterator things2)]
    (while (and (.hasNext i0) (.hasNext i1) (.hasNext i2))
      (.add a (f (.next i0) (.next i1) (.next i2))))
    (Collections/unmodifiableList a)))
;;----------------------------------------------------------------

