package nz.co.twirls.twitterreader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import twitter4j.Status;
import twitter4j.TwitterException;
import nz.co.twirls.twitterreader.twitter.TwitterConnection;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends ListActivity {
	
	public TwitterConnection twitterconnect;
	ListView listView1;
	int memClass;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if (android.os.Build.VERSION.SDK_INT > 8) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
	                .permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	    }
		
		memClass = ( ( ActivityManager )this.getSystemService( Context.ACTIVITY_SERVICE ) )
				.getMemoryClass();
			
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView1 = (ListView) findViewById(android.R.id.list);
		
		twitterconnect = new TwitterConnection();
		 
		List<String> stringList = new ArrayList<String>();

		try {
			ArrayList<Status> messageList = new ArrayList<Status>(twitterconnect.RunQuery("SKYNZ", 15));
			for (Status status : messageList) {
				stringList.add(status.getText());
			}
			
			StatusListAdapter listAdapter = new StatusListAdapter(this, 
					 R.layout.item_tweet, messageList, memClass);
			 
			 listView1.setAdapter(listAdapter);
			
			
		} catch (TwitterException e) {

			e.printStackTrace();
		}
		 

		 
/*		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class StatusListAdapter extends ArrayAdapter<Status> {

        private ArrayList<Status> items;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public StatusListAdapter(Context context, int textViewResourceId, 
        		ArrayList<Status> items, int memClass) {
                super(context, textViewResourceId, items);
                this.items = items;
                
        		Builder builder = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.memoryCache(new LruMemoryCache(1024 * 1024 * memClass / 8))
				.threadPriority(Thread.MAX_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.threadPoolSize(5)
				.tasksProcessingOrder(QueueProcessingType.LIFO);
        		
        		ImageLoaderConfiguration config = builder.build();
        		
        		options = new DisplayImageOptions.Builder()
        		.cacheInMemory(true)
        		.imageScaleType(ImageScaleType.NONE)
        		.build();
        		
        		imageLoader = ImageLoader.getInstance();
        		imageLoader.init(config);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.item_tweet, null);
                }
                Status o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.textStatus1);
                        ImageView bt = (ImageView) v.findViewById(R.id.imageView1);
                        if (tt != null) {
                              tt.setText("Name: "+ o.getUser().getName() + " Status: "+ o.getText());
                        if(bt != null){
                        	// show The Image
                        	String imgURL = o.getUser().getProfileImageURL();
                        	
                        	imageLoader.displayImage(imgURL, bt, options, new ImageLoadingListener() {

								@Override
								public void onLoadingStarted(String imageUri,
										View view) {
									view.setBackgroundColor(color.background_dark);
									
								}

								@Override
								public void onLoadingFailed(String imageUri,
										View view, FailReason failReason) {
									view.setBackgroundColor(color.background_dark);
								}

								@Override
								public void onLoadingComplete(String imageUri,
										View view, Bitmap loadedImage) {
									// TODO Auto-generated method stub
									view.setVisibility(View.VISIBLE);
								}

								@Override
								public void onLoadingCancelled(String imageUri,
										View view) {
									view.setBackgroundColor(color.background_dark);
									
								}
                        		
                        		
                        		
                        	});
                        	
                        	/*
                        	
                        	
                        	Bitmap bm = imgCache.getBitmapFromMemCache(imgURL);
                        	if (bm == null) {
                        		AsyncTask<String, Void, Bitmap> dit = new DownloadImageTask(bt)
                        	            .execute(imgURL);
                        	} else {
                        		bt.setImageBitmap(bm);
                        	}*/
                        	}
                        }
                }

                
        
				return v;
}
	}
}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
/*	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}*/

 class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

