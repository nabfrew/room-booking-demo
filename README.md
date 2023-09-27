# Rest Api application for managing booking

A rest api application with some basic functionality for managing room bookings. 

## Test Prerequisites
The test database must be set up to run the tests. To use the database with the existing configuration, run the following commands.

``` sql
CREATE USER roombooking_test WITH PASSWORD 'roombooking'
CREATE DATABASE roombooking_test WITH OWNER roombooking_test
```
The tests are configured to use this database, with a liquibase migration. This was necessary as I could not find any method that generates the schema on the fly that retains the no_overlapping_dates constraints. This could be streamlined with TestContainers, probably.

