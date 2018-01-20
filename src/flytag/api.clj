(ns flytag.api
  (:require
   [rum.core :as rum]
   [clojure.edn :as edn]
   [immutant.web :as web]
   [compojure.core :as cj]
   [clojure.java.io :as io]
   [compojure.route :as cjr]

   [flytag.tpl :as tpl]
   [flytag.core :as core])
  (:import
   [org.joda.time DateTime]
   [org.joda.time.format DateTimeFormat]))

(rum/defc index-page [games]
  (tpl/base-html {:page :index :title "Index page"}
                 {:env "dev"}
                ;  [:h2 "Games"]
                ;  [:.breadcrumbs
                ;   [:a {:href "/Games"} "/Games"]]
                 [:.center-block
                  [:.game-list-wrapper
                   (for [g games]
                     (tpl/game-html g))]]))

(rum/defc game-page [streams]
  (tpl/base-html {:page :game :title "Index streams"}
                 {:env "dev" :static-domain ""}
                 (for [s streams]
                   (tpl/stream-html s))))

(rum/defc stream-page [stream]
  (tpl/base-html {:page :stream :title "Index stream!!!"}
                 {:env "dev"}
                 (tpl/stream-detail-html stream)))
