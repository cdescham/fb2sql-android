package inventivelink.com.fb2sql;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class SQLDatabaseReference {
    private String table = null;
    private String id = null;

    public SQLDatabaseReference(String table) {
        this.table = table;
    }

    public SQLDatabaseReference child(@NonNull String child) {
        if (table == null)
            table = child;
        else
            id = child;
        return  this;
    }

    public Task<SQLDataSnapshot> setValue(@Nullable Object object) {
        if (object == null)
            return  SQLRepo.delete(table,id).getTask();
        else if (id == null)
            return  SQLRepo.insert(table,object).getTask();
        else
            return  SQLRepo.update(table,id,object).getTask();
    }

    public void addListenerForSingleValueEvent(@NonNull final SQLValueEventListener listener) {
        Task<SQLDataSnapshot> source = SQLRepo.get(table,id).getTask();
        source.addOnCompleteListener(new OnCompleteListener<SQLDataSnapshot>() {
            @Override
            public void onComplete(final @NonNull Task<SQLDataSnapshot> task) {
                if (task.isSuccessful())
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDataChange(task.getResult());
                        }
                    });
                else
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onCancelled(new SQLDatabaseError(task.getException()));
                        }
                    });
            }
        });
    }
}


/**
 * Set the data at this location to the given value. Passing null to setValue() will delete the
 * data at the specified location. The native types accepted by this method for the value
 * correspond to the JSON types:
 *
 * <ul>
 *   <li>Boolean
 *   <li>Long
 *   <li>Double
 *   <li>String
 *   <li>Map&lt;String, Object&gt;
 *   <li>List&lt;Object&gt;
 * </ul>
 *
 * <br>
 * <br>
 * In addition, you can set instances of your own class into this location, provided they satisfy
 * the following constraints:
 *
 * <ol>
 *   <li>The class must have a default constructor that takes no arguments
 *   <li>The class must define public getters for the properties to be assigned. Properties
 *       without a public getter will be set to their default value when an instance is
 *       deserialized
 * </ol>
 *
 * <br>
 * <br>
 * Generic collections of objects that satisfy the above constraints are also permitted, i.e.
 * <code>Map&lt;String, MyPOJO&gt;</code>, as well as null values.
 *
 * @param value The value to set at this location or null to delete the existing data
 * @return The {@link Task} for this operation.
 */

//

// READ
//        N/A (FB)  getReference(CONVERSATIONS_NODE).child(conversationId).addListenerForSingleValueEvent(
//        reference.child(EVENTS_NODE).child(eventId).addListenerForSingleValueEvent(
//        reference.child(EVENT_COMMENTS_NODE).child(eventId).addListenerForSingleValueEvent(
//        reference.child(FEED_NODE).child(feedId).addListenerForSingleValueEvent(
//        reference.child(LOCATIONS_NODE).child(addressId).addListenerForSingleValueEvent(
//        reference.child(LOCATION_COMMENTS_NODE).child(idToObserve)
//        reference.child(MUMS_LOCATION_NODE).child(mumId).addListenerForSingleValueEvent(

//          --> getFromTable(XXX).where(pk).equals(YYY) --> select * from XXX where fb_hash = YYY (fb_hash = firebase generated key)


//        getReference(CONVERSATIONS_NODE).orderByChild("participantIds").equalTo(participantIds).addListenerForSingleValueEvent(
//        getReference(LOCATIONS_NODE).orderByChild(PLACE_ID_CHILD).equalTo(placeId).addListenerForSingleValueEvent(
//        reference.child(EVENTS_NODE).orderByChild(START_DATE_FIELD).startAt(startDate).limitToFirst(GETTING_EVENT_STEP_COUNT+countDiff).addListenerForSingleValueEvent(
//        reference.child(EVENTS_NODE).orderByChild(TIMESTAMP_END_FIELD).startAt(now.toDouble()).limitToFirst(10).addListenerForSingleValueEvent(
//        reference.child(FEED_NODE).orderByChild(TIMESTAMP_END_FIELD).startAt(now.toDouble()).limitToFirst(10).addListenerForSingleValueEvent(
//        reference.child(FEED_NODE).orderByChild(TIMESTAMP_FIELD).endAt(startDate.toDouble()).limitToLast(itemToLoad+countDiff).addListenerForSingleValueEvent(
//        reference.child(MUMS_NODE).orderByChild(YOOMUM_ACCOUNT_NODE).equalTo(true).limitToFirst(1).addListenerForSingleValueEvent(valueEventListener)
//        reference.child(MESSAGES_NODE).child(conversationId).orderByChild("timestamp").startAt(dateToBeginObservation.toDouble(),"timestamp").addChildEventListener(observer.listener)
//        getReference(MESSAGES_NODE).child(conversationId).orderByChild("timestamp").endAt(endDate,"timestamp").limitToLast(NUMBER_OF_MESSAGES_PER_PAGE+countDiff).addListenerForSingleValueEvent(

//          --> getFromTable(XXX).where(field).equals(value).orderBy(field).limitToFirst(n)
//          --> getFromTable(XXX).where(field).equals(value).orderBy(field).limitToLast(n)
//          --> getFromTable(XXX).where(field).greaterthan(value).orderBy(field).limitToFirst(n)
//          --> getFromTable(XXX).where(field).lessthan(value).orderBy(field).limitToFirst(n)


