import org.json.JSONArray;
import org.json.JSONObject;

public class Ticket {
	//Page through tickets when more than 25 are returned
	private static final int LIMIT = 25;

	public int printMultiTickets(JSONObject ticketsJSON, int pageNum){
		JSONArray arr = ticketsJSON.getJSONArray("tickets");
		int ticketCount = arr.length();
		int total_page = ticketCount / LIMIT;
		int flag=0;
		//two extreme situations, the first page and the last page. Taken care from.
		if(pageNum > total_page){
			pageNum = total_page;
			flag=1;
		}else if(pageNum < 1){
			pageNum = 1;
			flag=2;
		}
		switch (flag) {
			case 1:
				System.out.println("This is the last page! Enter 'prev' for previous page, enter 'menu' to return, enter 'quit' to exit the viewer.");
				return pageNum;
			case 2:
				System.out.println("This is the first page! Enter 'next' for next page, enter 'menu' to return, enter 'quit' to exit the viewer.");
				return pageNum;
			default:
				int ticketCounter = 0;
				int offset = (pageNum - 1)*LIMIT;
				for(int i = 0; i < LIMIT; i++){
					int index=i+offset;
					if(arr.getJSONObject(index).isNull("id")){
						break;
					}
					printTicket(arr.getJSONObject(index).getInt("id"),arr.getJSONObject(index).getString("subject"), arr.getJSONObject(index).getInt("requester_id"), arr.getJSONObject(index).getString("updated_at"));
					ticketCounter++;
				}
				System.out.println("Displaying " + ticketCounter + " tickets. The current page number is " + pageNum + " out of a total of " + total_page
						+ " pages. Enter 'next' for next page, enter 'prev' for previous page, enter 'menu' to return, enter 'quit' to exit the viewer.");
				return pageNum;
		}
	}

	//print one ticket
	public void printSingleTicket(JSONObject ticketsJSON){
		printTicket(ticketsJSON.getInt("id"), ticketsJSON.getString("subject"), ticketsJSON.getInt("requester_id"),
				ticketsJSON.getString("updated_at"));
	}
	
	public void printTicket(int id, String subject, int requesterID, String date){
		System.out.println("(id: "+id+")"+"Ticket with subject '"+subject+"' opened by "+requesterID+" on "+date);
	}
}
