# SGT Student Gateway

Exposes a Student Rest API, acting as a proxy to the Student SOAP backend service.


## Requirements

- Implemented and tested on Java SE 8

- Major Dependencies 
-   JUnit 
-   Mockito
-   SpringBoot
-   SpringFox
-   Spring ws
-   JAXB

- Project dependencies managed by Maven

Deploy and run guidelines
-  Student SOAP backend service
      
     Steps:            
      
        1 - Copy .jar file called `student-service-1.0-SNAPSHOT.jar` located in the target folder of module `student-service`,
            to your designated folder where you want to run this application.
        2 - Open Windows Command prompt or Bash shell at your designated folder location
        3 - Run the following command "java -jar student-service-1.0-SNAPSHOT.jar"  {ignoring the quotes "}
        
    To verify that this service is up
            
        Open a Web browser with this url `http://localhost:8001/ws/students.wsdl`, if it is successful, a xml wsdl definition should be returned     

-  Student Rest Service
      
      Steps:   
               
        1 - Copy .jar file called `student-service-rest-1.0-SNAPSHOT.jar` located in the target folder of module `student-service-rest`,
            to a new designated folder where you want to run this application
        2 - Open Windows Command prompt or Bash shell at your designated folder location
        3 - Run the following command "java -jar student-service-rest-1.0-SNAPSHOT.jar"  {ignoring the quotes "}

    To verify that this service is up
            
        Open a another Web browser tab with this url `http://localhost:7001/sgt/gateway/swagger-ui.html`, if it is successful,
        a swagger-ui html page should appear, with API defintion     


**Challenges**
--
    Find a way to expose the new Rest API, in such a way as to make it easy for existing clients currently using the SOAP backend service to migrate from to this service.

    Allow this REST API service to be easily adaptable to changes made on the backend SOAP service  



**Sub modules**
--
* student-mapping 
   Module containing the pojos used by the student rest api.
    
        Generates the JAXB source from wsdl file, and applies jsr303 annotations to sources based on xsd schema constraints,
        thus enabling validation at a bean level, using javax.validation.constraints.
        Therefore enabling the use of the JAXB sources for the REST service
    
* student-service-gateway

    * student-service-rest
    
            Module containing Student Rest Service
     
            Uses springfox to expose the service api definition using swagger2
            See [http://localhost:7001/sgt/gateway/swagger-ui.html"]
        
            Usages spring @Valid [javax.validation.constraints] to validate input via Request Parameters, Path Variables and Request Body
            
* student-xsd-schema
        
        Module containing jaxb generates artifacts for backend SOAP service and client
         
* student-service
        
        Module containing the SOAP based Student backend service
        
* student-service-client
        
        Module containing client used to call the Student backend service
        
* student-xsd-schema
        
        Module containing 



SGT - Student API
----

**Get /students/id/{id}**
----
  Returns json data about a single student.

* **URL**

  /students/id/:id

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `id=[integer]`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{
                      "studentDetails": {
                          "id": 1,
                          "name": "John",
                          "passportNumber": "F54365"
                      }
                  }`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{
                    "error": "Not Found",
                    "errors": [
                      "Some more details highlighting error"
                    ],
                    "message": "Content Not Found",
                    "status": 400
                  }`

  * **Code:** 500 INTERNAL SERVER ERROR <br />
    **Content:** `{
                      "status": 500,
                      "error": "Internal Server Error",
                      "message": "Connection refused: connect",
                      "errors": [
                          "Some more details highlighting error"
                      ]
                  }`

* **Sample Call:**

  ```javascript
    curl -X GET "http://localhost:7001/sgt/gateway/students/id/1" -H "accept: application/json"
  ```
  
  **Get /students?year={year}**
  ----
    Returns json data about a list students.
  
  * **URL**
  
    /students?year
  
  * **Method:**
  
    `GET`
    
  *  **URL Params**
  
     None
  
  * **Data Params**
  
    **Required:**
    
    year=[positiveInteger]
  
  * **Success Response:**
  
    * **Code:** 200 <br />
      **Content:** `[
                        {
                            "id": 1,
                            "name": "John",
                            "passportNumber": "F54365"
                        },
                        {
                            "id": 2,
                            "name": "Peter",
                            "passportNumber": "A2356B"
                        },
                        {
                            "id": 3,
                            "name": "Sam",
                            "passportNumber": "G452249585"
                        }
                    ]`
   
  * **Error Response:**
  
    * **Code:** 400 BAD REQUEST <br />
      **Content:** `{
                      "error": "Bad Request",
                      "errors": [
                        "Some more details highlighting error"
                      ],
                      "message": "invalid request",
                      "status": 400
                    }`
  
    * **Code:** 500 INTERNAL SERVER ERROR <br />
      **Content:** `{
                        "status": 500,
                        "error": "Internal Server Error",
                        "message": "Connection refused: connect",
                        "errors": [
                            "Some more details highlighting error"
                        ]
                    }`
  
  * **Sample Call:**
  
    ```javascript
      curl -X GET "http://localhost:7001/sgt/gateway/students?year=2018" -H "accept: application/json"
    ```
    
    **POST /students**
      ----
        To create a single student
        Returns json data about the newly created student.
      
      * **URL**
      
        /students
      
      * **Method:**
      
        `POST`
        
      *  **URL Params**
      
         None
      
      * **Data Params**
      
         None
         
      * **Body**
         
         `[CreateStudentRequest]`
         
            `{
              "studentDetails": {
                "id": 3,
                "name": "John",
                "passportNumber": "55889977"
              },
              "year": 2018
            }`            
      
      * **Success Response:**
      
        * **Code:** 200 <br />
          **Content:** `{
                            "studentDetails": {
                                "id": 1256,
                                "name": "Sam",
                                "passportNumber": "D4858605"
                            }
                        }`
        
      * **Error Response:**
      
        * **Code:** 404 Bad Request <br />
          **Content:** `{
                          "error": "Bad Request",
                          "errors": [
                            "Some more details highlighting error"
                          ],
                          "message": "invalid request",
                          "status": 400
                        }`
                        
      * **Code:** 404 NOT FOUND <br />
          **Content:** `{
                          "error": "Not Found",
                            "errors": [
                                       "Some more details highlighting error"
                                      ],
                            "message": "Not found",
                            "status": 404
                         }`      
      
        * **Code:** 500 INTERNAL SERVER ERROR <br />
          **Content:** `{
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "Connection refused: connect",
                            "errors": [
                                "Some more details highlighting error"
                            ]
                        }`
      
      * **Sample Call:**
      
        ```javascript
          curl -X POST "http://localhost:7001/sgt/gateway/students" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"studentDetails\": { \"id\": 3456, \"name\": \"Sam\", \"passportNumber\": \"D346547\" }, \"year\": 2018}"
        ```
        
    **PUT /students**
      ----
        To create or update a single student
        Returns json data about the newly created or updated student.
      
      * **URL**
      
        /students
      
      * **Method:**
      
        `PUT`
        
      *  **URL Params**
      
         None
      
      * **Data Params**
      
         None
      
      * **Body**
         
         `[UpdateStudentRequest]`
                  
            `{
              "studentDetails": {
                "id": 3,
                "name": "John",
                "passportNumber": "55889977"
              },
              "year": 2018
             }`         
      
      * **Success Response:**
      
        * **Code:** 200 <br />
          **Content:** `{
                         "studentDetails": {
                           "id": 3,
                           "name": "John",
                           "passportNumber": "55889977"
                         },
                         "year": 2018
                         }`
        
      * **Error Response:**
      
        * **Code:** 404 Bad Request <br />
          **Content:** `{
                          "error": "Bad Request",
                          "errors": [
                            "Some more details highlighting error"
                          ],
                          "message": "invalid request",
                          "status": 400
                        }`
                        
      * **Code:** 404 NOT FOUND <br />
          **Content:** `{
                          "error": "Not Found",
                            "errors": [
                                       "Some more details highlighting error"
                                      ],
                            "message": "Not found",
                            "status": 404
                         }`      
      
        * **Code:** 500 INTERNAL SERVER ERROR <br />
          **Content:** `{
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "Connection refused: connect",
                            "errors": [
                                "Some more details highlighting error"
                            ]
                        }`
      
      * **Sample Call:**
      
        ```javascript
          curl -X PUT "http://localhost:7001/sgt/gateway/students" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"studentDetails\": { \"id\": 3, \"name\": \"John\", \"passportNumber\": \"55889977\" }, \"year\": 2018}"
        ```        
        
    **DELETE /students**
      ----
        To delete a single student
        Returns json data about the deleted student, if found and deleted.
      
      * **URL**
      
        /students
      
      * **Method:**
      
        `DELETE`
        
      *  **URL Params**
      
         None
      
      * **Data Params**
      
         None
      
      * **Body**
         
         `[DeleteStudentRequest]`
                  
            `{
              "studentDetails": {
                "id": 3,
                "name": "John",
                "passportNumber": "55889977"
              },
              "year": 2018
             }`         
      
      * **Success Response:**
      
        * **Code:** 200 <br />
          **Content:** `{
                         "studentDetails": {
                           "id": 3,
                           "name": "John",
                           "passportNumber": "55889977"
                         },
                         "year": 2018
                         }`
        
      * **Error Response:**
      
        * **Code:** 404 Bad Request <br />
          **Content:** `{
                          "error": "Bad Request",
                          "errors": [
                            "Some more details highlighting error"
                          ],
                          "message": "invalid request",
                          "status": 400
                        }`
                        
      * **Code:** 404 NOT FOUND <br />
          **Content:** `{
                          "error": "Not Found",
                            "errors": [
                                       "Some more details highlighting error"
                                      ],
                            "message": "Not found",
                            "status": 404
                         }`      
      * **Code:** 204 NO CONTENT FOUND <br />
          **Content:** `{ }`      
      
      * **Code:** 500 INTERNAL SERVER ERROR <br />
          **Content:** `{
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "Connection refused: connect",
                            "errors": [
                                "Some more details highlighting error"
                            ]
                        }`
      
      * **Sample Call:**
      
        ```javascript
          curl -X DELETE "http://localhost:7001/sgt/gateway/students" -H "accept: application/json" -H "Content-Type: application/json" -d "{ \"studentDetails\": { \"id\": 123, \"name\": \"Peter\", \"passportNumber\": \"1235\" }, \"year\": 0}"
        ```