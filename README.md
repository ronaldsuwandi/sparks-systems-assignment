
future improvements
- streaming operation
- special treatment for symbol source  ?


Important assumption
- data is kept in memory. Assuming it’s a backend and multiple client sending filter
- So can’t simply discard data - therefore single pass won’t do


TODO
- limit as post-filter approach (e.g. limit only first X result or limit only top X items per symbol)
- for z score / outlier - post input (e.g. calculate average and )
    - this could be part of the input processing -> calculate running average and stddev per symbol

outlier - uses online outlier algorithm (keeping track of mean+sum difference from mean) for efficiency 
and this can be adapted for streaming approach if needed (using windowing approach)


remaining
- input handling + file handling
- e2e test
- diagrams 


apache commons - to make file comparison code simpler + ignore carriage return type
