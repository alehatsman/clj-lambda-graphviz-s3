(defproject clj-lambda-graphviz-s3 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-future-spec "1.9.0-alpha17"]
                 [lambada "1.0.2"]
                 [cheshire "5.7.1"]
                 [byte-streams "0.2.3"]
                 [environ "1.1.0"]
                 [com.taoensso/timbre "4.10.0"]
                 [clj-s3-client "1.0.0"]]
  :plugins [[lein-environ "1.1.0"]]
  :resource-paths ["resources"]
  :uberjar-name "clj-lambda-graphviz-s3.zip"
  :profiles {:dev {:env {:use-system-graphviz true}}
             :lambda {:env {:use-system-graphviz false}}
             :uberjar {:aot :all}})
