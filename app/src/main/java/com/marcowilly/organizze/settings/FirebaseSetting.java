package com.marcowilly.organizze.settings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class FirebaseSetting {

    private static FirebaseAuth auth;
    private static DatabaseReference database;

    public static DatabaseReference getDatabase(){
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    public static FirebaseAuth getAuth(){
        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
