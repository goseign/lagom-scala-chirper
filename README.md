```
sbt runAll

# create user Alice
curl -X POST http://localhost:9000/api/users -d '{"userId":"alice","name":"Alice","friends":[]}'

# create user Bob with friend Alice
curl -X POST http://localhost:9000/api/users -d '{"userId":"bob","name":"Bob","friends":["alice"]}'

# Bob follows Alice (eventually)
curl -X GET http://localhost:9000/api/users/alice/followers

# Alice adds friend Bob
curl -X POST http://localhost:9000/api/users/alice/friends -d '{"friendId":"bob"}'

# Alice follows Bob (eventually)
curl -X GET http://localhost:9000/api/users/bob/followers

# create user Carl with friend Bob
curl -X POST http://localhost:9000/api/users -d '{"userId":"carl","name":"Carl","friends":["bob"]}'

# Carl follows Bob (eventually)
curl -X GET http://localhost:9000/api/users/bob/followers

# Alice adds friend Carl
curl -X POST http://localhost:9000/api/users/alice/friends -d '{"friendId":"carl"}'

# Alice follows Carl (eventually)
curl -X GET http://localhost:9000/api/users/carl/followers
```