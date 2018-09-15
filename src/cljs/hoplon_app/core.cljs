(ns hoplon-app.core
  (:require
    [hoplon.core
     :as h
     :include-macros true
     :refer-macros [defelem]]
    [javelin.core
     :refer [cell]
     :refer-macros [cell= defc defc=]]
    [hoplon-app.util :refer [trunc2 percent parse-int]]
    [hoplon-app.util.style :as s]
    [hoplon-app.util.colors :refer [rgb2hsl hsl2rgb]]
    [hoplon.jquery]
    [clojure.string :as str]))


;;(enable-console-print!)

(defc rgb
  { :r 255
    :g 0
    :b 0})

(defc hsl
  { :h 0
    :s 1.0
    :l 0.5})

(declare r' g' b')
(declare h' s' l')

(defn _rgb [value c]
  (swap! rgb assoc c value)
  (reset! hsl (rgb2hsl @r' @g' @b')))

(defn _hsl [value c]
  (swap! hsl assoc c value)
  (reset! rgb (hsl2rgb @h' @s' @l')))

(defc= r' (:r rgb) #(_rgb % :r))
(defc= g' (:g rgb) #(_rgb % :g))
(defc= b' (:b rgb) #(_rgb % :b))
(defc= h' (:h hsl) #(_hsl % :h))
(defc= s' (:s hsl) #(_hsl % :s))
(defc= l' (:l hsl) #(_hsl % :l))

(defc= hexString (s/hex r' g' b') #(reset! rgb (s/parse-hex %)))
(defc= rgbString (s/rgb r' g' b') #())
(defc= hslString (s/hsl h' (percent s') (percent l')) #())

(defelem text-input
  [{ :keys [value-fc view-fc converter validator]
     :or { view-fc   #(cell= value-fc)
           converter identity
           validator #(constantly true) }
     :as attr } _]

  (let [view-result (view-fc value-fc)
        error?      (cell false)
        _           (add-watch value-fc :watch-value
                               #(reset! error? false))
        do-change   #(let [new-value (converter @%)
                           valid? (validator new-value)]
                       (when valid?
                         (reset! value-fc new-value))
                       (reset! error? (not valid?)))]

    (h/input
      (assoc attr
        :type "text"
        :value view-result
        :class (cell= {:error  error?})
        :change do-change))))


(defelem range-number-input
  [{ :keys [value-fc view-fc min max converter]
     :or { view-fc   #(cell= value-fc)
           converter #(parse-int %) }} _]
  (text-input
      :size 3
      :value-fc value-fc
      :view-fc view-fc
      :converter converter
      :validator #(and (>= % min) (<= % max))))


(defelem range-input
  [{ :keys [lbl-text min max step value-fc style view-fc convert]
     :as attr} _]
    (h/div :class "slider-container"
      (h/label
        (h/text lbl-text))
      (h/input
        :type "range"
        :style style
        :min min
        :max max
        :step step
        :value value-fc
        :input #(reset! value-fc @%))
      (range-number-input attr)))


(defelem range-input-percent [attr]
  (range-input (assoc attr
                 :view-fc #(cell= (str (percent %) "%"))
                 :converter #(/ (parse-int %) 100))))


(defelem color-box [{:keys [color-fc validator] :as attr} _]
    (h/div :style "margin:4px; float:left; min-width: 150px; padding: 1.5em;"
         :css (cell= { :background-color color-fc})
      (text-input
        :value-fc color-fc
        :validator validator)))

(defn linear-gradient-all [gradient]
  (reduce (fn [acc prefix] (str acc "background: " prefix gradient  ";")) "" ["-moz-" "-webkit-" ""]))


(defn linear-gradient-rgb [arg]
  (let [[[rl gl bl ][rr gr br]] arg]
    (s/linear-gradient "left" (s/rgb rl gl bl) (s/rgb rr gr br))))


(defn linear-gradient-rgb-all [arg]
  (linear-gradient-all (linear-gradient-rgb arg)))


(defn linear-gradient-hsl-hue [s l]
  (let [rng (reduce
              #(conj %1 (s/hsl %2 (percent s) (percent l)))
              []
              (range 10 370 10))]
    (apply s/linear-gradient "left" rng)))


(defn linear-gradient-hsl-hue-all [s l]
  (linear-gradient-all (linear-gradient-hsl-hue s l)))


(defn linear-gradient-hsl-saturation [h l]
    (s/linear-gradient
      "left"
      (s/hsl h 0 (percent l))
      (s/hsl h 100 (percent l))))


(defn linear-gradient-hsl-luminosity [h s]
  (let [rng (reduce
              #(conj %1 (s/hsl h (percent s) %2))
              []
              (range 0 120 20))]
     (apply s/linear-gradient "left" rng)))


(defn linear-gradient-hsl-saturation-all [h l]
  (linear-gradient-all (linear-gradient-hsl-saturation h l)))


(defn linear-gradient-hsl-luminosity-all [h s]
  (linear-gradient-all (linear-gradient-hsl-luminosity h s)))


(defelem range-red []
  (let [style (cell= (linear-gradient-rgb-all [[0 g' b'][255 g' b']]))]
      (range-input :lbl-text "R" :style style :min 0 :max 255 :value-fc r')))


(defelem range-green []
  (let [style (cell= (linear-gradient-rgb-all [[r' 0  b'][r' 255 b']]))]
    (range-input :lbl-text "G" :style style :min 0 :max 255 :value-fc g')))


(defelem range-blue []
  (let [style (cell= (linear-gradient-rgb-all [[r' g'  9][r' g' 255]]))]
    (range-input :lbl-text "B" :style style :min 0 :max 255 :value-fc b')))


(defelem range-hue []
  (let [style (cell= (linear-gradient-hsl-hue-all s' l'))]
    (range-input :lbl-text "H" :style style :min 0 :max 360 :step 1 :value-fc h')))


(defelem range-saturation []
  (let [style (cell= (linear-gradient-hsl-saturation-all h' l'))]
    (range-input-percent :lbl-text "S" :style style :min 0.0 :max 1.0 :step 0.01 :value-fc s')))


(defelem range-luminosity []
  (let [style (cell= (linear-gradient-hsl-luminosity-all  h' s'))]
    (range-input-percent :lbl-text "L" :style style  :min 0.0 :max 1.0 :step 0.01 :value-fc l')))


(h/defelem home []
  (h/div
    :id "app"
    (h/form
      (range-red)
      (range-green)
      (range-blue)
      (range-hue)
      (range-saturation)
      (range-luminosity)
      (color-box :color-fc hexString :validator s/match-hex)
      (color-box :color-fc rgbString :validator s/match-rgb)
      (color-box :color-fc hslString :validator s/match-hsl))))

(defn mount-root []
  (js/jQuery #(.replaceWith (js/jQuery "#app") (home))))

(defn init! []
  (mount-root))


#_(re-find #"^[^0-9%]+$" "3a")

#_(defn check [e]
    (print (.fromCharCode js/String (.-which  e))))

#_(defn target-value [e]
    (-> e .-target .-value))
