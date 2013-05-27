# xively-clj

A Clojure library designed to interact with the Xively API.

## Usage

```clojure
(:require [xively.core :as xively]
          [xively.feeds :as feeds])

(def api-key "2rMKiuGNUsa2hXWH237savGI9897vKF4xRzYIS5H2cB1S9v2")

(xively/with-api-key api-key
  (feeds/get-feed 504))

(xively/with-api-key api-key
  (feeds/update-feed 504 {:title "Title",
                          :description "Description,
                          :descriptions [{:id "sensor1", :current-value "12.2"},
                                         {:id "sensor2", :current-value "182"},
                                         {:id "sensor3", :current-value "Fred"}]}))
```

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
