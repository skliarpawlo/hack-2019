(ns hack-2019.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.session :as session]
   [reitit.frontend :as reitit]
   [clerk.core :as clerk]
   [accountant.core :as accountant]
   [hack-2019.data :refer [ec2 well-planned-jobs datasets]]
   [cljsjs.highcharts]))

;; -------------------------
;; Routes

(def router
  (reitit/router
   [["/" :index]]))

(defn path-for [route & [params]]
  (if params
    (:path (reitit/match-by-name router route params))
    (:path (reitit/match-by-name router route))))

;; -------------------------
;; Page components
(defonce state (reagent/atom
                {:instance-type nil
                 :instance-count nil
                 :hours nil}))

(defn select-instance-type [id]
  (reagent/create-class
   {:component-did-mount
    (fn [this]
      (let [dropdown (-> (js/$ (str "#" id))
                         .dropdown)]
        (.on dropdown "change" (fn [e]
                                 (swap! state
                                        assoc
                                        :instance-type
                                        (-> e .-target .-value))))))
    :reagent-render
    (fn [id]
      [:div.ui.selection.dropdown.fluid {:id id}
       [:input {:type "hidden" :name "instance-type"}]
       [:i.dropdown.icon]
       (if (nil? (:instance-type @state))
         [:div.default.text "Instance Type"]
         [:div.text (:instance-type @state)])
       [:div.menu
        (doall
         (for [inst ec2]
           [:div.item
            {:key (:type inst)
             :data-value (:type inst)
             :class (if (= (:type inst) (:instance-type @state)) "active selected")}
            (:repr inst)]))]])}))


(defn select-instance-count [id]
  (reagent/create-class
   {:component-did-mount
    (fn [this]
      (let [dropdown (-> (js/$ (str "#" id))
                         .dropdown)]
        (.on dropdown "change" (fn [e]
                                 (swap! state
                                        assoc
                                        :instance-count
                                        (-> e .-target .-value int))))))

    :reagent-render
    (fn [id]
      [:div.ui.selection.dropdown {:id id}
       [:input {:type "hidden" :name "instance-count"}]
       [:i.dropdown.icon]
       (if (nil? (:instance-count @state))
         [:div.default.text "Instance Count"]
         [:div.text "x"(:instance-count @state)])
       [:div.menu
        (doall
         (for [i (range 1 50)]
           [:div.item
            {:key i
             :data-value i
             :class (if (= i (:instance-count @state)) "active selected")}
            "x" i]))]])}))


(defn select-hours [id]
  (reagent/create-class
   {:component-did-mount
    (fn [this]
      (let [dropdown (-> (js/$ (str "#" id))
                         .dropdown)]
        (.on dropdown "change" (fn [e]
                                 (swap! state
                                        assoc
                                        :hours
                                        (-> e .-target .-value int))))))

    :reagent-render
    (fn [id]
      [:div.ui.selection.dropdown {:id id}
       [:input {:type "hidden" :name "instance-count"}]
       [:i.dropdown.icon]
       (if (nil? (:hours @state))
         [:div.default.text "Hours to work"]
         [:div.text (:hours @state) "h"])
       [:div.menu
        (doall
         (for [i (range 1 24)]
           [:div.item
            {:key i
             :data-value i
             :class (if (= i (:instance-count @state)) "active selected")}
            i "h"]))]])}))


