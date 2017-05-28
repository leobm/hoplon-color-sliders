(ns hoplon-app.util.style
  (:require [hoplon-app.util :as util]))

(defn rgb [r g b]
  (str "rgb(" r "," g "," b ")"))

(defn match-rgb [rgb]
  (re-find #"^rgb\(([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5]),([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5]),([01]?[0-9]?[0-9]|2[0-4][0-9]|25[0-5])\)$" rgb))

(defn parse-rgb [rgb]
 (let [[_ r g b] (match-rgb rgb)]
    { :r (util/hex->int r)
      :g (util/hex->int g)
      :b (util/hex->int b) }))


(defn hsl [h s l]
  (str "hsl(" h "," s "%," l "%)"))



(defn hex [r g b]
  (str "#" (util/byte->hex r) (util/byte->hex g) (util/byte->hex b)))

(defn match-hex [hex]
  (re-find #"^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$" hex))

(defn parse-hex [hex]
  (let [[_ r g b]  (match-hex hex)]
    { :r (util/hex->int r)
      :g (util/hex->int g)
      :b (util/hex->int b) }))



(defn- join [col]
  (apply str (interpose "," col)))

(defn linear-gradient
  [direction & color-forms]
    (str "linear-gradient(" direction ", " (join color-forms) ")"))
