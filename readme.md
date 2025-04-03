### Sample Java Application

An example Jersey/JAX-RS REST API implementation.
- Exposes Upload API to upload list of students which will be saved to In-memory/MySQL/DynamoDB database as configured
- Average Grade API returns average grade of the students stored
- Top 5 students api returns top-5 students who scored highest grade.

###
Upload#1
```
[
{"id": "id-1", "name":"name-1", "score": 72},
{"id": "id-2", "name":"name-2", "score": 25},
{"id": "id-3", "name":"name-3", "score": 100},
{"id": "id-3", "name":"name-3", "score": 100}
]
```
Upload#1 Response

```
[
{"status": "SUCCESS", "count": 3},
{"status": "DUPLICATES", "count": 1}
]
```

Upload#3
```
[ {"id": "id-2", "name":"name-2", "score": 25},
{"id": "id-4", "name":"name-4", "score": 45},
{"id": "id-5", "name":"name-5", "score": 50}, 
{"id": "id-5", "name":"name-5", "score": 50},
{"id": "id-6", "name":"name-6", "score": 5}
]
```

Upload#2 Response
```
[
{"status": "SUCCESS", "count": 3},
{"status": "DUPLICATES", "count": 2}
]
```
Average grade API response
```{"average": 49.5, "totalStudent": 6}```

Top-5 Students API response
```
[
{"id": "id-3", "name":"name-3", "score": 100},
{"id": "id-1", "name":"name-1", "score": 72},
{"id": "id-5", "name":"name-5", "score": 50},
{"id": "id-4", "name":"name-4", "score": 45},
{"id": "id-2", "name":"name-2", "score": 25}
]
```
