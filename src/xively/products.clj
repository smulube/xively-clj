(ns xively.products
  (:use [xively.core]))

(defrecord Product [name description state product-id])
(defn get-product-response
  "Read a product response from the API. This includes headers and status
  code."
  [product-id]
  (let [resource (str "/products/" product-id)]
    (api-call :get resource)))
        ; response (api-call :get resource)))

(defn get-product
  "Get a product record from the API."
  [product-id]
  (let [response (get-product-response product-id)]
    (if (= 200 (:status response))
      (:body response)
      (:product response))))
