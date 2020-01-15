# ToDo with Ktor

This is a demo project written in Kotlin with [Ktor](https://ktor.io). It is a to-do list backend with very basic functionality. It uses

* [H2](https://www.h2database.com/) for database
* [Exposed](https://github.com/JetBrains/Exposed) for database access
* [Flyway](https://flywaydb.org) for database schema migrations
* [Gson](https://github.com/google/gson) for Json serialization/deserialization

## API

### 1. General Information

ToDo API is RESTful. It consumes and produces Json data. There is no authorization or authentication. Providing a valid `userId` as a path parameter is enough, because why not? 🤷🏻

All successful responses will have `200 OK` status unless explicitly mentioned.

#### Errors

All handled errors return an error Json in following format with an HTTP status same as the value of `code` field.

```json
{
  "code": 400,
  "message": "Human readable error message"
}
```

### 2. Creating a ToDo Item

Creates a new to-do item with given data

#### Example Request

`title` is required, `details` is optional

```
POST /todo/{userId}

{
    "title": "Buy Milk",
    "details": "1 carton"
}
```

#### Example Response

A successful response will have `201 Created` status.

```
201 Created

{
    "id": 1,
    "userId": 1,
    "title": "Buy Milk",
    "details": "1 carton",
    "time": "2020-01-15T14:44:14Z"
}
```

### 3. Listing ToDo Items

Lists all to-do items for given user id

#### Example Request

```
GET /todo/{userId}
```

#### Example Response

Response payload will contain a Json array of all to-do items.

```
200 OK

[
  {
    "id": 1,
    "userId": 1,
    "title": "Buy Milk",
    "details": "1 carton",
    "time": "2020-01-15T14:44:14Z"
  }
]
```

### 4. Getting a ToDo Item

Gets a to-do item with given id

#### Example Request

```
GET /todo/{userId}/{id}
```

#### Example Response

Response payload will contain a Json object of the to-do item.

```
200 OK

{
    "id": 1,
    "userId": 1,
    "title": "Buy Milk",
    "details": "1 carton",
    "time": "2020-01-15T14:44:14Z"
}
```

### 5. Updating a ToDo Item

Updates a to-do item with given data

#### Example Request

All fields are optional.

```
PUT /todo/{userId}/{id}

{
    "title": "Buy Soy Milk",
    "details": "2 cartons"
}
```

#### Example Response

Response payload will contain a Json object of the to-do item.

```
200 OK

{
    "id": 1,
    "userId": 1,
    "title": "Buy Soy Milk",
    "details": "2 cartons",
    "time": "2020-01-15T16:37:23Z"
}
```

### 6. Deleting a ToDo Item

Deletes given to-do item

#### Example Request

```
DELETE /todo/{userId}/{id}
```

#### Example Response

```
200 OK

{}
```
