# Verifying Equivalence of JVM Programs, Using pequod

 `pequod` is a utility that, given two programs, attempts to determine
 if they are equivalent. In particular,

 1. `pequod` takes two programs `P0` and `P1` represented as JVM
 bytecode classes, with designated entry methods with identical type
 signatures. The classes can be compiled from any source language.

 2. `pequod` attmepts to determine if `P0` and `P1` are equivalent, in
 the sense that for each vector of arguments, if `P0` and `P1` both
 return a value, then the values are equal. `pequod` can take an
 optional explicit definition of equivalence, which you can use to
 specify, e.g., that `pequod` should only check equivalence over
 certain inputs.

### Proving equivalence

If `pequod` is given two programs are equivalent, then in many cases
it will report this fact. For example, if `pequod` is given the two
Java programs:

```java
public int addDigits0(int num) {
  int result = num - 9 * ((num - 1) / 9);
  return result; }
```

```java
public int addDigits1(int num) {
  while (num > 9) {
    num = num / 10 + num % 10; }
  return num; }
```

and a definition of equivalence that specifies that they should only
be checked on inputs in which `num >= 0`, then `pequod` returns that
`addDigits0` is equivalent to `addDigits1` under this definition.

### Falsifying equivalence

If `pequod` is given two programs that are not equivalent, then it
will always eventually report this fact. For example, if `pequod` is
given the Java method `diff_mag` that, given integers `m` and `n`,
returns the magnitude of the difference of `m` and `n`:


```java
public int diff_mag(int m, n) {
  int result = m - n;
  if (result < 0) result = -1 * result;
  return result; }
```

and the Java program `mag_diff` that, given integers `m` and `n`,
returns the difference of the magnitudes of `m` and `n`:

```java
public int mag_diff(int m, n) {
  if (m < 0) m = -1 * m;
  if (n < 0) n = -1 * n;
  return m - n; }
```

then `pequod` returns that the methods are not equivalent.

## Building pequod

In your local package directory, run the following commands:

```
$ ant clean
$ ant build
$ ant BuildJar
```

## Using pequod

In your local package directry, run the following commands:

```shell
$ cd jar
$ java -jar pequod.jar <folderName> <function1> <function2>
```

where `folderName` contains the class files that contain the two
methods, and `function1` and `function2` are the names of the target
methods.

## Implementation

`pequod` is implemented in Java. It uses several existing tools for
performing key program-analysis tasks. In particular,

* `pequod` uses the [`soot`](https://sable.github.io/soot/) to translate a
  given JVM bytecode class into an intermediate representation.

* `pequod` uses the [`z3`](https://github.com/Z3Prover/z3) automatic
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
support

* multiple methods

These limitations are *not* fundamental. If you are interested in
extending `pequod` to support these features, we are happy to take
pull requests, or to collaborate!

## Technical References

The design of `pequod` is based on a novel technique for synthesizing
proofs of program equivalence as _product programs_. A technical
report of the techniques is available here:

Qi Zhou, David Heath, and William Harris. Completely Automated
Equivalence Proofs. [arxiv](http://arxiv.org/abs/1705.03110)
