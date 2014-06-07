package nz.co.twirls.twitterreader.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



import android.util.JsonReader;

public class TwitterJSON {
	
	
	public static List<messages> readJsonStream(InputStream in) throws IOException {
	     JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
	     try {
	       return readMessagesArray(reader);
	       }
	      finally {
	       reader.close();
	     }
	   }

	   public static List<messages> readMessagesArray(JsonReader reader) throws IOException {
	     List<messages> messages = new ArrayList<messages>();

	     reader.beginArray();
	     while (reader.hasNext()) {
	       messages.add(readMessage(reader));
	     }
	     reader.endArray();
	     return messages;
	   }

	   public static messages readMessage(JsonReader reader) throws IOException {
     
	     
	     messages message = new messages();

	     reader.beginObject();
	     while (reader.hasNext()) {
	    	 String name = reader.nextName();
	       if (name.equals("createdAt")) {
	    	   message.created = reader.nextString();
	       } else if (name.equals("text")) {
	    	   message.text = reader.nextString();
	       } else if (name.equals("isRetweeted")) {
	    	   message.isRetweeted = reader.nextString();
	       } else {
	         reader.skipValue();
	       }
	     }
	     
	     reader.endObject();
	     return message;
	   }
	

}
