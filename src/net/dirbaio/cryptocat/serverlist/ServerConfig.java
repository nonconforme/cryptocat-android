package net.dirbaio.cryptocat.serverlist;

public class ServerConfig
{
    public String name = "Cryptocat server";

	public String server = "crypto.cat";
    public String conferenceServer = "conference.crypto.cat";
	public int port = 5222;

    public boolean useTls = true;
    public boolean allowSelfSignedCerts = false;

    //NOT USED. BOSH IS BUGGY
    public boolean useBosh = false;
    public String boshRelay = "https://crypto.cat/http-bind";

    public String getDescription()
    {
        return server+":"+port;
    }
}
