(defproject metosin/codemirror-parinfer "0.0-SNAPSHOT"
  :description "site for illustrating the value of Parinfer"
  :url "http://shaunlebron.github.io/parinfer"

  :license {:name "MIT License"
            :url "https://github.com/shaunlebron/parinfer/blob/master/LICENSE.md"
            :distribution :repo}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.562"]
                 [parinfer-cljs "1.5.1-0"]
                 [cljsjs/codemirror "5.24.0-1"]
                 [cljsjs/parinfer "2.0.0-0"]]

  :plugins [[lein-cljsbuild "1.1.6"]
            [lein-figwheel "0.5.10"]]

  :source-paths []

  :clean-targets ^{:protect false} ["resources/public/js/compiled/out" "target"]

  :cljsbuild {
    :test-commands {"test" ["node" "resources/public/js/compiled/parinfer-test.js"]}
    :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel {:on-jsload "parinfer-codemirror.example/on-js-reload" }
              :compiler {:main parinfer-codemirror.example
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/parinfer.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true }}
             {:id "min"
              :source-paths ["src"]
              :compiler {:output-to "resources/public/js/compiled/parinfer.js"
                         :externs ["resources/public/codemirror/lib/closure-externs.js"]
                         :main      parinfer-codemirror.example
                         :optimizations :advanced
                         :pretty-print false}}

             ]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             })
