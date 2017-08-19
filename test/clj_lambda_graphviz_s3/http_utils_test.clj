(ns clj-lambda-graphviz-s3.http-utils-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [manifold.deferred :as d]
            [clj-lambda-graphviz-s3.http-utils :refer :all]))

(deftest input-stream-to-buffered-reader-test
  (testing "should throw error if passed not an input stream"
    (is (thrown? java.lang.AssertionError
                 (input-stream-to-buffered-reader ""))))
  (testing "should not throw error if passed an input stream"
    (is (input-stream-to-buffered-reader (io/input-stream (.getBytes "text")))))

  (testing "should return real buffered reader"
    (let [buffered-reader (input-stream-to-buffered-reader (io/input-stream (.getBytes "text")))
          text (slurp buffered-reader)]
      (is (= "text" text)))))

(deftest parse-json-input-stream-test
  (testing "should throw error if passed not an input stream"
    (is (thrown? java.lang.AssertionError
                 (parse-json-input-stream ""))))

  (testing "should not throw error if passed input stream"
    (is (parse-json-input-stream (io/input-stream (.getBytes "{}")))))

  (testing "should parse json and return map"
    (is (= {:test-key "test-value"}
           (parse-json-input-stream (io/input-stream (.getBytes "{\"test-key\":\"test-value\"}")))))))

(deftest parse-json-body-in-res-test
  (testing "should throw error if passed not a deferred"
    (is (thrown? java.lang.AssertionError
                 (parse-json-body-in-res "res"))))
  (testing "should not throw error if passed a deferred"
    (is (parse-json-body-in-res (d/deferred ""))))

  (testing "should parse body and update it"
    (let [res (d/deferred)]
      (d/success! res {:body (io/input-stream (.getBytes "{\"test-key\":\"test-value\"}"))})
      (is (= {:body {:test-key "test-value"}}
             @(parse-json-body-in-res res)))))

  (testing "should parse body of error response"
    (let [res (d/deferred)]
      (d/error! res {:body (io/input-stream (.getBytes "{\"test-key\":\"test-value\"}"))})
      (try @(parse-json-body-in-res res)
           (catch Throwable t
             (is (= {:body {:test-key "test-value"}} (:error (ex-data t)))))))))
