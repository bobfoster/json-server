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

The query portion of a URL begins with '?'. There may later be query subdivisions,
but for now there is no path portion of the URL.

Queries always return a JSON array as a UTF-8 string. If a query has no result,
it returns "[]". If there is a query syntax error, an error object is returned:

{
  "json-query-error": {
    "query": "query as received",
    "where": "     ^ indicates position",
    "what": "nature of error",
    ....
  }
}

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

NOTHING BELOW THIS IS IMPLEMENTED YET

For a simple example,

    localhost:8081?json=store

will return all objects in the store that have an outer key "store". When json is the
first query component, "json=" may be omitted.

    localhost:8081?store.name:"Borders"

will return at least the JSON value above.

From now on we will omit the host:port part of the URL in example queries.

    ?store.book.author:author?select=author

The use of a name instead of a literal after : binds the name to the value. The above
will return an array like:

    ["Nigel Rees","Eveln Waugh","Herman Melville","J. R. R. Tolkien"]

Dicey example:

    ?store?select=store.name:n,store.book.category?groupby=n
    
Vacuous array levels are omitted, so the result is:

    [ "Borders", [ "reference", "fiction" ] ]

If there were two stores, however, the result would be:

    [
      [ "Borders", [ "reference", "fiction" ] ]
      [ "Barnes & Noble", [ "reference", "fiction", "popup" ] ]
    ]

Query Components
----------------

Valid query components are:

    ?json=query (json= may be omitted if first)
    ?select=names (comma-separated list of names, which are the "columns" of each row)
    ?from=datetime (starting datetime)
    ?to=datetime (ending datetime)
    ?date=datetime (this date, may be partially specified to indicate a range)
    
