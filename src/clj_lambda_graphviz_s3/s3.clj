(ns clj-lambda-graphviz-s3.s3
  (:require [clojure.java.io :as io]
            [clj-s3-client.core :as s3]))

(defn- parse-bucket [bucket]
  (let [[_ bucket-name file-path] (re-find #"^([^/]+)\/(.+)$" bucket)]
    {:bucket-name bucket-name
     :file-path file-path}))

(defn put-file [file bucket]
  (let [{:keys [bucket-name file-path]} (parse-bucket bucket)
        client (s3/create-client)]
    (s3/put-object client bucket-name file-path (io/input-stream file)
                   {:content-length (.length file)})))
