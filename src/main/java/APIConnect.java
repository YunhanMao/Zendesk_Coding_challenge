import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;


public class APIConnect {
	public JSONObject connect(boolean option, String id) throws IOException {
		//use properties to import the credentials and flexible to changes
		Properties pps = new Properties();
		pps.load(new FileInputStream("src/main/resources/credentials.properties"));
		String subdomain = pps.getProperty("subdomain");
		String email_address = pps.getProperty("email_address");
		String password = pps.getProperty("password");
		String str;
		if(option){
			str = "https://"+ subdomain +".zendesk.com/api/v2/tickets.json";
		}else {
			str = "https://"+ subdomain +".zendesk.com/api/v2/tickets/"+ id +".json";
		}
		JSONObject ticket;
		try {
			//connect the server and authorize
			URL url = new URL(str);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Authorization", Auth(email_address, password));
			conn.connect();
			InputStream is = conn.getInputStream();
			Scanner sc = new Scanner(is);
			sc.useDelimiter("\\A");
			String result = sc.hasNext() ? sc.next() : "";
			sc.close();
			ticket = new JSONObject(result);
		} catch (Exception e) {
			System.out.println("There isn't any tickets matching the input id!");
			return null;
		}
		return ticket;
	}
	//method for authorization to POST
	public String Auth(String email_address, String password){
		String userAuthentication = email_address + ":" + password;
		return "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userAuthentication.getBytes());
	}

	//two ways to format JSON(Multiple and Single)
	public JSONObject formatJSON(boolean option, JSONObject ticketsJSON){
		if(option) {
			JSONArray arr;
			arr = ticketsJSON.getJSONArray("tickets");
			String dateStr = "";
			for (int i = 0; i < arr.length(); i++) {
				try {
					JSONObject currTicket = arr.getJSONObject(i);
					dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(currTicket.getString("updated_at")).toString();
				} catch (ParseException e) {
					System.out.println("There is a parsing issue with date in the JSON.");
				}
				int fetchedID = arr.getJSONObject(i).getInt("requester_id");
				String formattedID = formatID(fetchedID);
				ticketsJSON.getJSONArray("tickets").getJSONObject(i).put("requester_id", formattedID);
				ticketsJSON.getJSONArray("tickets").getJSONObject(i).put("updated_at", dateStr);
			}
		}
		else{
			JSONObject ticket;
			ticket = ticketsJSON.getJSONObject("ticket");
			String dateStr;
			int fetchedID = ticket.getInt("requester_id");
			String formattedID = formatID(fetchedID);
			ticket.put("requester_id", formattedID);
			try {
				dateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(ticket.getString("updated_at")).toString();
			} catch (ParseException e) {
				System.out.println("There is a parsing issue with date in the JSON.");
				return null;
			}
			ticket.put("updated_at", dateStr);
		}
		return ticketsJSON;
	}
	//get formatted ID
	public String formatID(int requesterID){
		return Integer.toString(requesterID).replace("-", "");
	}


	public JSONObject getAllTickets() throws IOException {
		JSONObject ticket = connect(true, "");
		ticket = formatJSON(true,ticket);
		return ticket;
	}

	public JSONObject getSingleTicket(String inputID) throws IOException {
		JSONObject ticket = connect(false, inputID);
		ticket = formatJSON(false,ticket);
		return ticket;
	}
}
