(ns markdown-editor.core
    (:require [reagent.core :as reagent :refer [atom]]
              [alandipert.storage-atom :refer [local-storage]]
              [cemerick.url :as url]
              [markdown-editor.icon :as icon]
              [markdown-editor.action :as action]))

(def default-welcome-text
  (clojure.string/join "\n" ["# Cool Markdown Editor"
                             ""
                             "## Features"
                             ""
                             "- Edit markdown in your browser"
                             "- Save your markdown and continue later"
                             "- Download markdown to your computer"
                             "- Share permanent link"
                             ""
                             "![alt screenshot](https://imgs.xkcd.com/comics/lisp_cycles.png)"
                             ""
                             "__TRY NOW!__"]))

(defonce app-state (local-storage
                    (atom {:text default-welcome-text})
                    :app-state))

(defn editor-component [app-state]
  [:textarea.fl.w-50.h-100.bg-black-10.br.b--black-10.pa3.pa4-l.f6.f5-m.code
   {:value    (:text @app-state)
    :onChange #(swap! app-state assoc :text (-> % .-target .-value))}])

(defn markdown-render-component [app-state]
  [:div.fl.w-50.h-100.ph4
   [:div
    {:id "preview"
     :dangerouslySetInnerHTML {:__html (js/marked (:text @app-state))}}]])

(defn tools-component [app-state]
  [:div.flex.items-center.fixed.bottom-2.left-2
   [:a.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box.inline-flex.items-center.mr2
    {:onClick (partial action/download-markdown @app-state)}
    icon/download]
   [:a.black.bg-animate.hover-bg-black.items-center.pa3.ba.border-box.inline-flex.items-center
    {:onClick (partial action/share @app-state)}
    icon/share]])

(defn app [app-state]
  [:div.w-100.m0.h100.dib.h-100
   [editor-component app-state]
   [markdown-render-component app-state]
   [tools-component app-state]])

(defn initialize-app-state! [app-state]
  (when-let [b64-text (-> js/window
                          .-location
                          url/url
                          :query
                          (get "t"))]
    (let [text (js/atob b64-text)]
      (swap! app-state assoc :text text))))

(defn main! []
  (enable-console-print!)
  (initialize-app-state! app-state)
  (reagent/render-component [app app-state]
                            (. js/document (getElementById "app"))))
