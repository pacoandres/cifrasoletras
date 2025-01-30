/**
 * Cifras o letras.
 * Basado en el concurso de televisión.
 * Esta aplicación se encuentra bajo licencia GNU v3.0:
 * https://www.gnu.org/licenses/gpl-3.0.en.html#license-text
 *
 * © Paco Andrés
 */

package org.gnu.itsmoroto.cifrasoletras;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class Gamescr extends RelativeLayout {

    protected TheActivity mActivity;
    protected TextView m_timerText;
    protected  boolean m_finished;
    protected Button m_bnext;

    protected CountDownTimer m_timer;

    protected Context mLocaleContext;

    public Gamescr(Context context) {
        super(context);
        mLocaleContext = context;
    }

    public Gamescr(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLocaleContext = context;
    }

    public Gamescr(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLocaleContext = context;
    }

    protected final void setMain (TheActivity activity){
        mActivity = activity;
    }

    public abstract void resetGame ();

    protected abstract void showBegin ();

    protected void setLocaleContext (Context context){
        mLocaleContext = context;
    };
    protected void playEnd (){
        //if (!BuildConfig.DEBUG)
            mActivity.playAlarm();
    }
    protected final void setBtnNext (){
        m_bnext.setOnClickListener(v -> {
            if (!m_finished) {
                beginGame();
            }
            else {
                mActivity.stopAlarm();
                mActivity.nextGame();
            }
        });
    }
    protected void beginGame (){
        int timeout = BuildConfig.DEBUG ? 1: TheActivity.getTimeout() * 30 ;
        //int timeout = TheActivity.getTimeout() * 30 ;
        m_timerText.setText(Integer.toString(timeout));
        m_bnext.setEnabled(false);
        m_timer = new CountDownTimer(timeout* 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Integer timeout = (int) millisUntilFinished/1000 + 1;
                m_timerText.setText(timeout.toString());
            }

            @Override
            public void onFinish() {
                m_timerText.setText("0");
                playEnd ();
                m_bnext.setText(mLocaleContext.getResources().getString(R.string.next));
                m_bnext.setEnabled(true);
                m_finished = true;
            }
        };
        m_timer.start();
    }
}
