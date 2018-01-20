(ns flytag.tpl
  (:require
   [rum.core :as rum]
   [clojure.edn :as edn]
   [immutant.web :as web]
   [compojure.core :as cj]
   [clojure.java.io :as io]
   [compojure.route :as cjr]

   [flytag.core :as core])
  (:import
   [org.joda.time DateTime]
   [org.joda.time.format DateTimeFormat]))

(rum/defc base-html [opts config & content]
  (let [{:keys [title page styles scripts]
         :or {title "The Website"
              page :page-name}} opts]
    [:html
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:title (str title " Flytag.tv")]
      [:meta {:name "description" :content "flytag.tv"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
      [:link {:rel "stylesheet" :href "https://fonts.googleapis.com/css?family=Roboto+Condensed"}]
      (if (= (:env config) "prod")
        [:link {:rel "stylesheet" :href "/css/css23s.css"}]
        [:link {:rel "stylesheet" :href "/css/dev-main.css"}])
      [:script {:dangerouslySetInnerHTML {:__html
                                          "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
          (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
          m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
          })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');
          ga('create', 'UA-60478931-1', 'auto');
          ga('send', 'pageview');"}}]
      [:link {:rel "canonical" :href "https://flytag.tv"}]
      [:link {:rel "icon" :type "image/png" :href "/favicon.ico"}]]
     [:body
      [:.center-block
       content]
      panel
      rigth
      (if (= (:env config) "prod")
        [:script {:src "/js/hjsa765asd.js"}]
        [:script {:src "/js/dev-main.js"}])]]))

(rum/defc panel
  [:aside.left-block
    [:a.logo {:href "/"} "flytag.tv"]
    [:.menu
      [:.profile
        (if (:username user)
          ([:#username
            [:a {:title "exit" :href "/logout"} "[<-]"]
            [:a {:href (:url stream)} (:username user)]
            ]
            [:#enter {:style "display:none"} "Have an account? " [:a {:href "/login"} "Enter"]])
          ([:#username
            [:a {:title "exit" :href "/logout"} "[<-]"]
            [:a {:href ""} ""]
            ]
            [:#enter {:style "display:none"} "Have an account? " [:a {:href "/login"} "Enter"]])
          )]
      (if (:follows user)
        [:#follows-menu
          [:p.title "Followed Channels:"]
          [:div
            ]
        ]
      )
    
    ]])

  <aside class="left-block">
    <a href="/" class="logo">flytag.tv</a>
    <div class="menu">
       

        {{if .follows}}
        <div id="follows-menu">
            <p class="title">Followed Channels:</p>
            <div>
                {{range $follow := .follows}}
                <div class="follows-stream{{if not $follow.Live}} offline{{end}}">
                    <a href="/{{$follow.StreamURL}}">
                        <img {{if $follow.Logo}}
                            src="{{$.config.DomainStat}}/{{$follow.Logo}}"
                            {{else}}
                            src="{{$.config.DomainStat}}/img/no-img/noImageAvailable.jpg"
                            {{end}}
                            alt="{{$follow.Username}}">
                    </a>
                    <p class="stream"><a href="/{{$follow.StreamURL}}">{{$follow.Username}}</a></p>
                    <p class="game"><a
                            href="/game/{{$follow.GameURL}}">{{$follow.GameName}}</a>
                        <span>{{if $follow.Live}}
                            {{$follow.Viewers}}
                            {{else}}
                            Offline
                        {{end}}</span></p>
                </div>
                {{end}}
            </div>
        </div>
        {{end}}

    </div>

    <footer class="wrapper">
        <a href="mailto:help@flytag.tv">help@flytag.tv</a>
    </footer>
</aside>

  )
(rum/defc game-html [game]
  [:h1.game-item
   [:a {:href (str "/game/" (:name game)) :title (:name game)}
    (if (seq (:box-large game))
      [:img {:src (str (:domain-stat game) "/" (:box-large game))
             :width 200 :height 279}]
      [:img {:src (str (:domain-stat game) "/img/no-img/noImageAvailable.jpg")
             :width 200 :height 279}])]])

(rum/defc stream-html [stream]
  [:h1.stream-item {:id (:url stream)}
   [:a {:title (:username stream) :href (str "/" (:url stream))}
    (if (seq (:logo stream))
      [:img {:src (str (:domain-stat stream) "/" (:logo stream))
             :alt (:username stream) :width 200 :height 200}]
      [:img {:src (str (:domain-stat stream) "/img/no-img/noImageAvailable.jpg")
             :alt (:username stream) :width 200 :height 200}])]
   [:.meta
    [:p.title {:title (:status stream)} (:status stream)]
    [:p.info
     [:span.viewers (:viewers stream)]
     " viewers by " [:span.shadow-name (:username stream)]]]])

(rum/defc stream-detail-html [stream]
  [:.stream-meta
   (if (seq (:logo stream))
     [:img {:src (str (:domain-stat stream) "/" (:logo stream))
            :alt (:username stream) :width 60 :height 60}]
     [:img {:src (str (:domain-stat stream) "/img/no-img/noImageAvailable.jpg")
            :alt (:username stream) :width 60 :height 60}])
   [:.info
    [:h1.title {:title (:username stream)} (:username stream)]]])

      ;   [:.rating
      ; (if (seq (:username stream))  
      ;     (if (= (seq (:url stream)) ())

      ;     )
      ;     [:#rating-number {:title (:rating stream)} "[ " (:rating-value stream) " ]"])]
      ;     ]
      ;       {{if .username}}

        ;         {{if ne .stream.Stream_url .streamURL}}
        ;             <button data-user-id="{{ .stream.ID }}" id="follow-button"
        ;             {{if .stream.Follows}}
        ;             class="active"
        ;             {{end}}
        ;             >Follow</button>
        ;             <button data-user-id="{{ .stream.ID }}" id="rating-good" class='good
        ;             {{range $rating := .stream.Ratings}}
        ;                 {{if eq $rating.Symbol "+"}}
        ;                 active
        ;             {{end}}{{end}}
        ;             '></button>

        ;             <div title="Rating" id="rating-number">{{ .stream.RatingValue }}</div>

        ;             <button data-user-id="{{ .stream.ID }}" id="rating-bad" class='bad
        ;             {{range $rating := .stream.Ratings}}
        ;                 {{if eq $rating.Symbol "-"}}
        ;                 active
        ;             {{end}}{{end}}
        ;             '></button>
        ;         {{end}}
        ;     {{else}}

        ;     {{end}}
        ;         <div class="stat">

        ;             <span title="Watching now"><img width="18" height="18" src="/img/images/user-group-60.png">&nbsp;<!--
        ;                 -->{{.stream.Viewers}}</span>
        ;             &nbsp;&nbsp;
        ;             <span title="Total views"><img width="18" height="18" src="/img/images/eye-60.png">&nbsp;<!--
        ;                 -->{{.stream.Views}}</span>
        ;         </div>
        ;         <div class="share">
        ;             <button id="share">Share</button>
        ;     {{if .username}}
        ;             <button data-user-id="{{ .stream.ID }}" id="block-button" title="block-stream">
        ;                 <img width="20" height="20" src="/img/images/block-128.png"></button>
        ;     {{end}}
        ;         </div>
        ;     </div>
        ; </div>)

(defn render-html [component]
  (str "<!DOCTYPE html>" (rum/render-static-markup component)))
