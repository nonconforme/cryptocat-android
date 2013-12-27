package net.dirbaio.cryptocat.serverlist;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ServerTable
{

	// Database table
	public static final String TABLE_SERVERS = "servers";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SERVER = "server";
	public static final String COLUMN_CONFERENCE_SERVER = "conference_server";
	public static final String COLUMN_PORT = "port";
	public static final String COLUMN_USE_TLS = "use_tls";
	public static final String COLUMN_ALLOW_SELFSIGNED = "allow_selfsigned";

	public static final String[] ALL_COLUMNS = {
			COLUMN_ID,
			COLUMN_NAME,
			COLUMN_SERVER,
			COLUMN_CONFERENCE_SERVER,
			COLUMN_PORT,
			COLUMN_USE_TLS,
			COLUMN_ALLOW_SELFSIGNED
	};

	// Database creation SQL statement
	private static final String DATABASE_CREATE =
			"create table " + TABLE_SERVERS + "(" +
					COLUMN_ID + " integer primary key autoincrement, " +
					COLUMN_NAME + " text not null, " +
					COLUMN_SERVER + " text not null, " +
					COLUMN_CONFERENCE_SERVER + " text not null, " +
					COLUMN_PORT + " integer not null," +
					COLUMN_USE_TLS + " integer not null" +
					COLUMN_ALLOW_SELFSIGNED + " integer not null" +
					");";

	public static void onCreate(SQLiteDatabase database)
	{
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		Log.w(ServerTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVERS);
		onCreate(database);
	}
}