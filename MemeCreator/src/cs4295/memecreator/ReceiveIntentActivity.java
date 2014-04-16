package cs4295.memecreator;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReceiveIntentActivity extends Activity {
	
	private ReceiveIntentActivity selfRef;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receive_intent);
		
		selfRef = this;
		// Get the intent that started this activity
	    Intent intent = getIntent();
	    Uri imageURI = null;//intent.getData();
	    //useless, always return null
//	    Log.e("URI:", intent.getData() + ""); 
	    Log.e("URI information:", intent.getExtras().getParcelable(Intent.EXTRA_STREAM).toString());
	    if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND)) {
            Bundle extras = intent.getExtras();
            if (extras.containsKey(Intent.EXTRA_STREAM)) {
                imageURI = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
            }
        } else {
            imageURI = (Uri) intent.getParcelableExtra("image");
        }
	    String imagePath;
	    if(imageURI.toString().contains("file:///"))
	    {
	    	imagePath = imageURI.toString().substring(7);
	    	imagePath = imagePath.replace("%20", " ");
	    }
	    else
	    {
	    	String[] filePath = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(imageURI, filePath, null, null, null);
			cursor.moveToFirst();
			imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
			cursor.close();
	    }
	    Log.i("Path", imagePath);
		// Forward the image path to the next activity
		forwardImagePath(imagePath, MemeEditorActivity.class);
		finish();
	}

	// Method for forwarding a image path to the next class
	private void forwardImagePath(String imagePath, Class<?> targetClass) {
		// Put the image path to the intent with the variable name "cs4295.memcreator.imagePath"
		Intent forward = new Intent(selfRef, targetClass);
		forward.putExtra("cs4295.memcreator.imagePath", imagePath);
		startActivity(forward);
	}
}
