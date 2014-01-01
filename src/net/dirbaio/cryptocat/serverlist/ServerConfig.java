package net.dirbaio.cryptocat.serverlist;

public class ServerConfig
{
	public long id; //The DB ID

    public String name = "Cryptocat server";

	public String server = "crypto.cat";
    public String conferenceServer = "conference.crypto.cat";
	public int port = 5222;

    public boolean useTls = true;
    public boolean allowSelfSignedCerts = false;
    public boolean useBosh = false;
    public String boshRelay = null;

    public String getDescription()
    {
        return server+":"+port;
    }
}
