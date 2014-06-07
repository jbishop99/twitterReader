package nz.co.twirls.twitterreader.twitter;

import android.annotation.SuppressLint;

import java.util.List;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterConnection {
	
	Twitter twitter;

	public TwitterConnection() {
		
		//TwitterFactory twitterfactory = new TwitterFactory();
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	            .setOAuthConsumerKey("iCioTRuAKCGBNKs8hsmphcZvB")
	            .setOAuthConsumerSecret("v4Vkjd7KLfsU8LZhAJhnLAsSTuXcIvhCIaJLXnUrYRABv9iEj0")
	            .setOAuthAccessToken("50594572-YuQSiWwPs7gq4PUkG5ShaXMhdFEfbJKm8MNiuDjfq")
	            .setOAuthAccessTokenSecret("Cf4cfgZZtHQekWOGMvo7oPLbLhrApU21MiQGoEtciwhZJ")
	            .setJSONStoreEnabled(true);
	    
	    TwitterFactory tf = new TwitterFactory(cb.build());
	    twitter = tf.getInstance();
    
	}
	
	@SuppressLint("NewApi")
	public List<Status> RunQuery(String queryStr, int noOfItems) throws TwitterException {
	    
		Query query = new Query(queryStr);
	    QueryResult result = null;
		try {
			result = twitter.search(query);
/*		    for (Status status : result.getTweets()) {
		        System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
		    }*/
		} catch (TwitterException e) {
			e.printStackTrace();
			result = null;
		}
		List<Status> messageList = result.getTweets();
		
		messageList =  messageList.subList(0, noOfItems);
		
		return messageList;
		
	
	}
	
}


