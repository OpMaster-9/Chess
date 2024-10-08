import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class ChessTimer {
    private int minutes;
    private int seconds;
    private Timer timer;
    private boolean isRunning;

    public ChessTimer(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
        this.isRunning = false;

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
    }
    public void start() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
        }
    }
    public void stop() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
        }
    }
    private void tick() {
        if (seconds == 0) {
            if (minutes > 0) {
                minutes--;
                seconds = 59;
            } else {
                stop();
            }
        } else {
            seconds--;
        }
    }
    public String getFormattedTime() {
        return String.format("%02d:%02d", minutes, seconds);
    }
    public int getRemainingTime() {
        return minutes * 60 + seconds;
    }
    public void setTime(int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }
    public boolean isRunning() {
        return isRunning;
    }
}
