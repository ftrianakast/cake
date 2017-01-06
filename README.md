#
Cake Problems

## Exercise 1.5

Ben Bitdiddle has invented a test to determine whether the interpreter he is faced with is using applicative-order evaluation or normal-order evaluation. He defines the following two procedures:

    (define (p) (p))
    
    (define (test x y)
    (if (= x 0)
        0
        y))

Then he evaluates the expression

    (test 0 (p))

What behavior will Ben observe with an interpreter that uses applicative-order evaluation? What behavior will he observe with an interpreter that uses normal-order evaluation? Explain your answer. (Assume that the evaluation rule for the special form if is the same whether the interpreter is using normal or applicative order: The predicate expression is evaluated first, and the result determines whether to evaluate the consequent or the alternative expression.)


### Answer

Having in mind that:

1. applicative-order -> "evaluate the arguments and then apply"
2. normal-order -> "fully expand and then reduce"

With applicative order evaluation Ben will see the arguments get evalulated first. This will cause (p) to be called over and over again.

With normal-order evaluation the arguements to (test) will not be evaluated until needed. This will caused the number '0' to be returned from the (test 0 (p)) call as the if statement would evaluate to true, causing (p) to never actually be executed.


## Exercise 1.6

Alyssa P. Hacker doesn't see why if needs to be provided as a special form. ``Why can't I just define it as an ordinary procedure in terms of cond?'' she asks. Alyssa's friend Eva Lu Ator claims this can indeed be done, and she defines a new version of if:

    (define (new-if predicate then-clause else-clause)
        (cond (predicate then-clause)
        (else else-clause)))

Eva demonstrates the program for Alyssa:

    (new-if (= 2 3) 0 5)
    5

    (new-if (= 1 1) 0 5)
    0

Delighted, Alyssa uses new-if to rewrite the square-root program:

    (define (sqrt-iter guess x)
        (new-if (good-enough? guess x)
            guess
            (sqrt-iter (improve guess x)
                        x)))
                        
What happens when Alyssa attempts to use this to compute square roots? Explain.


### Answer
"if" is special in Lisp as the books says, meaning that the interpreter treats it special. It is not a function, so applicative order evaluation does not apply in this case.

Alyssa's new-if is a function, and the applicative
order says that the sqrt-iter recursion happens before the new-if has a chance to choose between true or false paths.

## Exercise 1.10

The following procedure computes a mathematical function called Ackermann's function.

    (define (A x y)
        (cond ((= y 0) 0)
              ((= x 0) (* 2 y))
              ((= y 1) 2)
              (else (A (- x 1)
                       (A x (- y 1))))))


What are the values of the following expressions?

    (A 1 10)
    (A 2 4)
    (A 3 3)

Consider the following procedures, where A is the procedure defined above:

    (define (f n) (A 0 n))
    (define (g n) (A 1 n))
    (define (h n) (A 2 n))
    (define (k n) (* 5 n n))

Give concise mathematical definitions for the functions computed by the procedures f, g, and h for positive integer values of n. For example, (k n) computes 5n2.

### Answers

What are the values of the following expressions?

    (A 1 10) --> 1024
    (A 2 4) --> 65536
    (A 3 3) --> 65536


(define (f n) (A 0 n)) -> 2*n

(define (g n) (A 1 n)) -> 2^n, demonstrating:

    (A (- 1 1) (A 1 (- n 1)))
    = (A 0 (A 1 (- n 1)))
    = (A 0 (g (n - 1)))
    = (f (g (n - 1)))
    ...
    = (f (f ... (f (g 1))))
    ~~~~~~~~~~~~
    n - 1
    = (f (f ... (f 2)))
    ~~~~~~~~~~~~
    n - 1
    = 2 * 2 * ... * 2
    ~~~~~~~~~~~~~~~
    n
    = 2^n

