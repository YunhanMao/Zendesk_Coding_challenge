import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Tests {
	@Test
	//this is to test whether it will cause an exception using id out of range
	public void test1(){
		String id = "-1";
		APIConnect api = new APIConnect();
		try{
			api.connect(false, id);
		}catch(Exception e){
			assertEquals(e.getClass(), Exception.class);
		}
	}
	
	@Test
	//this is to test whether it will fetch the data and display it correctly.
	public void test2() throws IOException {
		String id = "1";
		APIConnect api = new APIConnect();
		JSONObject ticket = api.getSingleTicket(id);
		assertEquals(Integer.parseInt(id), ticket.get("id"));
	}
}
