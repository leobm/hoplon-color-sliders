(ns hoplon-app.util)

(defn parse-color [str]
  { :hex (re-matches #"#[0-9a-f]{3,6}" str)
    :rgb (re-matches #"rgb[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?,[\s+]?(\d+)[\s+]?\)" str)
    :hsl (re-matches #"hsl[\s+]?\([\s+]?(\d+)[\s+]?,[\s+]?(\d+)%[\s+]?,[\s+]?(\d+)%[\s+]?\)" str)})


(defn floor [n]
  #?(:cljs (.floor js/Math n)
     :clj  (int (Math/floor n))))


(defn round [n]
  #?(:cljs (.round js/Math n)
     :clj  (Math/round n)))

(defn parse-float [s]
  #?(:cljs (js/parseFloat s)
     :clj (Float/parseFloat s)))

(defn parse-int [s]
   #?(:cljs (js/parseInt s)
      :clj (Integer/parseInt s)))

(defn trunc2 [s]
  #?(:cljs (.toFixed (parse-float s) 2)
     :clj  (String/format java.util.Locale/US "%.2f" (into-array [(parse-float s)]))))


(defn abs [n]
  (max n (- n)))

(defn percent [n]
  (floor (* n 100)))

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
        h (floor (* h 360))]
    {:h h :s s :l l}))


(defn hsl2rgb [h s l]
  (let [h' (/ h 60)
        h' (if (< h' 0) h' (- 6 (mod (- h') 6)))
        h' (mod h' 6)
        s' (max 0 (min 1 s))
        l' (max 0 (min 1 l))
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
        r' (round (* (+ r m) 255))
        g' (round (* (+ g m) 255))
        b' (round (* (+ b m) 255))]
        {:r r' :g g' :b b'}))

