package com.mobdeve.tighee.samplemysqliteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context: Context?) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    // The singleton pattern design
    companion object {
        private var instance: MyDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDbHelper? {
            if (instance == null) {
                instance = MyDbHelper(context.applicationContext)
            }
            return instance
        }
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(DbReferences.CREATE_TABLE_STATEMENT)
    }

    // Called when a new version of the DB is present; hence, an "upgrade" to a newer version
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(DbReferences.DROP_TABLE_STATEMENT)
        onCreate(sqLiteDatabase)
    }

    fun getAllContactsDefault(): ArrayList<Contact>  {
        val database: SQLiteDatabase = this.readableDatabase

        val c : Cursor = database.query(
                DbReferences.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            DbReferences.COLUMN_NAME_LAST_NAME + " ASC, " + DbReferences.COLUMN_NAME_FIRST_NAME + " ASC",
            null
        )

        val contacts : ArrayList<Contact>  = ArrayList()

        while(c.moveToNext()) {
            contacts.add(Contact(
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_LAST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_FIRST_NAME)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_NUMBER)),
                    c.getString(c.getColumnIndexOrThrow(DbReferences.COLUMN_NAME_IMAGE_URI)),
                    c.getLong(c.getColumnIndexOrThrow(DbReferences._ID))
            ))
        }

        c.close()
        database.close()

        return contacts
    }

    @Synchronized
    fun insertContact(c: Contact): Long {
        /*
         *  DONE: When an add returns to the MainActivity, we don't actually
         *        include the ID that was generated by the DB. To do so, modify
         *        the insertContact() method in the DbHelper to return the
         *        generated ID. Note where this method is used throughout the code
         *        HINT: db.insert() returns the ID of the row if successful.
         * */

        val database = this.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DbReferences.COLUMN_NAME_LAST_NAME, c.lastName)
        values.put(DbReferences.COLUMN_NAME_FIRST_NAME, c.firstName)
        values.put(DbReferences.COLUMN_NAME_NUMBER, c.number)
        values.put(DbReferences.COLUMN_NAME_IMAGE_URI, c.imageUri)

        // Insert the new row
        // Inserting returns the primary key value of the new row, but we can ignore that if we don’t need it
        val primaryKey = database.insert(DbReferences.TABLE_NAME, null, values)
        database.close()

        return primaryKey
    }

    fun updateContact(c: Contact) {
        val database = this.writableDatabase

        val values = ContentValues()

        values.put(DbReferences.COLUMN_NAME_LAST_NAME, c.lastName)
        values.put(DbReferences.COLUMN_NAME_FIRST_NAME, c.firstName)
        values.put(DbReferences.COLUMN_NAME_NUMBER, c.number)
        values.put(DbReferences.COLUMN_NAME_IMAGE_URI, c.imageUri)

        val selection = "${DbReferences._ID} = ?"
        val selectionArgs = arrayOf(c.id.toString())

        database.update(DbReferences.TABLE_NAME, values, selection, selectionArgs)
        database.close()
    }

    /*
     *  TODO: Create methods for deleting and editing of a contact. Then call these methods in the
     *        appropriate places in the application.
     * */
    fun deleteContact(id: Int) {
        val database = this.writableDatabase

        // Define 'where' part of query.
        val selection = DbReferences._ID + " LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        database.delete(DbReferences.TABLE_NAME, selection, selectionArgs)

        database.close()
    }


    private object DbReferences {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "my_database.db"

        const val TABLE_NAME = "contacts"
        const val _ID = "id"
        const val COLUMN_NAME_FIRST_NAME = "first_name"
        const val COLUMN_NAME_LAST_NAME = "last_name"
        const val COLUMN_NAME_NUMBER = "number"
        const val COLUMN_NAME_IMAGE_URI = "image_uri"

        const val CREATE_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME_FIRST_NAME + " TEXT, " +
                    COLUMN_NAME_LAST_NAME + " TEXT, " +
                    COLUMN_NAME_NUMBER + " TEXT, " +
                    COLUMN_NAME_IMAGE_URI + " TEXT)"

        const val DROP_TABLE_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}