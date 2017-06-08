(ns parinfer-codemirror.editor
  "Glues Parinfer's formatter to a CodeMirror editor"
  (:require [clojure.string :as str]
            [parinfer-codemirror.editor-support :refer [update-cursor! fix-text!]]))

(defn before-change
  "Called before any change is applied to the editor."
  [cm change]
  ;; keep CodeMirror from reacting to a change from "setValue"
  ;; if it is not a new value.
  (when (and (= "setValue" (.-origin change))
             (= (.getValue cm) (str/join "\n" (.-text change))))
    (.cancel change)))

(defn on-change
  "Called after any change is applied to the editor."
  [mode cm change]
  (when (not= "setValue" (.-origin change))
    (fix-text! cm {:mode mode :change change})
    ; (update-cursor! cm change)
    ))

(defn on-cursor-activity
  "Called after the cursor moves in the editor."
  [mode cm]
  (fix-text! cm {:mode mode}))

(defn parinferize!
  "Add parinfer goodness to a codemirror editor"
  ([cm parinfer-mode]
   (let [initial-state {:mode parinfer-mode}]

     (.on cm "change" (partial on-change parinfer-mode))
     (.on cm "beforeChange" before-change)
     (.on cm "cursorActivity" (partial on-cursor-activity parinfer-mode))

     cm)))
