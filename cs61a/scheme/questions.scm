(define (caar x) (car (car x)))
(define (cadr x) (car (cdr x)))
(define (cdar x) (cdr (car x)))
(define (cddr x) (cdr (cdr x)))

; Some utility functions that you may find useful to implement.

(define (map proc items)
  (if (null? items) '()
    (cons (proc (car items)) (map proc (cdr items)))))

(define (cons-all first rests)
      (map (lambda (lst) (cons first lst)) rests))

(define (zip pairs)
  (cons (map car pairs) (cons (map cadr  pairs) nil)))


(define (enumerate s)
  (define (helper s index)
    (cond
      ((null? s) nil)
      (else (cons (cons index (cons (car s) nil)) (helper (cdr s) (+ 1 index))))))
  (helper s 0))




(define (list-change total denoms)
  (cond
    ((null? denoms) cons nil)
    ((= total 0) cons(cons nil nil))
    ((> (car denoms) total) (list-change total (cdr denoms)))

  (else 
    (append 
      (cons-all (car denoms) (list-change (- total (car denoms)) denoms))
      (list-change total (cdr denoms))))))

(define (check-special form)
  (lambda (expr) (equal? form (car expr))))

(define lambda? (check-special 'lambda))
(define define? (check-special 'define))
(define quoted? (check-special 'quote))
(define let?    (check-special 'let))

;; Converts all let special forms in EXPR into equivalent forms using lambda
(define (let-to-lambda expr)
  (cond ((atom? expr)
         expr
         )
        ((quoted? expr)
         expr
         )
        ((or (lambda? expr)
             (define? expr))
         (let ((form   (car expr))
               (params (cadr expr))
               (body   (cddr expr)))
           (cons form (cons params (map let-to-lambda body)))))
        ((let? expr)
         (let ((values (cadr expr))
               (body   (cddr expr)))
           (cons (cons 'lambda (cons (car (zip values)) (map let-to-lambda body))) (map let-to-lambda (cadr (zip values))))))
        (else
         (cons (car expr) (map let-to-lambda (cdr expr))))))
