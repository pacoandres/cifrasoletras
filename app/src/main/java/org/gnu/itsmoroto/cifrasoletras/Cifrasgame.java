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
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Cifrasgame extends Gamescr{
    public Cifrasgame(Context context) {
        super(context);
        init ();
    }

    private Button m_bunit, m_bdecade, m_bnext;
    private final Integer [] m_decades = {10, 25, 50, 75, 100};

    private TextView m_cifrasText, m_resulText, m_timerText;
    private int m_ncifras;

    private CountDownTimer m_timer;
    private boolean m_finished;
    private final int m_total = 6;
    private void  init (){
        inflate(getContext(), R.layout.cifras_game, this);
        m_bunit = findViewById(R.id.buttonUnidad);
        m_bdecade = findViewById(R.id.buttonDecena);
        m_cifrasText = findViewById(R.id.cifrasText);
        m_resulText = findViewById(R.id.resulText);
        m_timerText = findViewById(R.id.timerCifText);
        m_bnext = findViewById(R.id.buttonCifNext);
        m_bunit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUnidad();
            }
        });

        m_bdecade.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDecena();
            }
        });
        m_bnext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                if (!m_finished) {
                    beginGame();
                }
                else{
                    ((MainActivity) getContext()).nextGame();
                }
            }
        });
        resetGame();
    }
    @Override
    public void resetGame() {
        m_cifrasText.setText("");
        m_resulText.setText("");
        m_timerText.setText("");
        m_bnext.setVisibility(INVISIBLE);
        m_bunit.setEnabled(true);
        m_bdecade.setEnabled(true);
        m_ncifras = 0;
        m_finished = false;
    }

    public void onUnidad (){
        Integer val = MainActivity.getRandom (9);
        setCifras(val+1);
    }

    public void onDecena (){
        Integer val = m_decades[MainActivity.getRandom (4)];
        setCifras(val);
    }
    private void setCifras (Integer val){
        String nums = m_cifrasText.getText().toString();
        if (m_ncifras >0)
            nums += ", ";
        nums += val.toString();
        m_cifrasText.setText(nums);
        m_ncifras++;
        if (m_ncifras == m_total)
            showBegin ();
    }

    private void showBegin (){

        m_bnext.setVisibility(VISIBLE);
        m_bnext.setText(R.string.begin);
        m_bunit.setEnabled(false);
        m_bdecade.setEnabled(false);
        m_resulText.setText(MainActivity.getRandom(1000).toString());
    }

    private void beginGame (){
        Integer timeout = MainActivity.getTimeout() * 60;
        m_timerText.setText(timeout.toString());
        m_bnext.setEnabled(false);
        m_timer = new CountDownTimer(timeout*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Integer timeout = (int) millisUntilFinished/1000;
                m_timerText.setText(timeout.toString());
            }

            @Override
            public void onFinish() {
                playEnd ();
                m_bnext.setText(R.string.next);
                m_bnext.setEnabled(true);
                m_finished = true;
            }
        };
        m_timer.start();
    }


}
