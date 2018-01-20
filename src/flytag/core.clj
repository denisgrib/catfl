(ns flytag.core
  (:require

   [flytag.tpl :as tpl]
   [flytag.api :as api])
  (:import
   [org.joda.time DateTime]
   [org.joda.time.format DateTimeFormat]))

(def games
  [{:id 1
    :name "GxxaMe123"
    :url "game1"
    :create #inst "2017-12-27"
    :box-large "i/games/world-of-tanks/World-of-Tanks-272x380.jpg"
    :domain-stat "//flytag.tv"}
   {:id 2
    :name "111GaMe123"
    :create #inst "2017-12-28"
    :url "game666"
    :box-large ""
    :domain-stat "//flytag.tv"}])
(def streams
  {"GxxaMe123"
   [{:username "Stream1"
     :game-id 1
     :url "stream1"
     :status "status12"
     :create #inst "2017-12-27"
     :logo "i/games/world-of-tanks/World-of-Tanks-272x380.jpg"
     :domain-stat "//flytag.tv"
     :viewers 66623}
    {:username "stream23"
     :game-id 1
     :create #inst "2017-12-28"
     :url "stream666"
     :status "status12"
     :logo ""
     :domain-stat "//flytag.tv"
     :viewers 6662345}]
   "111GaMe123"
   [{:username "2Stream1"
     :game-id 2
     :url "stream1"
     :status "status12"
     :create #inst "2017-12-27"
     :logo "i/games/world-of-tanks/World-of-Tanks-272x380.jpg"
     :domain-stat "//flytag.tv"
     :viewers 66345}
    {:username "22stream23"
     :game-id 2
     :create #inst "2017-12-28"
     :url "stream666"
     :status "status12"
     :logo "22fkjhg"
     :domain-stat "//flytag.tv"
     :viewers 666}]})

(def date-formatter (DateTimeFormat/forPattern "dd.MM.YYYY"))
(defn render-date [inst]
  (.print date-formatter (DateTime. inst)))

; (prn pictures)
(defn with-headers [handler headers]
  (fn [request]
    (some-> (handler request)
            (update :headers merge headers))))