(define (h n) (A 2 n)) is equal to **2^(2^n)**, demonstrating:

    (A 2 n)
    = (A (- 2 1) (A 2 (- n 1)))
    = (A 1 (A 2 (- n 1)))
    = (g (A 2 (- n 1)))
    = (g (g (A 2 (- n 2))))
    ...
    = (g (g ... (g (A 2 1))))
    ~~~~~~~~~~~~
    n - 1
    = (g (g ... (g 2)))
    ~~~~~~~~~~~~
    n - 1
    = ((2^2)^2)^2...^2
    ~~~~~~~~~~
    n - 1
    = 2^(2^n)

## Exercise 1.34

Suppose we define the procedure

    (define (f g)
        (g 2))

Then we have

    (f square)
    4
    (f (lambda (z) (* z (+ z 1))))
    6

What happens if we (perversely) ask the interpreter to evaluate the combination (f f)? Explain.

### Answer

Expanding (f f) we will have:

    (f f)
    (f 2)
    (2 2)

This gives us Error; 2 is not a function


## Exercise 1.38

In 1737, the Swiss mathematician Leonhard Euler published a memoir De Fractionibus Continuis, which included a continued fraction expansion for e - 2, where e is the base of the natural logarithms. In this fraction, the Ni are all 1, and the Di are successively 1, 2, 1, 1, 4, 1, 1, 6, 1, 1, 8, .... Write a program that uses your cont-frac procedure from exercise 1.37 to approximate e, based on Euler's expansion.

### Answer

    (define (fractibus k)
        (cont-frac (lambda (x) 1)
                   (lambda (x)
                     (if (= (remainder x 3) 2)
                                (/ (+ x 1) 1.5)
                                1))
                   k))

## Exercise 1.41

Define a procedure double that takes a procedure of one argument as argument and returns a procedure that applies the original procedure twice. For example, if inc is a procedure that adds 1 to its argument, then (double inc) should be a procedure that adds 2. What value is returned by

    (((double (double double)) inc) 5)

### Answer

    (define (double f)
        (lambda (x) (f (f x))))

Evaluating:

    (((double (double double)) inc) 5)

We have:

    21

## Exercise 1.42

Let f and g be two one-argument functions. The composition f after g is defined to be the function x f(g(x)). Define a procedure compose that implements composition. For example, if inc is a procedure that adds 1 to its argument,

    ((compose square inc) 6)

### Answer

    (define (compose f g)
        (lambda (x) (f (g x))))
       
## Exercise 1.43

If f is a numerical function and n is a positive integer, then we can form the nth repeated application of f, which is defined to be the function whose value at x is f(f(...(f(x))...)). For example, if f is the function x x + 1, then the nth repeated application of f is the function x x + n. If f is the operation of squaring a number, then the nth repeated application of f is the function that raises its argument to the 2nth power. Write a procedure that takes as inputs a procedure that computes f and a positive integer n and returns the procedure that computes the nth repeated application of f. Your procedure should be able to be used as follows:

    ((repeated square 2) 5)
    
### Answer

    (define (compose f g)
        (lambda (x) (f (g x))))
    
    (define (repeated f n)
        (cond ((zero? n) (lambda (x) x))
              (else (compose f (repeated f (- n 1))))))
 
## Bonus
 
 
 For bonus point, tackle this - “you'll need to find out what language this is, first”. Let's model a set of integers to be a function Int -> Bool; applying that function to some integer tells us whether the integer is in the set or not.
  
    type Set = Int -> Bool
  
I would like to you implement function forall, which takes some predicate p, the set to be checked and computes whether all elements in the set satisfy the predicate p. Dueto the way the set is modelled, and Int represents *a lot* of numbers, and because we want the answer 'quickly', you will need to restrict the possible range, for example from -1000 to 1000.
Thus, the final exercise to implement the function
  
    forall :: (Int -> Bool) -> Set -> Bool
  
Feel free to parametrise the type Set with a type variable, if you like; and explore the implication of such type polymorphism.
 
### Answer
 
Bonus is implemented on /src/main/scala/Bonus