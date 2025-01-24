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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class TheActivity extends AppCompatActivity {
    private FrameLayout m_container;
    private MainScreen m_mainview;
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
    private SoundPool m_plyr;
    private int m_finalsnd;
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
        m_mainview = new MainScreen(this);
        m_games = new Gamescr[2];
        /*m_games[0] = new Cifrasgame(this);
        m_games[1] = new Letrasgame(this);*/
        m_selectedlocale = getResources().getConfiguration().getLocales().get(0);
        getLocales ();
        m_mainview.setLanguages(m_slocales);
        changeView(m_mainview);

        AudioAttributes
                audioAttributes
                = new AudioAttributes
                .Builder()
                .setUsage(
                        AudioAttributes
                                .USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(
                        AudioAttributes
                                .CONTENT_TYPE_SONIFICATION)
                .build();
        m_plyr
                = new SoundPool
                .Builder()
                .setMaxStreams(1)
                .setAudioAttributes(
                        audioAttributes)
                .build();

        m_finalsnd = m_plyr.load(this, R.raw.final_alarm, 1);
        getOnBackPressedDispatcher().addCallback(this, mCB);
    }

    public void playAlarm (){
        m_plyr.play(m_finalsnd, 1, 1, 0, 0, 1);
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
