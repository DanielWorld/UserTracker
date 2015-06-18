package namgyuworld.usertracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.namgyuworld.usertracker.UserTracker;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserTracker.getInstance(this).sendFirstRun();

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserTracker.getInstance(this).sendForeground("tag");

    }

    @Override
    protected void onPause() {
        super.onPause();
        UserTracker.getInstance(this).sendBackground("tag");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            UserTracker.getInstance(this).sendAction("tag");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
