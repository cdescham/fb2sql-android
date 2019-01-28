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