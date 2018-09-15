(ns hoplon-app.util)

;;(set! *assert* false)

(defn floor [n]
  #?(:cljs (.floor js/Math n)
     :clj  (int (Math/floor n))))


(defn round [n]
  #?(:cljs (.round js/Math n)
     :clj  (Math/round n)))

(defn parse-float [s]
  #?(:cljs (js/parseFloat s)
     :clj (Float. (re-find  #"[-+]?[0-9]*\.?[0-9]+" s))))

(defn parse-int [s]
   #?(:cljs (js/parseInt s)
      :clj (Integer. (re-find  #"[-+]?\d+" s))))

(defn byte->hex [b]
  {:pre [(<= b 255)]}
  #?(:clj (String/format "%02x" (into-array [b]))
     :cljs (.slice (str "00" (.toString (js/parseInt b) 16)) (- 2))))

(defn int->hex [i]
  (cond
    (= i 0) ""
    :else (str (int->hex (bit-shift-right i 8))
               (byte->hex (bit-and 0xff i)))))

(defn hex->int [hex]
  {:pre [(string? hex)]}
  #?(:clj (Integer/parseInt hex 16)
     :cljs (js/parseInt hex 16)))

(defn abs [n]
  {:pre [(number? n)]}
  (max n (- n)))

(defn percent [n]
  {:pre [(and (>= n 0.0) (<= n 1.0))]}
  (floor (* n 100)))

(defn trunc2 [s]
  #?(:cljs (.toFixed (parse-float s) 2)
     :clj  (String/format java.util.Locale/US "%.2f" (into-array [(parse-float s)]))))

