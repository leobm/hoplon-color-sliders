(ns hoplon-app.util)

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
  #?(:clj (Integer/parseInt hex 16)
     :cljs (js/parseInt hex 16)))

(defn abs [n]
  (max n (- n)))

(defn percent [n]
  (floor (* n 100)))

(defn trunc2 [s]
  #?(:cljs (.toFixed (parse-float s) 2)
     :clj  (String/format java.util.Locale/US "%.2f" (into-array [(parse-float s)]))))

