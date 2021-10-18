# Basic Online Book Retail Service
It is a backend service developed by using SpringBoot and MongoDB.
It runs on an embedded Tomcat via port 8080.

#### Firstly, you need to create Bearer token to send request ```/authenticate``` endpoint, then use created Bearer token to send request other controller endpoints. Otherwise, UnAuthorized exception is returned
#### Bearer token will expiry after 3 hours

## Controllers
* AuthenticationController (Create Bearer token)
* CustomerController (Create new customer, Query customer's orders)
* BookController (Create new book, Update book stock)
* OrderController (Create new Order, Query order detail, Query orders by date interval)
* StaticsController (Query customer's monthly statics data) 

### AuthenticationController
* Create Bearer token
  ``` java
  @PostMapping
  public ResponseEntity<?> generateAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
  ...
  }
  ```
  Must Request
  ``` http request
  POST http://localhost:8080/authenticate
  ```
  With Body
  ``` json
  {
    "username": "ysnerdogdu",
    "password": "12345."
  }
  ```
  Sample Response
  ``` json  
  {
    "jwtToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5c25lcmRvZ2R1IiwiaWF0IjoxNjM0NTcyMjI5LCJleHAiOjE2MzQ1ODMwMjl9.ps8rcyg4KuSDXS4uHoD2LjeWREiy9iPDrJ-CIa0k-F0"
  }
  ```
### CustomerController
* Create customer endpoint
  ``` java
  @PostMapping("/create")
  public ResponseEntity<CustomerDto> createNewCustomer(@RequestBody CustomerAddRequest customerAddRequest){
  ...
  }
  ```
  Sample request body
  ``` json
  {
    "email": "test@gmail.com",
    "password": "12345",
    "firstName": "Joe",
    "lastName": "Black",
    "phone": "+9054* 333 ** 34",
    "adress": "Çankaya/Ankara"
  }
  ```
* Fetch customer's orders endpoint
  ``` java
   @GetMapping("/{id}/orders")
   public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable String id,
                                                            @PageableDefault(size = 20) Pageable pageable) {
   ...
   }
  ```
  Sample request
  ``` http request
  GET http://localhost:8080/customer/{customerId}/orders?page=0&size=1
  ```

### BookController
* Add or Update Book
  ``` java
  @PostMapping("/create")
  public ResponseEntity<BookDto> createBook(@RequestBody BookAddRequest bookAddRequest){
  ...
  }
  ```
  Sample Request
  ``` http request
  POST http://localhost:8080/books/create
  ```
  ``` json
  {
    "name": "Test Book-1",
    "writer": "Test Writer1",
    "publishYear": 2021,
    "price": 10.0,
    "stock": 15
  }
  ```
* Update book stock endpoint
  ``` java
  @PutMapping ("/update/{bookId}")
  public ResponseEntity<BookDto> updateBook(@PathVariable String bookId,
                                              @RequestParam Integer stockCount){
  ...
  }
  ```
  Sample Request
  ``` http request
  PUT http://localhost:8080/books/update/{bookId}?stockCount=101
  ```

### OrderController
* Create new order
  ``` java
  @PostMapping("/create")
  public ResponseEntity<OrderDto> placeOrder(@RequestBody NewOrderRequest newOrderRequest) {
  ...
  }
  ```
  Sample Request
  ``` json
  {
    "customerId": "616c7cbbc8a251453abd0d06",
    "bookOrders": 
     [
      {"bookId": "616c747d1af93a468927e6a7", "count":2},
      {"bookId": "616c75301af93a468927e6a8", "count":1}
     ]
  }
  ```
* Fetch order detail endpoint
 ``` java
  @GetMapping("/{id}")
  public ResponseEntity<OrderDto> getOrder(@PathVariable String id) {
  ...
  }
  ```
Sample Request
  ``` http request
  GET http://localhost:8080/order/616c889e9af80c0b080c68a7
  ```
* Fetch orders by date interval
  ``` java
  @GetMapping()
  public ResponseEntity<List<OrderDto>> getOrders(@DateTimeFormat(iso = DATE) @RequestParam LocalDate startDate,
                                                    @DateTimeFormat(iso = DATE) @RequestParam LocalDate endDate) {
  ...
  }
  ```
  Sample Request
  ``` http request
  GET http://localhost:8080/order?startDate=2021-10-16&endDate=2021-10-18
  ```

### StaticsController
* Customer monthly statics endpoint
  ``` java
  @GetMapping("/{customerId}")
  public ResponseEntity<List<CustomerMonthlyStaticsDto>> getCustomerMonthlyStatics(@PathVariable String customerId) {
  ...
  }
  ```
 Sample Request
 ``` http request
  GET http://localhost:8080/statics/{customerId}
  ```

## Sample Postman SS
![img.png](img.png)


## Used Technologies and Libraries
* Java 11
* SpringBoot 2.4.4
* MongoDB
* Apache Maven
* Gson
* Lombok
* Mockito

### Compile
```
mvn clean
mvn install
```

### Run
```
mvn spring-boot:run
```
