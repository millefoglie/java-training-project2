# Project 2: Text Processor

##Description

A simple application which parses texts (stored as epub books) into trees of sentences, words and punctuation marks and then swaps the first and the last words in each sentence.

## Tools used

- Maven
- EPUB library http://www.siegmann.nl
- Log4j2
- JUnit4
- SonarQube

## Things which could be improved

- The provided functionality is enough for most of books. However, in certain cases there may be needed some adjustments to be made in the parsing algorithm. In particular, one might have to tune regex'es and abbreviation detection. For ordinary literature there seem to be no serious issues.
- The provided tests are extremely basic and do not really cover anything. Althought, the application is workins, it would be better to add more sophisticated test suits.
