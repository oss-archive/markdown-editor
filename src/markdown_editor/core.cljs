(ns markdown-editor.core
    (:require [reagent.core :as reagent :refer [atom]]
              [markdown.core :refer [md->html]]))

(enable-console-print!)

(defonce app-state (atom {:text "#Write some markdown here...\nYes, right here."}))

(defn update-text [event]
  (swap! app-state assoc :text event.target.value))

(defn hello-world []
  [:div
   [:div
    {:class "row"}
    [:h1
     "Markdown Editor"]
    [:div
     {:id "input"
      :class "container column"}
     [:textarea
      {:class "row"
       :onChange update-text}
      (:text @app-state)]]
    [:div
     {:id "output"
      :class "container column"
      :dangerouslySetInnerHTML {:__html (md->html (:text @app-state))}}]]])

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
