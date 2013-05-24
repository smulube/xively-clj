(ns xively.core
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

;; Var containing the base URL for all API calls
(def ^:dynamic *api-url* "https://api.xively.com/v2")

;; Dynamic var in which we set our API key
(def ^:dynamic *api-key* nil)

;; User-Agent string
(def ^:dynamic *user-agent*
  (str "xively-clj/0.1.0-SNAPSHOT (https://xively.com) Clojure/"
       (clojure-version)))

(defn headers
  "Return map of headers suitable for passing to the API, merging in user
  supplied headers."
  [& user-headers]
  (merge {"X-ApiKey" *api-key*, "User-Agent" *user-agent*} user-headers))

(defn keywordise
  "Convert key strings returned in JSON from the API into Clojure style
  keywords, i.e. replace underscores with hyphens."
  [s]
  (-> s
    (.replace "_" "-")
    keyword))

(defn stringify
  "Convert a Clojure style keyword (with hyphens), into an underscored string."
  [k]
  (-> k
    name
    (.replace "-" "_")))

(defn stringify-keys
  "Create a map with keys converted to strings ready for sending to the Xively
  API."
  [m]
  (into (empty m)
    (for [[k v] m] [(stringify k) v])))

(defn parse-json
  "Same as json/parse-string but handles nil gracefully and returns keys as
  keywords."
  [s]
  (when s (json/parse-string s keywordise)))

(defn safe-parse
  [{:keys [headers status body] :as response}]
  (cond
    (= 304 status)
      ::not-modified
    (#{204 400 401 403 404 422 500 503} status)
    (update-in response [:body] parse-json)
    :else
      (parse-json body)))

(defmacro with-api-key
  "Set the api key to be used for all contained requests"
  [api-key & body]
  `(binding [*api-key* ~api-key]
     (do ~@body)))

(defn api-call
  "Make an HTTP request to the API"
  [http-method path & [opts]]
  (let [url (str *api-url* path)
        headers (headers (:headers opts))
        opts (dissoc opts :headers)]
    (safe-parse (http/request (merge {:method http-method,
                                      :url url,
                                      :headers headers} opts)))))
