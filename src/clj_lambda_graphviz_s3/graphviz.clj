(ns clj-lambda-graphviz-s3.graphviz
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [taoensso.timbre :as log]
            [environ.core :as environ]))

(defn- uniq-file-path []
  (format "/tmp/%s" (java.util.UUID/randomUUID)))

(def use-system-graphviz? (environ/env :use-system-graphviz))

(def dot-path (if use-system-graphviz?
                "dot"
                "/tmp/dot_static"))

(defn- is-executable-created? []
  (.exists (io/file dot-path)))

(defn- copy-dot-to-temp []
  (log/info "create dot executable")
  (io/copy (io/file (io/resource "dot"))
           (io/file dot-path)))

(defn- create-dot-executable []
  (do (copy-dot-to-temp)
      (shell/sh "chmod" "+x" dot-path)))

(defn- generate-cli-command [dot options output-file]
  (str "echo \"" dot "\" | " dot-path " " options " > " output-file))

(defn- call-graphviz [dot options output-file]
  (let [sh-script (generate-cli-command dot options output-file)]
    (clojure.java.shell/sh "bash" "-c" sh-script)))

(defn generate [options dot]
  (let [output-file (uniq-file-path)]
    (if-not (and use-system-graphviz? 
                 (is-executable-created?))
      (create-dot-executable))
    (let [cli-res (call-graphviz dot options output-file)]
      (log/info "graphviz output" cli-res))
    (io/file output-file)))
