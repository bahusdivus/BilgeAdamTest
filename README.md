# Bilge Adam Test

## How to run

Just tun "./gradlew bootRun" command in the root directory of the project.

## Limitations

- I didn't use any database, so the data is not persistent.
- Everything is in memory, so it can fail if the dataset is too big. I've implemented line by line processing, so it
  should be fine for most cases and only fail if number of "speakers" is too big and it will create huge HashMaps.
- There is no authentication and authorization, monitoring, logging, etc. so it is not by any means production ready.
- There is no unit tests, only integration tests. For such simple project I think it is enough. If logic gets more
  complicated, unit tests should be added.

## Assumptions

- I assumed that there will be only one format of date
- Topic field looks like potential enum, but with only 3 examples in sample data I decided to leave it as String
- There is no validation for Speaker and Topic fields, so they can be any string, including empty string

## Test data

Got test data from [here](https://mockaroo.com/) and generated manually for failed test cases. 