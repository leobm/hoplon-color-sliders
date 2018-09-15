(defproject hoplon-app "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring-server "0.4.0"]
                 [hoplon "7.0.1"]
                 [ring "1.6.1"]
                 [ring/ring-defaults "0.3.0"]
                 [compojure "1.6.0"]
                 [hiccup "1.0.5"]
                 [yogthos/config "0.8"]
                 [org.clojure/clojurescript "1.9.542" :scope "provided"]]

  :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.6"]
            [lein-asset-minifier "0.3.2"
             :exclusions [org.clojure/clojure]]
            [yogthos/lein-sass "0.1.4"]]
  :sass {:source "resources/sass" :target "resources/public/css"}

  :ring {:handler      hoplon-app.handler/app
         :uberwar-name "hoplon-app.war"}

  :min-lein-version "2.7.1"

  :uberjar-name "hoplon-app.jar"

  :main hoplon-app.server

  :clean-targets ^{:protect false}

  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  {:assets {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
                           {:output-to     "target/cljsbuild/public/js/app.js"
                            :output-dir    "target/uberjar"
                            :optimizations :advanced
                            :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :compiler
                           {:main          "hoplon-app.dev"
                            :asset-path    "/js/out"
                            :output-to     "target/cljsbuild/public/js/app.js"
                            :output-dir    "target/cljsbuild/public/js/out"
                            :source-map    true
                            :optimizations :none
                            :pretty-print  true}}}}


  :figwheel {:http-server-root "public"
             :server-port      3449
             :nrepl-port       7002
             :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"]
             :css-dirs         ["resources/public/css"]
             :ring-handler     hoplon-app.handler/app}

  :profiles {:dev
                      {:repl-options {:init-ns hoplon-app.repl}

                       :dependencies [[ring/ring-mock "0.3.0"]
                                      [ring/ring-devel "1.5.1"]
                                      [prone "1.1.4"]
                                      [figwheel-sidecar "0.5.10"]
                                      [org.clojure/tools.nrepl "0.2.12"]
                                      [com.cemerick/piggieback "0.2.1"]
                                      [pjstadig/humane-test-output "0.8.2"]]

                       :source-paths ["env/dev/clj"]
                       :plugins      [[lein-figwheel "0.5.10"]]
                       :injections   [(require 'pjstadig.humane-test-output)
                                      (pjstadig.humane-test-output/activate!)]

                       :env          {:dev true}}

             :uberjar {:hooks        [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks   ["compile" ["cljsbuild" "once" "min"]]
                       :env          {:production true}
                       :aot          :all
                       :omit-source  true}})
