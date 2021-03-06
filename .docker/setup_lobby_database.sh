#!/bin/bash

# TODO: use flyway to execute migration file, see: 'lobby-db/README.md'
# Creates the 'ta_users' database, used by lobby and then
# runs all schema updates in order (as found in the lobby/db directory).
#
# Note: prod and travis may deviate a bit here, travis
# is set up from scratch each time while in prod we would run the
# latest patch only.


DB_NAME="ta_users"
echo "create database $DB_NAME" | psql -h localhost -U postgres


# Run the sql from every file in 'lobby/db' in order by file name
while read migration_file; do
  psql -h localhost -U postgres -d $DB_NAME < "$migration_file"
done < <(find  lobby/db/ -type f | sort -n)
