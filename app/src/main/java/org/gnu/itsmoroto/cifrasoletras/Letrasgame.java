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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Letrasgame extends Gamescr {

    private static String[] m_consonant;
    private static int[] m_consonantf;
    private static String[] m_vocal;
    private static int[] m_vocalf;


    private TextView m_letras;
    private Button m_vocalb, m_consb;
    private int m_nletras;
    private final int m_total = 9;
    private int mVocalSub, mConsSub;
    private int m_voctotalFreq = 10000, m_constotalFreq = 10000;
    public Letrasgame(Context context) {
        super(context);
        init ();
    }

    private void init (){
        inflate(getContext(), R.layout.letras_game, this);



        m_timerText = findViewById(R.id.timerLetText);
        m_letras = findViewById(R.id.letrasText);
        m_vocalb = findViewById(R.id.buttonVocal);
        m_consb = findViewById(R.id.buttonCons);
        m_bnext = findViewById(R.id.buttonLetNext);




        m_vocalb.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick (View v){
                String letra = getVocal();
                setLetra(letra);
            }
        });
        m_consb.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick (View v){
                String letra = getCons();
                setLetra(letra);
            }
        });
        setBtnNext();
        resetGame();
    }

    @Override
    protected void setLocaleContext (Context c){
        super.setLocaleContext(c);
        m_consb.setText(mLocaleContext.getResources().getString(R.string.consonant));
        m_vocalb.setText(mLocaleContext.getResources().getString(R.string.vocal));
        m_bnext.setText(mLocaleContext.getResources().getString(R.string.begin));
        resetGame();
    }
    private void getFrequecies (){
        Context c = mLocaleContext;
        m_consonantf = c.getResources().getIntArray(R.array.conso_dist_freq);
        m_vocalf = c.getResources().getIntArray(R.array.voc_dist_freq);
        m_consonant = c.getResources().getStringArray(R.array.conso_dist);
        m_vocal = c.getResources().getStringArray(R.array.voc_dist);
        m_voctotalFreq = m_vocalf[m_vocalf.length -1];
        m_constotalFreq = m_consonantf[m_consonantf.length - 1];
        mVocalSub = m_voctotalFreq/m_vocalf.length;
        mConsSub = m_constotalFreq/m_consonantf.length;
    }
    private void setLetra (String letra){
        String letras = m_letras.getText().toString();
        if (m_nletras > 0)
            letras += ", ";
        letras += letra;
        m_letras.setText(letras);
        m_nletras++;
        if (m_nletras >= m_total)
            showBegin ();
    }

    @Override
    protected void showBegin (){
        m_vocalb.setEnabled(false);
        m_consb.setEnabled(false);
        m_bnext.setText(mLocaleContext.getResources().getString (R.string.begin));
        m_bnext.setVisibility(VISIBLE);
    }
    @Override
    public void resetGame() {
        int timeout = TheActivity.getTimeout() * 30;
        m_timerText.setText(Integer.toString(timeout));
        m_finished = false;
        getFrequecies();
        m_letras.setText("");
        m_bnext.setVisibility(INVISIBLE);
        m_vocalb.setEnabled(true);
        m_consb.setEnabled(true);
        m_nletras = 0;
    }

    private String getVocal (){
        int n = TheActivity.getRandom(m_voctotalFreq);
        String ret = m_vocal[m_vocal.length - 1];
        for (int i = 0; i < m_vocalf.length - 1; i++){
            if (n > m_vocalf[i] && n <= m_vocalf[i+1]) {
                ret = m_vocal[i + 1];
                int res = Math.min(m_vocalf[i+1] - m_vocalf[i], mVocalSub);
                for (int j = i+1; j < m_vocalf.length; j++)
                    m_vocalf[j] -= res;
                m_voctotalFreq -= res;
                mVocalSub = m_voctotalFreq/m_vocalf.length;
                return ret;
            }
        }
        return ret;
    }

    private String getCons (){
        int n = TheActivity.getRandom(m_constotalFreq);
        String ret = m_consonant[m_consonant.length - 1];
        for (int i = 0; i < m_consonantf.length - 1; i++){
            if (n > m_consonantf[i] && n <= m_consonantf[i+1]) {
                ret = m_consonant[i + 1];
                int res = Math.min(m_consonantf[i+1] - m_consonantf[i], mConsSub);
                for (int j = i+1; j < m_consonantf.length; j++)
                    m_consonantf[j] -= res;
                m_constotalFreq -= res;
                mConsSub = m_constotalFreq/m_consonantf.length;
                return ret;
            }

        }
        return ret;
    }



}
