package com.example.contacts;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactHelper {
	public static Cursor getContactCursor(ContentResolver contactHelper, String startsWith) {

	    String[] projection = { ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
	    Cursor cur = null;
	    
	    try {
	        if (startsWith != null && !startsWith.equals("")) {
	            cur = contactHelper.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like \"" + startsWith + "%\"", null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
	        } else {
	            cur = contactHelper.query (ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
	        }
	        cur.moveToFirst();
	    } 
	    catch (Exception e) {
	        e.printStackTrace();
	    }
	    return cur;
	}
}
