package com.example.contacts;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.RawContacts;

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
	public static boolean insertContact(ContentResolver contactAdder, String firstName, String mobileNumber) {
	    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	    ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI).withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null).withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
	    
	    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,firstName).build());
	    
	    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0).withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,mobileNumber).withValue(ContactsContract.CommonDataKinds.Phone.TYPE,Phone.TYPE_MOBILE).build());

	    try {
	        contactAdder.applyBatch(ContactsContract.AUTHORITY, ops);
	    } catch (Exception e) {
	    return false;
	    }
	return true;
	}
	public static void deleteContact(ContentResolver contactHelper, String number) {

	    ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
	    String[] args = new String[] { String.valueOf(getContactCursor(contactHelper, number)) };

	    ops.add(ContentProviderOperation.newDelete(RawContacts.CONTENT_URI).withSelection(RawContacts.CONTACT_ID + "=?", args).build());

	    try {
	        contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
	    } catch (RemoteException e) {
	        e.printStackTrace();
	    } catch (OperationApplicationException e) {
	        e.printStackTrace();
	    }
	}
}
