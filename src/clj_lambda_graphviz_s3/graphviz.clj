(ns clj-lambda-graphviz-s3.graphviz
  (:require [clojure.java.io :as io]
            [clojure.java.shell :as shell]
            [taoensso.timbre :as log]))

(defn- uniq-file-id []
  (java.util.UUID/randomUUID))

(defn- uniq-file-path []
  (format "/tmp/%s" (uniq-file-id)))

(defn- pdf-file-path [dot-file-path]
  (format "%s.%s" dot-file-path "pdf"))

(defn- create-temp-dot-file [filepath dot]
  (io/make-parents filepath)
  (spit filepath dot))

(def dot-path "/temp/dot")

(defn- is-executable-created? []
  (.exists (io/file dot-path)))

(defn- copy-dot-to-temp []
  (io/copy (io/file (io/resource "dot"))
           (io/file dot-path)))

(defn create-dot-executable []
  (if-not (is-executable-created?)
    (copy-dot-to-temp)))

(defn generate [options dot]
  (log/info "graph generation - start")
  (let [dot-file-path (uniq-file-path)
        pdf-file-path (pdf-file-path dot-file-path)]
    (log/info "graph generation - dot-file-path" dot-file-path 
              "pdf-file-path" pdf-file-path)
    (try 
      (create-dot-executable)
      (create-temp-dot-file dot-file-path dot)
      (shell/sh dot-path options "-O" dot-file-path)
      (io/file pdf-file-path)
      (catch Exception e
        (log/error e))
      (finally 
        (io/delete-file pdf-file-path)))))

(defn generate-stream [options dot out]
  (let [pdf-file (generate options dot)]
      (io/copy pdf-file out)
      (io/delete-file pdf-file)))
