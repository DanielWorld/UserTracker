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

        UserTracker tracker = UserTracker.getInstance(this);

        tracker.sendFirstRun("1");
        tracker.sendFirstRun("2");
        tracker.sendFirstRun("3");
        tracker.sendFirstRun("4");
        tracker.sendFirstRun("5");
        tracker.sendAction("1");
        tracker.sendAction("2");
        tracker.sendAction("3");
        tracker.sendAction("4");
        tracker.sendAction("5");
        tracker.sendForeground("1");
        tracker.sendForeground("2");
        tracker.sendForeground("3");
        tracker.sendForeground("4");
        tracker.sendForeground("5");
        tracker.sendBackground("1");
        tracker.sendBackground("2");
        tracker.sendBackground("3");
        tracker.sendBackground("4");
        tracker.sendBackground("5");
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
