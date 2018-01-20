(ns flytag.server
  (:require
   [immutant.web :as web]
   [compojure.core :as cj]
   [clojure.java.io :as io]
   [compojure.route :as cjr]

   [flytag.tpl :as tpl]
   [flytag.api :as api]
   [flytag.core :as core])
  (:gen-class))

(cj/defroutes routes
  (cjr/files "/i" {:root "public/i"})
  (->
   (cjr/resources "/css" {:root "public/css"})
   (core/with-headers {"Cache-Control" "no-cache"
                       "Expires" "2"}))
  (->
   (cjr/resources "/js" {:root "public/js"})
   (core/with-headers {"Cache-Control" "no-cache"
                       "Expires" "-1"}))
  (cj/GET "/" [:as req]
    {:body (tpl/render-html (api/index-page core/games))})
  (cj/GET "/game/:game" [game]
    {:body (tpl/render-html (api/game-page (get core/streams game)))})
  (cj/GET "/:stream" [stream]
    {:body (tpl/render-html (api/stream-page (get core/streams stream)))})
  (fn [req] {:status 404 :body "404 Not Found!"}))

(def app
  (-> routes
      (core/with-headers {"Content-Type" "text/html; charset=utf-8"
                          "Cache-Control" "no-cache"
                          "Expires"       "-1"})))

(defn -main [& args]
  (let [args-map (apply array-map args)
        port-str (or (get args-map "-p")
                     (get args-map "--port")
                     "8080")]
    (println "Starting web server on " port-str)
    (web/run #'app {:port (Integer/parseInt port-str)})))

(comment
  (def server (-main "--port" "8080"))
  (web/stop server))
