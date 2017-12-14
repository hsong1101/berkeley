(define (compose-all funcs)
	(define (fn funcs arg)
		(cond 
			((null? funcs) arg)
			(else (fn (cdr funcs) ((car funcs) arg)))
		))
	(lambda (x) (fn funcs x)))



(define (deep-map fn s)
  ; empty list?, return `()
  ; if empty fn, return s
  ; is the element list?
  ; -> if not, apply fn and move on
  ; -> if yes, recur the list and move on to next

  (cond
  	((null? fn) s)
  	((null? s) `())
  	((list? (car s)) (cons (deep-map fn (car s)) (deep-map fn (cdr s))))
  	(else (cons (fn (car s)) (deep-map fn (cdr s))))
  	)
)
