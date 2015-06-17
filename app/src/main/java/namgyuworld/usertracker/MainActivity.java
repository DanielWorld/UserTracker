package namgyuworld.usertracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.namgyuworld.usertracker.UserTracker;

public class MainActivity extends AppCompatActivity {

    UserTracker tracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracker = new UserTracker(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tracker.sendForeground("Foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        tracker.sendBackground("Background");
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
            tracker.sendAction("Menu Clicked");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
