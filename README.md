# Starling Technical Challenge

This is my submission for the Starling Technical Challenge. I have decided to build a web server to handle the round-up feature.

There are two endpoints:
1. GET /starling/feed/account/{accountUid}/outgoing/week
2. PUT /starling/account/{accountUid}/savings-goals/{savingsGoalUid}/add-money/round-up

The first endpoint is just to view all outgoing transactions from the provided week and the total amount that would be saved. 
This is just for convenience as I thought it would be nice to be able to what transactions are being considered, as well as the amount that would be saved

The second will perform the transfer of money to the provided savings account.

# Assumptions
- No complex auth required as stated in the challenge
- User has a Starling account (or access to a sandbox customer)
- User's Starling account has a primary account, savings goal account and sufficient funds
- User has their access token
- User can use the Starling API to get their own accountUid, and savingsGoalUid
- The only supported currency is GBP
- A week is 7 days
- Within a week of the specified startTimestamp, the transactions that are considered are
  - with a direction of "OUT" (outgoings)
  - with a spendingCategory that is NOT "SAVING"
  - that are settled
- When rounding up some amount, go to the nearest pound
  - If amount is at an exact pound, don't save anything

# Running
Running the app can be done by either building it from source or by creating a docker image
1. To build from source
- Clone the project
- cd into `/starling`
- run `./gradlew run`
- The logs should state where to access the server
- By default, this should be http://localhost:8080

2. To build a docker image locally
- Clone the project
- cd into `/starling`
- run `/gradlew jibDockerBuild` (may need `sudo` from this point forward)
- (optional) run `docker image ls` to view the image
- run `docker run -p 8080:8080 starling:1.0`
- Access at http://localhost:8080

# Project details
- Used Micronaut CLI to create project
```
mn create-app kiran.interview.starling --build=gradle --lang=java --test=junit --features=http-client,graalvm,openapi,swagger-ui --jdk=21
```
- Swagger docs available at /swagger-ui
  - Endpoints can be triggered there
  - Ensure to Authorize at the top of the page first

# Testing
Due to time constraints there is limited testing. 
The tests written cover the core functionality as I felt they were the most important.




