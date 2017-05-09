# Verifying Equivalence of JVM Programs, Using `pequod`

 `pequod` is a utility that, given two programs, attempts to determine
 if they are equivalent. In particular,

 1. `pequod` takes two programs `P0` and `P1 represented as JVM
 bytecode classes, with designated entry methods with identical type
 signatures. The classes can be compiled from any source language.

 2. `pequod` attmepts to determine if `P0` and `P1` are equivalent, in
 the sense that for each vector of arguments `A`, if `P0` and `P1`
 both return a value, then the values are equal. `pequod` can take an
 optional explicit definition of equivalence, which you can use to
 specify, e.g., that `pequod` should only check equivalence over
 certain inputs.

For example, if `pequod` is given the two Java programs:

```java
public int
  addDigits0(int num) {
  int result = num -
    9 * ((num - 1) / 9);
  return result; }
```

```java
public int
  addDigits1(int num) {
  while (num > 9) {
    num = num / 10 +
      num % 10; }
  return num; }
}
```

And a definition of equivalence that specifies that they should only
be checked on inputs in which `num >= 0`, then `pequod` returns that
`addDigits0` is equivalent to `addDigits1` under this definition.

## Example Usage

QZ, DH: provide example command-line usage

## Implementation

`pequod` is implemented in Java. It uses several existing tools for
performing key tasks. In particular,

* `pequod` uses the [soot](https://sable.github.io/soot/) to translate a
  given JVM bytecode class into an intermediate representation.

* `pequod` uses the [Z3](https://github.com/Z3Prover/z3) automatic
  theorem prover to attempt to synthesize relational invariants that
  prove the equivalence of given programs, or to generate an input on
  which they are not equivalent.
   
## Limitations

If `pequod`, given programs `P0` and `P1` returns that they are (not)
equivalent, then `P0` and `P1` are indeed (not) equivalent. However,
`pequod` may not terminate on some pairs of programs. This limitation
is fundamental to any equivalence verifier.

The current public version of `pequod` has been made available
primarily for public experimentation, and does not support all
features of the JVM intermediate language. In particular, it does not
support:

* objects

* multiple procedures

These limitations are *not* fundamental. If you are interested in
extending `pequod` to support these features, we are happy to take
pull requests, or to collaborate!

## Technical References

The design of `pequod` is based on a novel technique for synthesizing
proofs of program equivalence as _product programs_. A technical
report of the techniques is available here:

Qi Zhou, David Heath, and William Harris. Completely Automated
Equivalence Proofs. [arxiv](TODO)
