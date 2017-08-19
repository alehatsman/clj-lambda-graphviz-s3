(ns clj-lambda-graphviz-s3.core
  (:require [clojure.java.io :as io]
            [lambada.core :refer [def-lambda-fn]]
            [cheshire.core :as json]
            [taoensso.timbre :as log]
            [environ.core :as environ]
            [clj-lambda-graphviz-s3.http-utils :as http-utils]
            [clj-lambda-graphviz-s3.spec :as spec]))

(defn get-env-vars []
  ;{:open-weather-api-key (environ/env :open-weather-api-key)})
  {})

(defn lambda-handler [body]
  (if-not (spec/valid-body? body)
    {:error (spec/explain body)}
    {:result :ok}))

(defn lambda-function
  [in out context]
  (log/info "start")
  (let [body (http-utils/parse-json-input-stream in)
        env-vars (get-env-vars)]
    (log/info "body" body ", env-vars" env-vars)
    (with-open [w (io/writer out)]
      (json/generate-stream (lambda-handler body) w)
      (log/info "lambda-function - finish"))))

(def-lambda-fn clj-lambda-graphviz.WebHook
  [in out context]
  (lambda-function in out context))
