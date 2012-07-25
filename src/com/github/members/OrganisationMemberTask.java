package com.github.members;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.github.helper.AppStatus;
import com.github.helper.Constants;
import com.github.organisation.OrganisationRepositoryActivity;
import com.github.rest.RestClient;

public class OrganisationMemberTask extends AsyncTask<String, Void, String> {

	private OrganisationRepositoryActivity context;
	AppStatus mAppStatus;
	private String strOrganisation;

	Handler mhandler;

	public String getStrOrganisation() {
		return strOrganisation;
	}

	public void setStrOrganisation(String strOrganisation) {
		this.strOrganisation = strOrganisation;
	}

	public OrganisationMemberTask(OrganisationRepositoryActivity context, String organisation) {
		this.context = context;
		this.strOrganisation = organisation;
		mAppStatus = new AppStatus();
	}

	@Override
	protected String doInBackground(String... organisation) {
		// TODO Auto-generated method stub

		String strJsonReponse = null;

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);

		params.add(new BasicNameValuePair(Constants.AUTH_KEY, mAppStatus.getSharedStringValue(Constants.AUTH_KEY)));

		params.add(new BasicNameValuePair("organization", organisation[0]));

		try {
			if (mAppStatus.isOnline(context))
				strJsonReponse = RestClient.getInstance(context).doApiCall(Constants.strOrganisationMember, "GET",
						params);
			else
				context.dismissDialog(0);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			context.dismissDialog(0);
			e.printStackTrace();
		}

		return strJsonReponse;

	}

	@Override
	protected void onPostExecute(String strJsonReponse) {

		Log.i("STRJSON RESPONSE::::", String.valueOf(strJsonReponse));

		if (strJsonReponse.equals("[]")) {
			context.dismissDialog(0);
			Log.i("JSON RESPONSE::::", "Data not found...!!");
			Toast.makeText(context, "No repository", Toast.LENGTH_SHORT).show();

		} else {
			/* RepositoryActivity' activity */
			context.dismissDialog(0);
			context.memberResponse(strJsonReponse);
		}
	}
}
