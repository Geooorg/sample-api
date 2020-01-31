# sample-api
Sample REST API for client management.

# How to run

Build the project as outlined in build.sh:

    ./gradlew bootJar
    docker build -t sample-api .


Adapt the application-local.yml for your needs.
The application comes with an InMemory-H2 database. In case you want to exchange this,
 please consider to add the corresponding JDBC driver in the build.gradle 

For running the application in a container, use the script "run.sh":

    docker run --rm -p 8080:8080 -e PROFILES=local sample-api
    
The server is started on localhost:8080    
      
# API description

Foreword: As there are still some TODOs, please use the 'local' profile. 
A Swagger documentation is planned.

## Client API

The root path is /v1/client. 
See also ClientApiCrudEndpointTest for examples.

### Listing all (enabeld) clients

GET /v1/client
.. Pagination is supported. Provide page and pageSize parameters, e.g.

    curl -X GET http://localhost:8080/v1/client?page=0&pageSize=50

### Get client by Id

GET /v1/client/search/id/{id}
.. or use the generic search (see below)

### Adding a client

POST /v1/client
.. with a JSON body containing a UpdateClientDto

### Updating a client

PUT /v1/client
.. with a JSON body containing a UpdateClientDto

### Deleting a client

DELETE /v1/client/{id}

### Searching for clients by multiple properties

- email and id will return unique search results
- all other properties are searched for using a "contains incasesensitive" search

POST /v1/client/search/generic
.. .. Pagination is supported. Provide page and pageSize parametere as described for listing clients


# API Authentication

An authentication using JWT is prepared but not tested, so it's DISABLED for the moment.

## Concept of API Users and permissions

For authentication there are APIUsers coming with username, password and a set of roles.
There are currently these roles:
- GUEST: can view only
- CLIENT_MANAGER: can edit clients

For API user management, there will be an additional role
- ADMIN: can edit API users and set their permissions


When starting the application, an API User (username: admin, password: admin) is created automatically. 
See InitialDataProvider for details. The user will be able to create, edit and delete API users and set there permissions,
i.e. setting their roles.

For the sake of completeness, this feature could use Spring Security. 

## Authentication using JWT

A user needs to login using the endpoint 

    POST /v1/login
    ... with username:≤username> password:<password>
    
The response contains the JWT.

The JWT needs to be transported in the *Authorization* header
 
When authentication is enabled, all requests on the client API (/v1/client) will be intercepted for a valid JWT.
If the JWT is not valid, the request will not be allowed.
Note: Currently the user's roles are not checked.        

# Open TODOs

Providing a working, high quality software with a mostly tested API was important than providing lots of features that were not tested enough.
From this perspective, the following TODOs (in this order) could be done next:

* Authentication / Access control:
    * Provide API to edit API users (setting permissions etc)
    * Discuss which roles should exist and which role should be bound to what permissions and allowed/denied actions
    
* Client API
    * Extend the search specification so all properties are supported
          
* History feature
    * Discuss the details of changes and the true business value of this feature before estimating the actual technical effort
     
* Miscellaneous
    * Extend unit tests (e.g. add unit test for searchClientsGeneric)