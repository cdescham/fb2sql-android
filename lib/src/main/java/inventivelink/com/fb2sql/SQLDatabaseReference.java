/*************************************************************************
 *
 *  Copyright (c) [2009] - [2019] Inventivelink
 *  All Rights Reserved.
 *
 ************************************************************************/

package inventivelink.com.fb2sql;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.annotations.PublicApi;

public class SQLDatabaseReference {
    private String table = null;
    private String id = null;
    private String geoSearch = null;
    private String parameters = null;
    private Object pivotfield = null;


    public SQLDatabaseReference(String table) {
        this.table = table;
    }

    @PublicApi
    public SQLDatabaseReference child(@NonNull String child) {
        if (table == null)
            table = child;
        else
            id = child;
        return  this;
    }

    // Limited to simple query parameters
    private void addParameter(String key,Object value) {
        String toAdd = key+"="+value;
        parameters = parameters != null ? parameters + "&"+toAdd : toAdd;
    }

    @PublicApi
    public SQLDatabaseReference limitToFirst(Integer limit) {
        addParameter("itemsPerPage",limit);
        return  this;
    }

    @PublicApi
    public SQLDatabaseReference orderByChildAsc(Object field) {
        pivotfield = field;
        addParameter("order%5B"+field+"%5D","asc");
        return  this;
    }

    @PublicApi
    public SQLDatabaseReference orderByChildDesc(Object field) {
        pivotfield = field;
        addParameter("order%5B"+field+"%5D","desc");
        return  this;
    }


    @PublicApi
    public SQLDatabaseReference timestampStartAt(Long secondsSinceCocoaStarts) { // Cocoa Starts = 978307200L GMT: Monday, January 1, 2001 12:00:00 AM
        if (pivotfield == null) {
            SQLDatabaseLogger.error("timestampStartAt called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        addParameter(pivotfield+"%5Bafter%5D",secondsSinceCocoaStarts);
        return  this;
    }

    @PublicApi
    public SQLDatabaseReference timestampEndAt(Object value) {
        if (pivotfield == null) {
            SQLDatabaseLogger.error("endAt called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        addParameter(pivotfield+"%before%5D",value);
        return  this;
    }


    @PublicApi
    public SQLDatabaseReference equalTo(Object value) {
        if (pivotfield == null) {
            SQLDatabaseLogger.error("equalTo called but no column defined by orderByChildDesc prior to this call. ");
            return this;
        }
        addParameter(pivotfield.toString(),value);
        return  this;
    }


    @PublicApi
    public SQLDatabaseReference withinPerimeter(@NonNull Double latitude, @NonNull Double longitude, Integer distance, String unit) {
        geoSearch = "geo_search/"+latitude+"/"+longitude+"/"+distance+unit;
        return  this;
    }


    @PublicApi
    public Task<SQLDatabaseSnapshot> setValue(@Nullable Object object) {
        if (object == null)
            return  SQLApiPlatformStore.delete(table,id).getTask();
        else if (id == null)
            return  SQLApiPlatformStore.insert(table,object).getTask();
        else
            return  SQLApiPlatformStore.update(table,id,object).getTask();
    }

    @PublicApi
    public void addListenerForSingleValueEvent(@NonNull final SQLValueEventListener listener) {
        Task<SQLDatabaseSnapshot> source = SQLApiPlatformStore.get(table,id,geoSearch,parameters).getTask();
        source.addOnCompleteListener(new OnCompleteListener<SQLDatabaseSnapshot>() {
            @Override
            public void onComplete(final @NonNull Task<SQLDatabaseSnapshot> task) {
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
                            listener.onCancelled(new SQLDatabaseException(task.getException()));
                        }
                    });
            }
        });
    }
}