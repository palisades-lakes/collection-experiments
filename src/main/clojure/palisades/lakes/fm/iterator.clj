(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.fm.iterator
  
  {:doc "A generic <code>iterator</code> function,
         implemented with 
         <a href=\"https://github.com/palisades-lakes/faster-multimethods\">
         faster-multimethods</a>."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-13"}
  
  (:refer-clojure :exclude [map])
  (:require [palisades.lakes.multimethods.core :as fm]
            [palisades.lakes.collex.arrays :as arrays])
  (:import [java.util ArrayList Collections Iterator LinkedList]
           [clojure.lang IFn IFn$D IFn$L LazySeq
            IPersistentList IPersistentVector]
           [com.google.common.collect ImmutableList]))
;;----------------------------------------------------------------
(fm/defmulti ^Iterator iterator
  "Return an iterator for <code>things</code>." 
  {:arglists '([things])}
  class)
;;----------------------------------------------------------------
(fm/defmethod iterator Iterator  [^Iterator things] 
  things)
(fm/defmethod iterator Iterable [^Iterable things] 
  (.iterator things))
(fm/defmethod iterator nil [_] 
  (Collections/emptyIterator))
(fm/defmethod iterator Map [^Map things] 
  (iterator (.entrySet things)))
(fm/prefer-method iterator Map Iterable)
(fm/defmethod iterator Multimap [^Multimap things] 
  (iterator (.asMap things)))
(fm/defmethod iterator Table [^Table things] 
  (iterator (.cellSet things)))

;; TODO: iterators that return unboxed double and long primitives 
;; (and String?)
(fm/defmethod iterator 
  arrays/boolean-array-type [^booleans things] 
  (iterator (Booleans/asList things)))
(fm/defmethod iterator 
  arrays/byte-array-type [^bytes things] 
  (iterator (Bytes/asList things)))
(fm/defmethod iterator 
  arrays/char-array-type [^chars things] 
  (iterator (Chars/asList things)))
(fm/defmethod iterator 
  arrays/double-array-type [^doubles things] 
  (iterator (Doubles/asList things)))
(fm/defmethod iterator 
  arrays/float-array-type [^floats things] 
  (iterator (Floats/asList things)))
(fm/defmethod iterator 
  arrays/int-array-type [^ints things] 
  (iterator (Longs/asList things)))
(fm/defmethod iterator 
  arrays/long-array-type [^longs things] 
  (iterator (Longs/asList things)))
(fm/defmethod iterator 
  arrays/short-array-type [^shorts things] 
  (iterator (Shorts/asList things)))
(fm/defmethod iterator 
  arrays/object-array-type [^objects things] 
  (iterator (Arrays/asList things)))
;;----------------------------------------------------------------