//        reference.child(FEED_NODE).child(idToObserve).child(COMMENTS_NODE)
//        reference.child(LOCATIONS_GEOHASH_NODE).child(category.referencedNode)
//        reference.child(MUMS_LOCATION_NODE)
//        reference.child(MUMS_NODE).child(mumId).addListenerForSingleValueEvent(valueEventListener)
//        reference.child(MUMS_NODE).child(uid).child(CHILDREN_NODE)

//          --> getJoin(MUMS_NODE,CHILDREN_NODE,leftfield,rightfield).whereleft(field).equals(value)


// WRITE

//    TRANSAC
//        reference.child(MUMS_NODE).child(mumId).runTransaction(
//        reference.child(MUMS_NODE).child(mumId).runTransaction(
//        reference.child(MUMS_NODE).child(mumIdToReport).runTransaction(
//        reference.child(MUMS_NODE).child(requestedMumId).runTransaction(
//        reference.child(MUMS_NODE).child(requestedMumId).runTransaction(
//        reference.child(MUMS_NODE).child(uid).child(FRIENDS_NODE).runTransaction(
//        reference.child(MUMS_NODE).child(yoomumAccountIdentifier).runTransaction(
//        getReference(CONVERSATIONS_NODE).child(conversationId).child("unreadStatus").runTransaction(
//        getReference(CONVERSATIONS_NODE).child(conversationId).runTransaction
//        getReference(MUMS_NODE).child(currentMumId).child(FRIENDS_NODE).child(FRIENDS_ACCEPTED_NOTIFICATION_NODE).runTransaction(
//        getReference(MUMS_NODE).child(userId).child(EVENTS_NODE).runTransaction(
//        getReference(MUMS_NODE).child(mumId).child(CONVERSATIONS_NODE).runTransaction(
//        getReference(MUMS_NODE).child(mumId).child(LOCATIONS_NODE).runTransaction(object:Transaction(),Transaction.Handler
//        reference.child(FEED_NODE).child(feedIdentifier).runTransaction(
//        reference.child(MESSAGES_NODE).child(conversationId).child(messageId).child("read").runTransaction(
//        reference.child(MUMS_NODE).child(currentMumId).runTransaction(
//        getReference(EVENTS_NODE).child(eventKey).child(EVENT_PARTICIPANTS).runTransaction(


//   PUSH
//        getReference(CONVERSATIONS_NODE).push().key?.let{newConversationKey->
//        getReference(FEED_NODE).push().key?.also{newFeedItemKey->
//        getReference(FEED_NODE).push().key?.let{key->
//        getReference(MESSAGES_NODE).child(conversationId).push().key?.let{newMessageKey->

//   SETVALUE
//        reference.child(MUMS_NODE).child(userId).child(LAST_CONNECTION_TIMESTAMP).setValue(date)
//        reference.child(MUMS_NODE).child(uid).setValue(mum)
//        getReference(CONVERSATIONS_NODE).child(newConversationKey).setValue(newConversation).addOnCompleteListener{creationTask->
//        reference.child(LOCATIONS_NODE).child(id).child("photo").setValue(imageUrl)
//        getReference(EVENTS_NODE).child(eventKey).child(DELETED_FIELD).setValue(true).addOnCompleteListener{task->
//        getReference(EVENTS_NODE).child(eventKey).setValue(event).addOnCompleteListener{addEvenTask->
//        getReference(FEED_NODE).child(identifier).child(MESSAGE_NODE).setValue(message).addOnCompleteListener{creationTask->
//        getReference(FEED_NODE).child(key).setValue(feedItem).addOnCompleteListener{task->
//        getReference(FEED_NODE).child(messageId).setValue(null).addOnCompleteListener{creationTask->
//        getReference(FEED_NODE).child(newFeedItemKey).setValue(newFeedItem).addOnCompleteListener{creationTask->
//        getReference(MESSAGES_NODE).child(conversationId).child(newMessageKey).setValue(message).addOnCompleteListener{creationTask->
//        getReference(LOCATIONS_NODE).child(addressId).child(LOCATION_REMOVE_NODE).setValue(true).addOnCompleteListener{task->
//        reference.child(EVENTS_NODE).child(id).child("photo").setValue(imageUrl)


//   UPDATECHIILDREN
//        reference.updateChildren(updates).addOnCompleteListener{task->
//        reference.updateChildren(updates).addOnCompleteListener{updateTask->
//        reference.child(MUMS_NODE).child(id).updateChildren(map)
//        getReference(EVENTS_NODE).child(eventKey).updateChildren(updatesFields).addOnCompleteListener{updateEventTask->
//        reference.child(MUMS_NODE).child(mumId).updateChildren(interestMap)
//        reference.child(LOCATIONS_NODE).child(addressId).updateChildren(mapOf("reported"totrue)).addOnCompleteListener{task->callback(task.isSuccessful)
//        reference.child(EVENTS_NODE).child(eventId).updateChildren(mapOf("reported"totrue)).addOnCompleteListener{task->
//        getReference(LOCATIONS_NODE).child(addressId).updateChildren(updates)




// REALTIME READ
//        reference.child(CONVERSATIONS_NODE).child(conversationId).addValueEventListener(observer.listener)
//        getReference(MUMS_NODE).child(invitedId).child(EVENTS_EVENTS_INVITED_NODE).addValueEventListener(
//        reference.child(MUMS_NODE).child(mumId).child(CONVERSATIONS_NODE).addValueEventListener(observer.listener)
//        reference.child(MUMS_NODE).child(mumId).child(FRIENDS_NODE).child(nodeName).addValueEventListener(observer.listener)
