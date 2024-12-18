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
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class Gamescr extends RelativeLayout {

    protected MainActivity mActivity;
    public Gamescr(Context context) {
        super(context);
    }

    public Gamescr(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Gamescr(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected final void setMain (MainActivity activity){
        mActivity = activity;
    }

    public abstract void resetGame ();

    protected void playEnd (){
        if (!BuildConfig.DEBUG)
            ((MainActivity) getContext()).playAlarm();
    }
}
