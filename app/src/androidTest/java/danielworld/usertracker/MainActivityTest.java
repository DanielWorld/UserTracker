package danielworld.usertracker;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.jayway.android.robotium.solo.Solo;

/**
 * Copyright (C) 2014-2015 Daniel Park, op7773hons@gmail.com
 * <p>
 * This file is part of UserTracker (https://github.com/NamgyuWorld)
 * Created by danielpark on 2015. 6. 29..
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private Solo solo;

    public MainActivityTest(){ super(MainActivity.class);}

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testSendTracking() throws Exception{
        Button btn = (Button) solo.getView(R.id.button);
        solo.clickOnView(btn);
        solo.waitForText("What ever", 1, 20000); // wait for 20 seconds

        assertEquals(1, 1);
    }
}
