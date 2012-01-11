Japanese Analysis for ElasticSearch
==================================

Japanese Analysis plugin integrates lucene-gosen module into elasticsearch.

In order to install the plugin, simply run: `bin/plugin -install suguru/elasticsearch-analysis-japanese/1.0.0`.

    --------------------------------------------------
    | Japanese Analysis Plugin | ElasticSearch  |
    --------------------------------------------------
    | master                   | 0.18 -> master |
    --------------------------------------------------
    | 1.0.0                    | 0.18 -> master |
    --------------------------------------------------

The plugin includes the `japanese` analyzer, `japanese_sentence` tokenizer, `japanese_basic_form`,`japanese_katakana_stem`,`japanese_part_of_speech_keep`,`japanese_part_of_speech_stop`,`japanese_punctuation` and `japanese_width` token filter.
