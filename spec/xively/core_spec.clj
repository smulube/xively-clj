(ns xively.core-spec
  (:require [speclj.core :refer :all]
            [xively.core :as core]))

(describe "keywordise"
  (it "converts an underscored string returned to a clojure style keyword"
    (should= :title (core/keywordise "title"))
    (should= :product-id (core/keywordise "product_id")))
  (it "throws an exception if passed a nil string"
    (should-throw (core/keywordise nil))))

(describe "stringify"
  (it "converts a clojure style keyword into a string"
    (should= "title" (core/stringify :title))
    (should= "product_id" (core/stringify :product-id)))
  (it "throws an exception if passed a nil keyword"
    (should-throw (core/stringify nil))))

(describe "stringify-keys"
  (it "stringifies all the keys in a map"
    (should= {"title" "title"} (core/stringify-keys {:title "title"})))
  (it "handles maps which already have string keys"
    (should= {"title" "title"} (core/stringify-keys {"title" "title"}))))

(describe "parse-generic-json"
  (it "parses a json string returning keyword keys"
    (should= {:title "title", :product-id "12345"}
             (core/parse-generic-json "{\"title\":\"title\",\"product_id\":\"12345\"}"))
    (should= nil (core/parse-generic-json nil))))

(describe "build-generic-json"
  (it "builds a json string from passed in map"
    (should= "{\"title\":\"title\"}"
             (core/build-generic-json {:title "title"})))
  (it "should stringify the keys"
    (should= "{\"product_id\":\"12345\"}"
             (core/build-generic-json {:product-id "12345"}))))

(describe "parse-response"
  (it "returns map containing headers, status and parsed body for a 200 status code"
    (should= {:headers "headers",
              :status 200,
              :body { :title "title", :product-id "12345"}}
             (core/parse-response {:headers "headers"
                               :status 200
                               :body "{\"title\":\"title\",\"product_id\":\"12345\"}"})))
  (it "returns a not-modified keyword for a 304 status code"
    (should= :not-modified
             (core/parse-response {:status 304})))
  (it "returns the original response but with a parsed body"
    (should= {:headers "headers"
              :status 500
              :body {:error "Server error"}}
             (core/parse-response {:headers "headers"
                               :status 500
                               :body "{\"error\":\"Server error\"}"})))
  (it "filters out any extra headers"
    (should= {:headers "headers"
              :status 200
              :body {:title "title"}}
             (core/parse-response {:headers "headers"
                               :status 200
                               :body "{\"title\":\"title\"}"
                               :extra "extra"}))))

(describe "User-Agent string"
  (it "should have a default user agent"
    (should-not-be-nil core/*user-agent*)))

(describe "API url"
  (it "should have a default API url"
    (should-not-be-nil core/*api-url*)))

(describe "API key"
  (it "should not have a default API key"
    (should-be-nil core/*api-key*))
  (describe "when it has a binding"
    (around [it]
      (binding [core/*api-key* "12345"]
        (it)))
      (it "should use the bound API key"
        (should= "12345" core/*api-key*))))
