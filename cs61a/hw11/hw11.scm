(define (find s predicate)
  (cond
  	((null? s) #f)
  	((predicate (car s)) (car s))
  	(else (find (cdr-stream s) predicate))))


(define (scale-stream s k)
  (cons-stream (* k (car s)) (scale-stream (cdr-stream s) k)))


(define (has-cycle-h slow-ls fast-ls)
  (cond
   ((null? fast-ls) #f)
   ((eq? slow-ls fast-ls) #t)
   ((null? (cdr-stream fast-ls)) #f)
   ((null? (cdr-stream slow-ls)) #f)
   (else (has-cycle-h (cdr-stream slow-ls) (cdr-stream (cdr-stream fast-ls))))))

(define (has-cycle s)
  (cond
    ((null? s) #f)
    (else (has-cycle-h s (cdr-stream s)))))


(define (has-cycle-constant s)
  'YOUR-CODE-HERE
)
