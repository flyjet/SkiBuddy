package edu.sjsu.qi.skibuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageButton bt_login_facebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_login_facebook = (ImageButton)findViewById(R.id.button_login_facebook);
        //Action of Facebook Login Button
        bt_login_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Implement login with facebook account


                try{
                    Intent intent = new Intent(MainActivity.this, ActivityFragmentContainer.class);
                    startActivity(intent);
                }catch(Exception e){
                    Log.e(TAG,e.toString());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
