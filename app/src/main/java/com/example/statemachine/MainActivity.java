package com.example.statemachine;



import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

// Context Class to manage the state transitions
class Context {
    private State state;
    private MainActivity mainActivity;

    public Context(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.state = new Idle(); // Initial state
        state.startTransitionTimer(this); // Start automatic state transition
    }

    public void nextState() {
        state.stopTransitionTimer(); // Stop the current state's transition timer
        state.nextState(this);
        state.startTransitionTimer(this); // Start automatic state transition for the next state
    }

    public void setState(State state) {
        this.state = state;
        mainActivity.updateUI(state.showState()); // Update the UI color based on the state
    }

    public String showState() {
        return state.showState();
    }
}

// Define your States

interface State {
    void nextState(Context context);
    String showState();
    void startTransitionTimer(Context context);
    void stopTransitionTimer();
}

class Idle implements State {
    private Timer transitionTimer;

    @Override
    public void nextState(Context context) {
        context.setState(new Ringing());
    }

    @Override
    public String showState() {
        return "Idle";
    }

    @Override
    public void startTransitionTimer(Context context) {
        transitionTimer = new Timer();
        transitionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                context.nextState();
            }
        }, 3000); // Change the time interval as needed
    }

    @Override
    public void stopTransitionTimer() {
        if (transitionTimer != null) {
            transitionTimer.cancel();
            transitionTimer = null;
        }
    }
}

class Ringing implements State {
    private Timer transitionTimer;

    @Override
    public void nextState(Context context) {
        context.setState(new Connected());
    }

    @Override
    public String showState() {
        return "Ringing";
    }

    @Override
    public void startTransitionTimer(Context context) {
        transitionTimer = new Timer();
        transitionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                context.nextState();
            }
        }, 5000); // Change the time interval as needed
    }

    @Override
    public void stopTransitionTimer() {
        if (transitionTimer != null) {
            transitionTimer.cancel();
            transitionTimer = null;
        }
    }
}

class Connected implements State {
    private Timer transitionTimer;

    @Override
    public void nextState(Context context) {
        context.setState(new Idle());
    }

    @Override
    public String showState() {
        return "Connected";
    }

    @Override
    public void startTransitionTimer(Context context) {
        transitionTimer = new Timer();
        transitionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                context.nextState();
            }
        }, 7000); // Change the time interval as needed
    }

    @Override
    public void stopTransitionTimer() {
        if (transitionTimer != null) {
            transitionTimer.cancel();
            transitionTimer = null;
        }
    }
}

// MainActivity Class

public class MainActivity extends AppCompatActivity {

    private FrameLayout stateContainer;
    private Context stateContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateContainer = findViewById(R.id.state_container);

        // Initialize state context
        stateContext = new Context(this);
    }

    // Method to update UI based on current state
    public void updateUI(String state) {
        runOnUiThread(() -> {
            switch (state) {
                case "Idle":
                    stateContainer.setBackgroundColor(Color.WHITE); // Idle = White
                    break;
                case "Ringing":
                    stateContainer.setBackgroundColor(Color.YELLOW); // Ringing = Yellow
                    break;
                case "Connected":
                    stateContainer.setBackgroundColor(Color.GREEN); // Connected = Green
                    break;
            }
        });
    }
}
