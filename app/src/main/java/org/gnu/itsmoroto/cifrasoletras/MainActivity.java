/**
 * Cifras o letras.
 * Basado en el concurso de televisión.
 * Esta aplicación se encuentra bajo licencia GNU v3.0:
 * https://www.gnu.org/licenses/gpl-3.0.en.html#license-text
 *
 * © Paco Andrés
 */

package org.gnu.itsmoroto.cifrasoletras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FrameLayout m_container;
    private Main m_mainview;
    private View m_currview = null;
    private Gamescr[] m_games;
    private boolean m_sequential = false;
    private static Random m_generator = null;
    private static int m_timeout;
    HashMap<String, String> m_slocales;

    private Locale m_selectedlocale;
    static {
        m_timeout = 2;
    }
    private int ncurr;
    private MediaPlayer m_plyr;

    private OnBackPressedCallback mCB = new OnBackPressedCallback (true){

        @Override
        public void handleOnBackPressed() {
            System.exit(0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRandom();
        m_slocales = new HashMap<String, String>();
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        m_container = findViewById(R.id.container);
        m_mainview = new Main(this);
        m_games = new Gamescr[2];
        /*m_games[0] = new Cifrasgame(this);
        m_games[1] = new Letrasgame(this);*/
        m_selectedlocale = getResources().getConfiguration().getLocales().get(0);
        getLocales ();
        m_mainview.setLanguages(m_slocales);
        changeView(m_mainview);
        m_plyr = MediaPlayer.create(this, R.raw.final_alarm);

        getOnBackPressedDispatcher().addCallback(this, mCB);
    }

    public void playAlarm (){
       m_plyr.start();
    }
    private void getLocales (){
        //Resources r = getResources();
        Configuration c = getResources().getConfiguration();
        Context ctx1, ctx2;
        String[] loc = getResources().getAssets().getLocales();
        c.setLocale(Locale.forLanguageTag(""));

        //r.updateConfiguration(c, null);
        ctx1 = createConfigurationContext(c);
        String s2 = ctx1.getResources().getString(R.string.translated);
        for (int i = 0; i < loc.length; i++) {
            //Log.d("LOCALE", i + ": " + loc[i]);

            //c.locale = new Locale(loc[i]);
            Locale curr = Locale.forLanguageTag(loc[i]);
            c.setLocale(curr);
            ctx2 = createConfigurationContext(c);
            //r.updateConfiguration(c, null);
            String s1 = ctx2.getResources().getString(R.string.translated);


            if (!s1.equals(s2)) {
                //Log.d("DIFFERENT LOCALE", i + ": " + s1 + " " + s2 + " " + loc[i]);
                if (m_slocales.containsKey(curr.getLanguage()))
                    continue;
                m_slocales.put(curr.getLanguage(), curr.getDisplayLanguage());
            }
        }
        //r.updateConfiguration(corig, null);
    }
    private static void initRandom (){
        if (m_generator == null)
            m_generator = new Random();
    }

    public static Integer getRandom (int bound){
        if (m_generator == null) {
            return -1;
        }
        return m_generator.nextInt(bound);
    }
    public static int getTimeout (){
        return m_timeout;
    }
    public void changeView (View newview){
        if (m_currview != null)
            m_container.removeView(m_currview);
        m_container.addView(newview);
        m_currview = newview;
    }

    public void onBegin (View button){
        String lang = m_mainview.getLang();
        m_sequential = m_mainview.isOrderSeq();
        m_timeout = m_mainview.getTimeout();
        setLocalteContext(lang);
        createGameScreens();
        ncurr = getRandom(2);
        m_games[ncurr].resetGame();
        changeView(m_games[ncurr]);

    }
    private Context getLocaleContext (){
        Context ctx;
        /*if (m_selectedlocale.equals(
                getResources().getConfiguration().getLocales().get(0)) ) {
            ctx = this;
        }
        else {*/

            Resources resources = getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(m_selectedlocale);

            ctx = getApplicationContext().createConfigurationContext(configuration);
        /*}
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }*/
        return ctx;

    }

    private void createGameScreens (){
        Context ctx = getLocaleContext();
        m_games[0] = new Cifrasgame(ctx);
        m_games[0].setMain(this);
        m_games[1] = new Letrasgame(ctx);
        m_games[1].setMain(this);
    }

    public Context setLocalteContext (String loc){
        for (String k : m_slocales.keySet()) {
            if (loc.equals(m_slocales.get(k))) {
                m_selectedlocale = Locale.forLanguageTag(k);
                return getLocaleContext();
            }
        }
        return this;
    }
    public void nextGame (){
        if (!m_sequential) {
            ncurr = getRandom(2);
        }
        else{
            ncurr++;
            ncurr %= 2;
        }
        m_games[ncurr].resetGame();
        changeView(m_games[ncurr]);
    }

    public Locale getSelectedLocale (){
        return m_selectedlocale;
    }
}

class Main extends RelativeLayout {

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
    public Main (Context c){
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
        m_timeValue.setText("" + (progress+1) + " min");
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
        mCurrContext = mMain.setLocalteContext(loc);
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