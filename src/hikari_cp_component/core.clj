(ns hikari-cp-component.core
  (:require [hikari-cp.core :refer [make-datasource close-datasource]]
            [com.stuartsierra.component :as component]))

(defn- make-spec [hikari-cp]
  {:datasource (make-datasource hikari-cp)})

(defrecord HikariCP []
  component/Lifecycle
  (start [hikari-cp]
    (if (:spec hikari-cp)
      hikari-cp
      (assoc hikari-cp
             :spec (make-spec hikari-cp))))

  (stop [hikari-cp]
    (if-let [datasource (-> hikari-cp :spec :datasource)]
      (do (close-datasource datasource)
          (dissoc hikari-cp :spec))
      hikari-cp)))

(defn hikari-cp [config]
  (-> config
      (map->HikariCP)))
