package net.dirbaio.cryptocat.service;

import android.util.Log;
import net.dirbaio.cryptocat.ExceptionRunnable;
import net.dirbaio.cryptocat.R;
import net.java.otr4j.*;
import net.java.otr4j.session.SessionID;
import net.java.otr4j.session.SessionStatus;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import java.security.*;

/**
 * An OTR conversation.
 */
public class OtrConversation extends Conversation implements MessageListener, OtrEngineHost, OtrEngineListener
{
	public final MultipartyConversation parent;
    public final MultipartyConversation.Buddy buddy;
    Chat chat;

    private OtrPolicy otrPolicy;
    private SessionID otrSessionID;
    private OtrEngine otrEngine;

	public OtrConversation(MultipartyConversation parent, MultipartyConversation.Buddy buddy) throws XMPPException
	{
		super(parent.server, parent.nickname);
		this.parent = parent;
        this.buddy = buddy;
		this.id = buddy.nickname;
        this.me = parent.me;
	}

	@Override
	public void join()
	{
		if (getState() != State.Left)
			throw new IllegalStateException("You're already joined.");
		if (server.getState() != CryptocatServer.State.Connected)
			throw new IllegalStateException("Server is not connected");
		if (parent.getState() != State.Joined)
			throw new IllegalStateException("You haven't joined the chatroom");

        setState(State.Joining);

        otrPolicy = new OtrPolicyImpl(OtrPolicy.ALLOW_V2 | OtrPolicy.ERROR_START_AKE | OtrPolicy.REQUIRE_ENCRYPTION);
        otrSessionID = new SessionID("", "", "");
        otrEngine = new OtrEngineImpl(this);
        otrEngine.addOtrEngineListener(this);

		CryptocatService.getInstance().post(new ExceptionRunnable()
		{
			@Override
			public void run() throws Exception
			{
				try
				{
					server.notifyStateChanged();
					chat = parent.muc.createPrivateChat(parent.roomName +"@"+parent.server.config.conferenceServer+"/"+buddy.nickname, OtrConversation.this);

                    setState(State.Joined);
                    otrEngine.startSession(otrSessionID);
				}
				catch(Exception e)
				{
					e.printStackTrace();

                    setState(State.Error);
				}
			}
		});
	}

	@Override
	public void leave()
	{
		if (getState() == State.Left)
			throw new IllegalStateException("You have not joined.");

		final Chat chatFinal = chat;
		CryptocatService.getInstance().post(new ExceptionRunnable()
		{
			@Override
			public void run() throws Exception
			{
				chatFinal.removeMessageListener(OtrConversation.this);
			}
		});

		chat = null;

        setState(State.Left);
	}

	@Override
	public void sendMessage(final String msg) throws OtrException {
		//Check state
		if (getState() != State.Joined)
			throw new IllegalStateException("You have not joined.");

        if(otrEngine.getSessionStatus(otrSessionID) != SessionStatus.ENCRYPTED)
        {
            addMessage(new CryptocatMessage(CryptocatMessage.Type.Error, "", "Encrypted session hasn't been established yet."));
        }
        else
        {
            addMessage(new CryptocatMessage(CryptocatMessage.Type.MessageMine, nickname, msg));
            sendRawMessage(otrEngine.transformSending(otrSessionID, msg));
        }
	}

	@Override
	public void processMessage(Chat chat, final Message message)
	{
		CryptocatService.getInstance().uiPost(new ExceptionRunnable() {
            @Override
            public void run() throws Exception {
                String txt = message.getBody();
                String plaintext = otrEngine.transformReceiving(otrSessionID, txt);
                if (plaintext != null)
                {
                    //I'm sometimes receiving empty messages. Not sure if it's desktop cryptocat's
                    //fault or mine. This fixes it.
                    if(!isWhitespace(plaintext))
                        addMessage(new CryptocatMessage(CryptocatMessage.Type.Message, buddy.nickname, plaintext));
                }
            }
        });
	}

    private static boolean isWhitespace(String s)
    {
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c != ' ' && c != '\t' && c != '\n')
                return false;
        }
        return true;
    }

	@Override
	public String toString()
	{
		return "[" + getState() + "] " + parent.roomName +":"+buddy.nickname;
	}

    private void sendRawMessage(final String msg)
    {
        CryptocatService.getInstance().post(new ExceptionRunnable() {
            @Override
            public void run() throws Exception {
                chat.sendMessage(msg);
            }
        });
    }

    @Override
    public void injectMessage(SessionID sessionID, String s) {
        sendRawMessage(s);
    }

    @Override
    public void showWarning(SessionID sessionID, String s) {
        addMessage(new CryptocatMessage(CryptocatMessage.Type.Error, "", "OTR Warning: " + s));
    }

    @Override
    public void showError(SessionID sessionID, String s) {
        addMessage(new CryptocatMessage(CryptocatMessage.Type.Error, "", "OTR Error: " + s));
    }

    @Override
    public OtrPolicy getSessionPolicy(SessionID sessionID) {
        return otrPolicy;
    }

    @Override
    public KeyPair getKeyPair(SessionID sessionID) {
        return me.otrKeyPair;
    }

    @Override
    public String getTitle() {
        return buddy.nickname;
    }

    @Override
    public int getImage() {
        return R.drawable.ic_action_person;
    }

    @Override
    public void sessionStatusChanged(SessionID sessionID) {
        if(otrEngine.getSessionStatus(otrSessionID) == SessionStatus.ENCRYPTED)
            buddy.setOtrPublicKey(otrEngine.getRemotePublicKey(otrSessionID));
    }
}
