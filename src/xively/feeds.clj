(ns xively.feeds
  (:require [xively.core :as core]))

(defn- build-json
  "Build JSON feed string for sending to API"
  [feed]
  (core/build-generic-json (merge feed {:version "1.0.0"})))

(defn get-feed
  "Return details of a feed"
  [feed-id]
  (let [resource (str "/feeds/" feed-id)
        response (core/api-call :get resource)]
    (if (= 200 (:status response))
        (:body response)
        response)))

(defn create-feed
  "Create a new feed on Xively. Passed a map containing our values. Returns the
  Location header if successful, else the complete response as a map."
  [feed]
  (let [resource "/feeds"
        body (build-json feed)
        response (core/api-call :post resource {:body body})]
    (if (= 201 (:status response))
      (get-in response [:headers "Location"])
      response)))

(defn update-feed
  "Update a feed on Xively. Passed a map containing the data to send"
  [id feed]
  (let [resource (str "/feeds/" id)
        body (build-json feed)
        response (core/api-call :put resource {:body body})]
    (if (= 200 (:status response))
      :ok
      response)))

(defn delete-feed
  "Delete a feed on Xively"
  [id]
  (let [resource (str "/feeds/" id)
        response (core/api-call :delete resource)]
    (if (= 200 (:status response))
      :ok
      response)))
