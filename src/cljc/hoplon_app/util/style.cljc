(ns hoplon-app.util.style)

(defn rgb [r g b]
  (str "rgb(" r "," g "," b ")"))

(defn hsl [h s l]
  (str "hsl(" h "," s "%," l "%)"))

(defn hex2 [i]
  #?(:clj (String/format "%02d"(java.lang.Integer/toHexString (Integer/parseInt i)))
     :cljs (.slice (str "00" (.toString (js/parseInt i) 16)) (- 2))))

(defn hex [r g b]
  (str "#" (hex2 r) (hex2 g) (hex2 b)))

(defn- join [col]
  (apply str (interpose "," col)))

(defn linear-gradient
  [direction & color-forms]
    (str "linear-gradient(" direction ", " (join color-forms) ")"))
