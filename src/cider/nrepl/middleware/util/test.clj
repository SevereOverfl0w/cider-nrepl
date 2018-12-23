(ns cider.nrepl.middleware.util.test
  (:require
   [lambdaisland.deep-diff :as deep-diff]
   [lambdaisland.deep-diff.printer :as deep-diff.printer]))

(defn printer
  "Extracts relevant keys from an nrepl message for creating a printer."
  [msg]
  (let [{:keys [ansi-supported print-right-margin]
         :or {ansi-supported false}} msg]
    (deep-diff.printer/puget-printer
      (cond->
        {:print-color ansi-supported}
        print-right-margin
        (assoc :width print-right-margin)))))

(defn pprint-str
  [x printer]
  (with-out-str
    (deep-diff/pretty-print x printer)))

(defn diffs-result
  "Convert diffs data to form appropriate for transport."
  [diffs printer]
  (map (fn [[a diff]]
         [(pprint-str a printer)
          (pprint-str diff printer)])
       diffs))
