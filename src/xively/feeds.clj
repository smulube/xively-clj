(ns xively.feeds
  (:use [xively.core]))

(defn feed
  "Return details of a feed"
  [feed-id]
  (let [resource (str "/feeds/" feed-id)]
    (api-call :get resource)))
