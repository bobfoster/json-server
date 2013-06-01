json-server
===========

A simple server that accepts JSON POSTs and allows querying the content with GET.

The server timestamps each JSON value it receives. Requests can specify results
within a certain date/time range.

POST
----

Every POST received is expected to contain a valid JSON value. Invalid JSON is
rejected with a 400; valid JSON is accepted with a 201.

All POSTed values are saved indefinitely (as far as the server knows).

GET
---

A simple naked '/' (just the host and port) will return "JSON Server"
with some other fixed text telling how to form queries.

The query portion of a URL begins with '?'.

A query starting with 'json=' indicates the query is over all JSON
values stored by the server, and only the JSON values.

If 'json=' is the first query subpart, the 'json=' may be omitted.

A query path starting with 'date=' indicates the query includes the
date and time when each object was received.

These may be combined, as,

    localhost:8081?date=2013-10,json=job

which means, "all JSON objects with an outer 'job' key that were received
in October, 2013.

Query results are always JSON arrays. There is no restriction on what kind of 
JSON values can be stored, so the array may be heterogenous.

For the 'date' form, the result is an array of arrays, of the form:

    [
      ["YYYY-MM-DDThh:mm:ssZ", value],
      ...
    ]

"YYYY-MM-DDThh:mm:ssZ" is the ISO 8601 format date and time the JSON value was
received, and value is the value, itself.

For the 'json' only form, the array has the format:

    [
      value,
      ...
    ]

The default array contents can be changed by means of a 'select' qualifier, e.g.,

    localhost:8081?date=2013-10,json=job,select=json

will return

    [
        { "job": value,
          ...
        },
        ...
    ]

Which is a date-restricted subset of the array that would be returned by

    localhost:8081?json=job
    
We will use this JSON value in query examples below:

{ "store": {
    "name": "Borders",
    "address": "761 Sunset Drive",
    "city": "San Francisco",
    "state": "CA",
    "book": [ 
      { "category": "reference",
        "author": "Nigel Rees",
        "title": "Sayings of the Century",
        "price": 8.95
      },
      { "category": "fiction",
        "author": "Evelyn Waugh",
        "title": "Sword of Honour",
        "price": 12.99
      },
      { "category": "fiction",
        "author": "Herman Melville",
        "title": "Moby Dick",
        "isbn": "0-553-21311-3",
        "price": 8.99
      },
      { "category": "fiction",
        "author": "J. R. R. Tolkien",
        "title": "The Lord of the Rings",
        "isbn": "0-395-19395-8",
        "price": 22.99
      }
    ],
    "bicycle": {
      "color": "red",
      "price": 19.95
    }
  }
}

Any unique key name used in a query may be used in a select clause. For example,

    ?store.book[{author}],select=author

Unless modified by select, a query always returns all of the values that matched
the query expression, to whatever depth.

### Queries

The query language is designed for retrieving a number of objects for
further processing by the receiver. It is not a complete programming
language.

Every query is over a hypothetical JSON object with the following format:

    [ 
      [ n, "YYYY-MM-DDThh:mm:ssZ", value],
      ...
    ]

where n is the sequence number of the value in the order received by the server,
"YYYY-MM-DDThh:mm:ssZ" is the ISO 8601 format date and time the value was
received, and value is the value that was received.

The * (asterisk) stands for any value. So, for example,

    *

or

    [*,*,*]

will return all data in the server, including the sequence numbers and timestamps.

If you only wish to get some of the data in a query, enclose the portion you want
to receive in parenthesis, e.g.,

    [*,*,(*)]

Inner parenthesis take precedence, or if you like, outer parenthesis are ignored, so

    [*,(*,(*))]

Is the same as the preceding example.

However logical this query form may be, it is awkward. Many queries are only interested
in the payload - the JSON values originally sent to the server. For shorthand,

    $expr

is interpreted as:

    [*,*,(expr)]


  