(defn get-instance-conf [instance-type]
  (->> ec2
       (filter #(-> % :type (= instance-type)))
       first))


(defn get-job-cost-series [inst-type inst-count hours]
  (if (or (nil? inst-type) (nil? inst-count) (nil? hours)) nil
      (let [inst-conf (get-instance-conf inst-type)
            inst-price (:price inst-conf)]
        (for [h (range 0 (inc hours) 1)]
          (* inst-price inst-count h)))))


(defn get-well-planned-series [job-conf]
  (let [instance-type (job-conf :type)
        instance-count (job-conf :count)
        hours (job-conf :hours)]
    (get-job-cost-series instance-type instance-count hours)))


(defn time-cost-chart [id atom]
  (let [draw-highchart
        (fn [this] (let [your-data (get-job-cost-series
                                    (:instance-type @state)
                                    (:instance-count @state)
                                    (:hours @state))
                         well-planned-data (map
                                            (fn [item] (assoc item
                                                              :series
                                                              (get-well-planned-series item)))
                                            well-planned-jobs)
                         well-planned-series (for [item well-planned-data]
                                               {:name (:repr item)
                                                :data (:series item)})
                         chart-conf (clj->js {:title {:text "Time & Cost"}
                                              :yAxis {:title "Cost"
                                                      :labels {:format "{value}$"}}
                                              :xAxis {:title "Hours"
                                                      :labels {:format "{value}h"}}
                                              :tooltip {:headerFormat ""
                                                        :pointFormat "{point.x:.1f}h {point.y:.1f}$"}
                                              :legend {:layout "vertical"
                                                       :align "right"
                                                       :verticalAlign "middle"}
                                              :series (if (zero? (count your-data))
                                                        well-planned-series
                                                        (conj well-planned-series
                                                              {:color "red"
                                                               :dashStyle "Dash"
                                                               :name "Your Cluster"
                                                               :data your-data}))})]
                     (if (not (nil? your-data))
                       (js/Highcharts.chart "time-cost-chart" chart-conf))))]
    (reagent/create-class
     {:display-name "highchart"
      :component-did-mount draw-highchart
      :component-did-update draw-highchart
      :reagent-render
      (fn [id data]
        [:div#time-cost-chart
         {:style {:width "600px", :height "400px"}}])})))


(defn get-disk [inst-type inst-count]
  (if (or (nil? inst-type)
          (nil? inst-count))
    nil
    (let [inst-conf (get-instance-conf inst-type)]
      (* (inst-conf :disk) inst-count))))


(defn get-ram [inst-type inst-count]
  (if (or (nil? inst-type)
          (nil? inst-count))
    nil
    (let [inst-conf (get-instance-conf inst-type)]
      (* (inst-conf :ram) inst-count))))


(defn ram-chart [id atom]
  (let [draw-highchart
        (fn [this] (let [your-ram {:color "#F5BC42"
                                   :y (get-ram (@state :instance-type)
                                               (@state :instance-count))
                                   :repr "Your Cluster (RAM)"}
                         your-disk {:color "#42f596"
                                    :y (get-disk (@state :instance-type)
                                                 (@state :instance-count))
                                    :repr "Your Cluster (Disk)"}
                         datasets-data (conj (for [dataset datasets]
                                               {:color "#4287F5"
                                                :y (dataset :size-gb)
                                                :repr (dataset :repr)})
                                             your-ram your-disk)
                         data-sorted (sort-by :y datasets-data)
                         chart-conf (clj->js {:title {:text "Ram & Disk"}
                                              :chart {:type "bar"}
                                              :tooltip {:enabled false}
                                              :plotOptions {:bar {:dataLabels {:enabled true
                                                                               :format "{point.y:,.1f} Gb"}}}
                                              :yAxis {:title "" :labels {:format "{value} Gb"}}
                                              :legend {:enabled false}
                                              :xAxis {:categories (for [elem data-sorted] (elem :repr))}
                                              :series [{:data data-sorted}]})]
                     (if (not (nil? (your-ram :y)))
                       (js/Highcharts.chart "ram-chart" chart-conf))))]
    (reagent/create-class
     {:display-name "highchart"
      :component-did-mount draw-highchart
      :component-did-update draw-highchart
      :reagent-render
      (fn [id data]
        [:div#ram-chart
         {:style {:width "600px", :height "400px"}}])})))


(defn michael-the-pirate [id atom]
  (let [cluster-price-series (get-job-cost-series
                              (:instance-type @state)
                              (:instance-count @state)
                              (:hours @state))
        cluster-price (last cluster-price-series)]
    (if (and (not (nil? cluster-price))
             (> cluster-price 100))
      [:img {:style {:position "fixed"
                     :opacity (/ (- cluster-price 100) 200)
                     :left 0
                     :bottom 0}
             :src "/img/michael pirate.png"
             :width "300px"
             :height "300px"}])))


(defn home-page []
  (fn []
    [:span.main
     [:h1 "What kind of cluster do I need?"]
     [select-instance-type "instance-type"]
     [select-instance-count "instance-count"]
     [select-hours "hours"]
     [ram-chart "disk-chart" @state]
     [time-cost-chart "time-cost-chart" @state]
     [michael-the-pirate "michael-the-pirate" @state]]))



;; -------------------------
;; Translate routes -> page components

(defn page-for [route]
  (case route
    :index #'home-page))


;; -------------------------
;; Page mounting component

(defn current-page []
  (fn []
    (let [page (:current-page (session/get :route))]
      [:div
       [page]])))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (clerk/initialize!)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (let [match (reitit/match-by-path router path)
            current-page (:name (:data  match))
            route-params (:path-params match)]
        (reagent/after-render clerk/after-render!)
        (session/put! :route {:current-page (page-for current-page)
                              :route-params route-params})
        (clerk/navigate-page! path)
        ))
    :path-exists?
    (fn [path]
      (boolean (reitit/match-by-path router path)))})
  (accountant/dispatch-current!)
  (mount-root))
