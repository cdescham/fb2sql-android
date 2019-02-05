(c) Inventivelink SARL 2019

Firebase to SQL Wrapper for Android.

dependencies : API Platform DB

to run unit tests : 

adb shell am instrument -w -r   -e debug true -e class 'inventivelink.com.fb2sql.SQLDatabaseInstrumentedTestYoomumEntities' inventivelink.com.fb2sql.test/android.support.test.runner.AndroidJUnitRunner





- Notes : push() is always used in conjonction with .key, and the associated DB reference is never used. 
so push.getKey() have been replaced by SQLDatabase.getInstance().generateKey()


- addGeoQueryEventListener : returns 


TODO CD : 
virer dumpStackTrace et les cdes, 
flag migrated
firebase active
curl -X GET -H "X-Auth-Token: 4UFsaVeqleSIMbvRqBfnlZ0mnE" "http://127.0.0.1:8000/api/feed_likes?feed=-LX4-dHvnrjToNRnEuOR&mum=Hdr6l2EqmWgi3kKr1nSMiQJiHKn1" -H "accept: application/ld+json"
clean the helper

Entités -> pas touchées au final (juste ajout)
normalizes / de font du flat soit updateChildren soir updateChild soit setValue pas recursif

Phase 2: 
curl -X POST "http://192.168.1.99:8000/api/friends" -H "accept: application/ld+json" -H "Content-Type: application/ld+json" -d "{\"friendId\":\"8fns6RaiORaByLBsOzYUbHyEbRv2\",\"mumId\":\"8fns6RaiORaByLBsOzYUbHyEbRv2\",\"state\":\"accepted\"}"

curl -X POST "http://192.168.1.99:8000/api/mums" -H "accept: application/ld+json" -H "Content-Type: application/ld+json" -d "{ \"mumId\": \"1234\", \"location\": { \"latitude\": 0, \"longitude\": 0 }}" -H "X-Auth-Token: 4UFsaVeqleSIMbvRqBfnlZ0mnE"
curl -X PUT "http://192.168.1.99:8000/api/mums/1234" -H "accept: application/ld+json" -H "Content-Type: application/ld+json" -d "{\"location\":{\"latitude\":43.7111452,\"l\":[],\"longitude\":7.1351481}}" -H "X-Auth-Token: 4UFsaVeqleSIMbvRqBfnlZ0mnE"

curl -X GET "http://192.168.1.99:8000/api/mums/9HWOdSP1h4X7HgR6axZwG73AIQ83" -H "X-Auth-Token: 4UFsaVeqleSIMbvRqBfnlZ0mnE" -H "accept: application/ld+json" -H "Content-Type: application/ld+json" 





curl -X POST "http://127.0.0.1:8000/api/mums" -H "accept: application/ld+json" -H "Content-Type: application/ld+json" -d "{ \"mumId\": \"1234\", \"location\": { \"latitude\": 0, \"longitude\": 0 }}" -H "X-Auth-Token: 4UFsaVeqleSIMbvRqBfnlZ0mnE"
curl -X PUT "http://127.0.0.1:8000/api/mums/1234" -H "accept: application/ld+json" -H "Content-Type: application/ld+json" -d "{\"location\":{\"latitude\":43.7111452,\"l\":[],\"longitude\":7.1351481}}" -H "X-Auth-Token: 4UFsaVeqleSIMbvRqBfnlZ0mnE"

