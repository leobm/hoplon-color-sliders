(ns hoplon-app.util.colors
   (:require [hoplon-app.util :as util]))

(defn rgb2hsl [r g b]
  (let [r (/ r 255)
        g (/ g 255)
        b (/ b 255)
        max' (max r g b)
        min' (min r g b)
        l (/ (+ max' min') 2)
        d (- max' min')
        s (if (> l 0.5)
            (/ d (- 2 max' min'))
            (/ d (+ max' min')))
        h (cond
            (= r max') (+ (/ (- g b) d) (if (< g b) 6 0))
            (= g max') (+ (/ (- b r) d) 2)
            (= b max') (+ (/ (- r g) d) 4))
        h (/ h 6)
        h (if (= max' min') 0 h)
        s (if (= max' min') 0 s)
        h (util/floor (* h 360))]
    {:h h :s s :l l}))


(defn hsl2rgb [h s l]
  (let [h' (/ h 60)
        h' (if (< h' 0) h' (- 6 (mod (- h') 6)))
        h' (mod h' 6)
        s' (max 0 (min 1 s))
        l' (max 0 (min 1 l))
        c (* (- 1 (util/abs (- (* 2 l') 1)) ) s')
        x (* c (- 1 (util/abs (- (mod h' 2) 1))))
        r (cond
            (< h' 1) c
            (< h' 2) x
            (< h' 3) 0
            (< h' 4) 0
            (< h' 5) x
            :else c)
        g (cond
            (< h' 1) x
            (< h' 2) c
            (< h' 3) c
            (< h' 4) x
            (< h' 5) 0
            :else 0)
        b (cond
            (< h' 1) 0
            (< h' 2) 0
            (< h' 3) x
            (< h' 4) c
            (< h' 5) c
            :else x)
        m (- l' (/ c 2))
        r' (util/round (* (+ r m) 255))
        g' (util/round (* (+ g m) 255))
        b' (util/round (* (+ b m) 255))]
        {:r r' :g g' :b b'}))

