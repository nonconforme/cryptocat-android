package net.dirbaio.cryptocat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import net.dirbaio.cryptocat.serverlist.ServerConfig;
import net.dirbaio.cryptocat.service.CryptocatServer;
import net.dirbaio.cryptocat.service.CryptocatService;

import java.util.ArrayList;

public class JoinServerFragment extends BaseFragment
{

	private View rootView;
    private ListView serversListView;

	@Override
	public void onStart()
	{
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        rootView = inflater.inflate(R.layout.fragment_join_server, container, false);
        serversListView = (ListView) rootView.findViewById(R.id.servers);
        serversListView.setAdapter(new ServersAdapter(getActivity(), CryptocatService.getInstance().serverList.servers));
        serversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CryptocatService service = CryptocatService.getInstance();
                ServerConfig config = service.serverList.servers.get(position);
                if(service.getServer(config.server) != null)
                    Toast.makeText(getActivity(), "You're already connected to this server", Toast.LENGTH_SHORT).show();
                else
                {
                    CryptocatServer server = getService().createServer(config);
                    server.connect();
                    callbacks.onItemSelected(server.id, null, null);
                }
            }
        });
        serversListView.setLongClickable(true);
        serversListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

		return rootView;
	}

    private class ServersAdapter extends ArrayAdapter<ServerConfig>
    {

        private Context context;

        public ServersAdapter(Context context, ArrayList<ServerConfig> items)
        {
            super(context, 0, items);
            this.context = context;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = convertView;
            if (view == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_conversation, null);
            }

            ServerConfig item = (ServerConfig) getItem(position);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(item.name);

            TextView subtitle = (TextView) view.findViewById(R.id.subtitle);
            subtitle.setText(item.getDescription());

            ImageView icon = (ImageView) view.findViewById(R.id.image);
            icon.setImageResource(android.R.color.transparent);

            return view;
        }
    }
    @Override
    protected void onMustUpdateTitle(ActionBar ab)
    {
        ab.setTitle("Cryptocat");
        ab.setSubtitle(null);
    }
}
