/*
 * One Time Password, for use with S/KEY logins.
 */
package in.aotp;

import in.aotp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * The main activity.
 * 
 * TODO: Saved state, 
 * TODO: Forget password.
 */
public class OTPActivity 
    extends Activity 
    implements OnClickListener 
{
    
    static final String SAVED_RESP_KEY = "otp_resp";
        
    private TextView response_text;
    private TextView v_pass, v_seq, v_seed;
    private RadioGroup v_alg;
    private OTPParameters params;
    private String otp_resp;
    
    public OTPActivity()
    {
        params = new OTPParameters();
    }
 
    private String computeResponse(OTPParameters params) 
        throws Exception {
        Otp otp = new Otp(params);
        otp.calc();
        otp_resp = new String(otp.toString()); 
        return otp_resp;
    }

    public void restoreUserParams(OTPParameters p) {
        v_pass.setText(p.pass);
        v_alg.check(p.alg == Otp.MD5 ? R.id.otp_md5 : R.id.otp_md4);
        v_seq.setText(null);
        v_seed.setText(p.seed);
    }

    public void setUserParams(OTPParameters p) 
        throws OTPParametersException {
        p.set(v_pass.getText().toString(), 
                                   v_seed.getText().toString(),
                                   v_alg.getCheckedRadioButtonId() == R.id.otp_md4 ? Otp.MD4 : Otp.MD5, 
                                   v_seq.getText().toString());
    }
    
    public void onOk(View v) {
        try {
            setUserParams(params);
            computeResponse(params);
            response_text.setTextAppearance(response_text.getContext(), 
                                            R.style.otp_response_style);
            response_text.setText(otp_resp);
        }
        catch (OTPParametersException e) {
            response_text.setTextAppearance(response_text.getContext(), 
                                            R.style.otp_response_error);
            if (e.messageId() != 0)
                response_text.setText(e.messageId());
        }
        catch (Exception e) {
            response_text.setText("Fail");
        }           
    }
    
    private void onReset(View v) {
        try {
            params.clear();
            restoreUserParams(params);
            response_text.setTextAppearance(response_text.getContext(), 
                                            R.style.otp_response_style);
            response_text.setText(R.string.noresp);
            otp_resp = null;
        }
        catch (Exception e) {
            response_text.setText("Epic Fail");
        }
    }
    
    /** Button click */
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.otp_ok)
            onOk(v);
        if (vid == R.id.otp_reset)
            onReset(v);
    }
    
    public void onSaveInstanceState(Bundle state)
    {
        super.onSaveInstanceState(state);
        if (otp_resp != null)
            state.putString(SAVED_RESP_KEY, otp_resp);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
   
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button button; 
        button = (Button) findViewById(R.id.otp_ok);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.otp_reset);
        button.setOnClickListener(this);

        response_text = (TextView) findViewById(R.id.otp_resp);
        v_pass = (TextView) findViewById(R.id.otp_pass);
        v_seq = (TextView) findViewById(R.id.otp_seq);
        v_seed = (TextView) findViewById(R.id.otp_seed);
        v_alg = (RadioGroup) findViewById(R.id.otp_alg);
        
        if (savedInstanceState != null) {
            otp_resp = savedInstanceState.getString(SAVED_RESP_KEY);
            if (otp_resp != null) {
                response_text.
                    setTextAppearance(response_text.getContext(), 
                                      R.style.otp_response_style);
                response_text.setText(otp_resp);
            }
        }
    }
}
