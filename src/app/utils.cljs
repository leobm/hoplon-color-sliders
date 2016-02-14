(ns app.utils)

(defn js-apply [f target args]
  (.apply f target (to-array args)))

(defn log
  "Display messages to the console."
  [& args]
  (js-apply (.-log js/console) js/console args))

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
        h (.floor js/Math (* h 360))]
      {:h h :s s :l l}))

(defn hsl2rgb [h s l]
  (let [abs #(max % (- %))
        h' (/ h 60)
        h' (if (< h' 0) h' (- 6 (mod (- h') 6)))
        h' (mod h' 6)
        s' (max 0 (min 1 s))
        l' (max 0 (min 1 l))

        ;; (1 - Math.abs((2 * l) - 1)) * s
        c (* (- 1 (abs (- (* 2 l') 1)) ) s')
        x (* c (- 1 (abs (- (mod h' 2) 1))))
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
        r' (.round js/Math (* (+ r m) 255))
        g' (.round js/Math (* (+ g m) 255))
        b' (.round js/Math (* (+ b m) 255))]
        {:r r' :g g' :b b'}))

(defn trunc [num]
  (.floor js/Math num))

(defn fixed2 [num]
  (.toFixed (js/parseFloat num) 2 ))

(defn hexString [r g b]
  (let [hex #(.slice (str "00" (.toString (js/parseInt %) 16) ) (- 2)) ]
    (str "#"  (hex r) (hex g) (hex b))))

(defn rgbString [r g b]
  (str "rgb("r "," g "," b ")"))

(defn hslString [h s l]
  (str "hsl("h "," (trunc (* s 100)) "%," (trunc (* l 100)) "%)"))
