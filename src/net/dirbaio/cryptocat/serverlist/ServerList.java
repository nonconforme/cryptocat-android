package net.dirbaio.cryptocat.serverlist;

import android.content.Context;
import net.dirbaio.cryptocat.service.GsonHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerList
{
    public static final String SERVER_LIST_FILE = "servers.json";

    public ArrayList<ServerConfig> servers;

    public void save(Context context) throws IOException
    {
        String serialized = GsonHelper.customGson.toJson(servers);

        FileOutputStream fos = context.openFileOutput(SERVER_LIST_FILE, Context.MODE_PRIVATE);
        OutputStreamWriter out = new OutputStreamWriter(fos);
        out.write(serialized);
        out.close();
    }

    public void load(Context context)
    {
        try
        {
            FileInputStream fis = context.openFileInput(SERVER_LIST_FILE);
            InputStreamReader in = new InputStreamReader(fis);

            ServerConfig[] serverArray = GsonHelper.customGson.fromJson(in, ServerConfig[].class);
            servers = new ArrayList<>(Arrays.asList(serverArray));
        }
        catch (IOException e)
        {
            e.printStackTrace();

            //In case of error, or file not existing (first run)
            //populate the server list with the official server and that's it.
            servers = new ArrayList<>();
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
            servers.add(new ServerConfig());
        }
    }
}
