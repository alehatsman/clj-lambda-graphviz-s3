(ns clj-lambda-graphviz-s3.core-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [cheshire.core :as json]
            [manifold.deferred :as d]
            [clj-lambda-graphviz-s3.core :as core]))

(defn query-lambda [query cb]
  (let [temp-file-path "./temp-file"
        in (io/input-stream (.getBytes (json/encode query)))
        out (io/output-stream (io/file temp-file-path))
        context {}]
    (core/lambda-function in out context)
    (cb (slurp temp-file-path))
    (.delete (io/file temp-file-path))))

(deftest lambda-function-test
  (testing "should return validation error if body not valid"
    (query-lambda {}
                  (fn [res]
                    (is (= "{\"error\":{\"clojure.spec.alpha/problems\":[{\"path\":[],\"pred\":[\"clojure.core/fn\",[\"%\"],[\"clojure.core/contains?\",\"%\",\"source\"]],\"val\":{},\"via\":[\"clj-lambda-graphviz-s3.spec/body\"],\"in\":[]},{\"path\":[],\"pred\":[\"clojure.core/fn\",[\"%\"],[\"clojure.core/contains?\",\"%\",\"options\"]],\"val\":{},\"via\":[\"clj-lambda-graphviz-s3.spec/body\"],\"in\":[]},{\"path\":[],\"pred\":[\"clojure.core/fn\",[\"%\"],[\"clojure.core/contains?\",\"%\",\"bucket\"]],\"val\":{},\"via\":[\"clj-lambda-graphviz-s3.spec/body\"],\"in\":[]}],\"clojure.spec.alpha/spec\":\"clj-lambda-graphviz-s3.spec/body\",\"clojure.spec.alpha/value\":{}}}" res)))))

  (comment 
  (testing "should return result ok if body valid"
    (query-lambda {:source "graph { a -- b; b -- c; }"
                   :options "-Tpdf"
                   :bucket "outputbucket/out/file.pdf"}
                  (fn [res]
                    (is (= "{\"result\":\"ok\"}" res)))))))
