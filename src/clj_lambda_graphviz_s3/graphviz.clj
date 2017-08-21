(ns clj-lambda-graphviz-s3.graphviz
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [taoensso.timbre :as log]))

(defn- uniq-file-id []
  (java.util.UUID/randomUUID))

(defn- uniq-file-path []
  (format "/tmp/%s" (uniq-file-id)))

(defn- create-temp-dot-file [filepath dot]
  (log/info "create-temp-dot-file " filepath dot)
  (io/make-parents filepath)
  (spit filepath dot))

(def dot-path "/tmp/dot_static")

(defn- file-exists? [path]
  (.exists (io/file path)))

(defn- is-executable-created? []
  (file-exists? dot-path))

(defn- copy-dot-to-temp []
  (log/info "create dot executable")
  (io/copy (io/file (io/resource "dot"))
           (io/file dot-path)))

(defn- create-dot-executable []
  (if-not (is-executable-created?)
    (do (copy-dot-to-temp)
        (shell/sh "chmod" "+x" dot-path))))

(defn- generate-cli-command [dot options output-file]
  (str "echo \"" dot "\" | " dot-path " " options " > " output-file))

(defn generate [options dot]
  (log/info "graph generation - start" options dot)
  (let [output-file (uniq-file-path)]
    (log/info "graph generation - output file" output-file)
    (create-dot-executable)
    (log/info "execute sh " "echo" dot "|" dot-path options "-O" output-file)
    (let [cli-res (clojure.java.shell/sh "bash" "-c" (generate-cli-command dot options output-file))]
      (log/info "cli-res" cli-res))
    (io/file output-file)))
