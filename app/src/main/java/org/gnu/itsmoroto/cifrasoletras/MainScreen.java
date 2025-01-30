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
import android.content.res.Resources;
import android.os.LocaleList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

class MainScreen extends RelativeLayout {

    private RadioButton m_sequential, m_random;
    private RadioGroup m_orderGroup;
    private SeekBar m_timeBar;
    private TextView m_timeValue;
    private TextView mtimeLabel;
    private Spinner m_languages;
    private ArrayAdapter<String> m_langadapter;
    private Button mbeginButton;
    private Context mCurrContext;
    private MainActivity mMain;
    public MainScreen(Context c){
        super (c);
        init();
    }
    private void init (){
        inflate(getContext(), R.layout.main, this);
        mCurrContext = getContext();
        mMain = (MainActivity) mCurrContext;
        m_sequential = findViewById(R.id.orderSeq);
        m_random = findViewById(R.id.orderRand);
        m_orderGroup = findViewById(R.id.orderGroup);
        m_orderGroup.check(m_random.getId());
        m_timeBar = findViewById(R.id.timelimitBar);
        m_timeValue = findViewById(R.id.timeValue);
        mtimeLabel = findViewById(R.id.timelimitlabel);
        m_timeBar.setProgress(2);
        setTimeText(2);
        m_timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                                 @Override
                                                 public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                     setTimeText (progress);

                                                 }
                                                 @Override
                                                 public void onStopTrackingTouch(SeekBar s){

                                                 }
                                                 public void onStartTrackingTouch(SeekBar s){

                                                 }

                                             }

        );
        m_langadapter = new ArrayAdapter<String>(getContext(),
                R.layout.comboview);
        //android.R.layout.simple_dropdown_item_1line);
        m_languages = findViewById(R.id.langcombo);

        m_languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                changeLanguage (i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mbeginButton = findViewById(R.id.startButton);
    }


    private void setTimeText (int progress){
        int val = (progress * (m_timeBar.getWidth() - 2 * m_timeBar.getThumbOffset())) / m_timeBar.getMax();
        m_timeValue.setText("" + (progress+1)*30 + " sec.");
        m_timeValue.setX(m_timeBar.getX() + val + m_timeBar.getThumbOffset() / 2);
        //textView.setY(100); just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar
    }

    public boolean isOrderSeq (){
        if (m_orderGroup.getCheckedRadioButtonId() == m_sequential.getId())
            return true;
        return false;
    }

    public void setLanguages (HashMap<String, String> langs){
        Iterator<String> it = langs.keySet().iterator();
        int pos = 0;
        LocaleList locs = getResources().getConfiguration().getLocales();
        Locale curr = getResources().getConfiguration().getLocales().get(0);
        m_languages.setAdapter(m_langadapter);
        while (it.hasNext ()){
            String k = it.next();
            String l = langs.get(k);
            m_langadapter.add(l);
            if (l.equals(curr.getDisplayLanguage()))
                m_languages.setSelection(pos);
            pos++;
        }
    }

    public String getLang (){
        return  (String) m_languages.getSelectedItem();
    }
    public int getTimeout (){
        return (m_timeBar.getProgress() +1);
    }

    private void changeLanguage (int index){
        String loc = (String) m_languages.getItemAtPosition(index);
        mCurrContext = mMain.setLocaleContext(loc);
        changeLabels ();
    }

    private void changeLabels (){
        Resources r = mCurrContext.getResources();
        m_sequential.setText(r.getString(R.string.orderseq));
        m_random.setText(r.getString(R.string.orderand));
        mtimeLabel.setText(r.getString(R.string.timelimit));
        mbeginButton.setText(r.getString(R.string.begin));
    }
}
