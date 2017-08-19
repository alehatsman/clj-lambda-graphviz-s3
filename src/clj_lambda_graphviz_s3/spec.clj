(ns clj-lambda-graphviz-s3.spec
  (:require [clojure.spec.alpha :as s]))

(s/def ::source string?)
(s/def ::options string?)
(s/def ::bucket string?)
(s/def ::body (s/keys :req-un [::source ::options ::bucket]))

(defn valid-body? [body]
  (s/valid? ::body body))

(defn explain [body]
  (s/explain-data ::body body))
