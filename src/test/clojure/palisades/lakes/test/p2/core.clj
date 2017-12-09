;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

; Author: Frantisek Sodomka, Robert Lachlan

(ns palisades.lakes.test.p2.core
  
  {:doc "Test for the projective plane P2."
   :author "palisades dot lakes at gmail dot com"
   :version "2017-12-08"}
  
  (:require [clojure.test :as test])
  
  (:import [palisades.lakes.java.p2 D2 H2 Equivalent Rescale]))
;;----------------------------------------------------------------
(test/deftest rescale
  (let [p0 (D2/make 1.0 2.0)
        p1 (H2/make 1.0 2.0)
        p2 (H2/make 2.0 4.0 2.0)
        p3 (Rescale/rescale 2.0 p0)
        p4 (Rescale/rescale 2.0 p1)
        p5 (Rescale/rescale 2.0 p2)
        pp [p0 p1 p2 p3 p4 p5]]
    (doseq [pi pp
            pj pp]
      (test/is 
        (Equivalent/approximately pi pj)
        (print-str pi "\n" pj))
      (test/is 
        (Equivalent/approximately pi pj (Math/ulp 1.0))
        (print-str pi "\n" pj)))))
;;----------------------------------------------------------------
