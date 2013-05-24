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

(describe "parse-json"
  (it "parses a json string returning keyword keys"
    (should= {:title "title", :product-id "12345"}
             (core/parse-json "{\"title\":\"title\",\"product_id\":\"12345\"}"))
    (should= nil (core/parse-json nil))))

(describe "safe-parse"
  (it "returns the parsed body for a 200 status code"
    (should= {:title "title", :product-id "12345"}
             (core/safe-parse {:headers "headers"
                               :status 200
                               :body "{\"title\":\"title\",\"product_id\":\"12345\"}"})))
  (it "returns a not-modified keyword for a 304 status code"
    (should= :xively.core/not-modified
             (core/safe-parse {:status 304})))
  (it "returns the original response but with a parsed body"
    (should= {:headers "headers"
              :status 500
              :body {:error "Server error"}}
             (core/safe-parse {:headers "headers"
                               :status 500
                               :body "{\"error\":\"Server error\"}"}))))
