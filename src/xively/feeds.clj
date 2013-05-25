(ns xively.feeds
  (:use [xively.core]))

(defn- decorate-attributes
  "Clean attributes map from nice clean Clojure form, into the format Xively expects"
  ([feed]
    (decorate-attributes :v2))
  ([feed version]
    feed))

(defn get-feed
  "Return details of a feed"
  [feed-id]
  (let [resource (str "/feeds/" feed-id)]
    (api-call :get resource)))

(defn create-feed
  "Create a new feed on Xively. Passed a map containing our values"
  [feed]
  (let [resource "/feeds"
        feed (decorate-attributes feed)]
    (api-call :post resource feed)))
