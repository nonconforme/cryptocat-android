package net.dirbaio.cryptocat.serverlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ServerDataSource
{

	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;

	public ServerDataSource(Context context)
	{
		dbHelper = new SQLiteHelper(context);
	}

	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public void insert(ServerConfig server)
	{
		ContentValues values = new ContentValues();
		values.put(ServerTable.COLUMN_NAME, server.name);
		values.put(ServerTable.COLUMN_SERVER, server.server);
		values.put(ServerTable.COLUMN_CONFERENCE_SERVER, server.conferenceServer);
		values.put(ServerTable.COLUMN_PORT, server.port);
		values.put(ServerTable.COLUMN_USE_TLS, server.useTls);
		values.put(ServerTable.COLUMN_ALLOW_SELFSIGNED, server.allowSelfSignedCerts);

		long insertId = database.insert(ServerTable.TABLE_SERVERS, null, values);
		server.id = insertId;
	}

	public void delete(ServerConfig server)
	{
		long id = server.id;
		database.delete(ServerTable.TABLE_SERVERS, ServerTable.COLUMN_ID + " = " + id, null);
	}

	public List<ServerConfig> getAllComments()
	{
		List<ServerConfig> servers = new ArrayList<ServerConfig>();

		Cursor cursor = database.query(ServerTable.TABLE_SERVERS, ServerTable.ALL_COLUMNS, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			ServerConfig server = cursorToServer(cursor);
			servers.add(server);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return servers;
	}

	private ServerConfig cursorToServer(Cursor cursor)
	{
		ServerConfig server = new ServerConfig();
		server.id = cursor.getLong(0);
		server.name = cursor.getString(1);
		server.server = cursor.getString(2);
		server.conferenceServer = cursor.getString(3);
		server.port = cursor.getInt(4);
		server.useTls = cursor.getInt(5)>0;
		server.allowSelfSignedCerts = cursor.getInt(6)>0;
		return server;
	}
}
