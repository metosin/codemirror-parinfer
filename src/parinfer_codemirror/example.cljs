(ns parinfer-codemirror.example
  (:require cljsjs.codemirror
            cljsjs.codemirror.addon.selection.active-line
            cljsjs.codemirror.addon.edit.matchbrackets
            parinfer-codemirror.clojure-parinfer-mode
            [parinfer-codemirror.editor :refer [parinferize!]]))

(defn on-tab
  "Indent selection or insert two spaces when tab is pressed.
  from: https://github.com/codemirror/CodeMirror/issues/988#issuecomment-14921785"
  [cm]
  (if (.somethingSelected cm)
    (.indentSelection cm)
    (let [n (.getOption cm "indentUnit")
          spaces (apply str (repeat n " "))]
      (.replaceSelection cm spaces))))

(def editor-opts
  {:mode "clojure-parinfer"
   :matchBrackets true
   :extraKeys {:Tab on-tab
               :Shift-Tab "indentLess"}})

(defn create-editor!
  "Create a parinfer editor."
  ([el] (create-editor! el nil))
  ;; codemirror opts vs. parinfer opts
  ([el {:keys [parinfer-mode]
        :or {parinfer-mode :indent-mode}
        :as opts}]
   (let [cm (js/CodeMirror.fromTextArea el (clj->js (merge editor-opts opts)))]

     (parinferize! cm parinfer-mode)

     cm)))

(def text (atom nil))

(defonce cm1 (atom nil))
(defonce cm2 (atom nil))

(defn render-dev! []
  (let [el1 (js/document.getElementById "code-indent-mode")
        el2 (js/document.getElementById "code-paren-mode")]

    (swap! cm1 (fn [cm]
                 (if cm (.toTextArea cm))
                 (create-editor! el1)))
    (swap! cm2 (fn [cm]
                 (if cm (.toTextArea cm))
                 (create-editor! el2 {:parinfer-mode :paren-mode})))

    (.on @cm1 "change" (fn [cm change]
                         (when (not= "setValue" (.-origin change))
                           (reset! text (.getValue cm))
                           (.setValue @cm2 (.getValue cm)))))
    (.on @cm2 "change" (fn [cm change]
                         (when (not= "setValue" (.-origin change))
                           (reset! text (.getValue cm))
                           (.setValue @cm1 (.getValue cm)))))))

(render-dev!)
