# Byte pair encoding

## Intuition

```text
Suppose the string to be encoded
aaabdaaabac
```

```text
The byte pair encoded result will be
ZabdZabac
```

```text
The replaceent string is Z = aa because "aa" is the most frequent pair.
```

The idea here is that we need to encode the an input text by replacing the most frequent pair of characters with a character that does not exist in the un-encoded input. The implementation in this repository is a parallelised implementation of this process. The parallelisation is done for CPUs with 4 cores.

Apparently this algorithm, or at least a slightly modified version is utilised in GPT-3.5 and GPT-4.0 <br>
Inspired by https://en.wikipedia.org/wiki/Byte_pair_encoding

## Parallelisation approach

```Java
// Packages
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
```

I split the initial input string into four chunks. Each chunk will be encoded in parallel, then merged together into one encoded string. 

## Example usage

```Java
public static void main(String[] args){
    BytePairParallelised bpe = new BytePairParallelised();
    String input = "abbabbaaabbabbaaabb";

    // This will be a set of all you allow for encoding
    HashSet<Character> charSet = new HashSet<>();

    String encodedString = bpe.encode(input, charSet);
    System.out.println(encodedString);
}
```
