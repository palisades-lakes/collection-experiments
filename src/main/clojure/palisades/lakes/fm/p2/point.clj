(set! *warn-on-reflection* true) 
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns palisades.lakes.fm.p2.point
  
  {:doc "Multiple representations for points in the projective
         plane, implemented with
         <a href=\"https://github.com/palisades-lakes/faster-multimethods\">
         faster-multimethods</a>."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-11"}
  
  (:require [palisades.lakes.multimethods.core :as fm])
  (:import [clojure.lang IFn IFn$D]
           [palisades.lakes.java.p2 D2 Equivalent H2 Rescale]))
;;----------------------------------------------------------------
;; D2
;; H2
;; {:x x :y y}
;; {:x x :y y :w w}
;; [x y]
;; [x y w]
;; double[x y]
;; double[x y w]
;;----------------------------------------------------------------
