# Consume quotas API. 

This project is an example of how to implement rate limit in different databases. 

## Technologies

In this project I am using:  
Spring boot application.
Mysql server. 
H2 database to represent elastic search database. 
Maven

## Patterns implements

To cover this rule: </br> 

During the day 9:00 - 17:00 (UTC) we use MySql as a source </br>
During the night 17:01 - 8:59 we use Elastic </br> 
<b>I implemented AbstractRoutingDataSource pattern from spring boot. <b>


To cover rate limit rule: </br> 

I implemented AOP approach, creating QuotaLimit annotation. </br> 
In my opinion, this is the fastest way to scale up this rule accross multiples controllers and methods. 



## Starting the Application

Run the `UserQuotasApplication` class
start dock-compose up -d to run mysql instance. 



## Testing
This project includes both unit tests and integration tests.

just execute it using maven or ide. 
obs: make sure to have mysql instance running. 




## API Documentation
The API documentation is generated using OpenAPI 3.0 and can be accessed at http://localhost:8080/swagger-ui.html and http://localhost:8080/v3/api-docs.

## H2 Console

In order to see and interact with your db, access the h2 console in your browser.
After running the application, go to:

http://localhost:8080/h2-console

Enter the information for the url, username, and password in the application.properties:

```yml
url: jdbc:h2:mem:test
username: sa 
password: password
```

## Improvements 

this project doesn't sync databases. </br>   
Possibles approachs to do it: </br> 
Create a  scheduler to run based on cron job to sync all databases. </br> 
Create a event first approach to insert on all intances. </br> 
Use a middleware to sync databases. 

