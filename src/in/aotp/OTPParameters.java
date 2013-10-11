/**
o * 
 */
package in.aotp;

import in.aotp.R;

/**
 * Parameters for the Otp algorithm
 */
class OTPParameters {
    public String pass;
    public String seed;
    public int alg = Otp.MD5;
    public int seq = 0;
    
    public OTPParameters()
    {
    }
    
    public OTPParameters(String p_pass, String p_seed, int p_alg, String p_seq)
    		throws OTPParametersException 
    {
    	set(p_pass, p_seed, p_alg, p_seq);
    }
    
    public OTPParameters(String p_pass, String p_seed, int p_alg, int p_seq) 
    		throws OTPParametersException 
    {
    	set(p_pass, p_seed, p_alg, p_seq);
    }

    public void set(String p_pass, String p_seed, int p_alg, String p_seq) 
        throws OTPParametersException
    {
        try {
            set(p_pass, p_seed, p_alg, Integer.parseInt(p_seq)); 
        }
        catch (NumberFormatException e) {
            throw new OTPParametersException(R.string.error_badseq);
        }
    }

    public void set(String p_pass, String p_seed, int p_alg, int p_seq) 
        throws OTPParametersException
    {
        if (p_seq > 10000)
            throw new OTPParametersException(R.string.error_bigseq);
        if (p_seq < 5)
            throw new OTPParametersException(R.string.error_smallseq);
        if (p_pass.length() < 10)
            throw new OTPParametersException(R.string.error_smallpass);
        if (p_pass.length() > 64)
            throw new OTPParametersException(R.string.error_bigpass);
        if (p_seed.length() < 1)
            throw new OTPParametersException(R.string.error_badseed);
        pass = new String(p_pass);
        seed = new String(p_seed);
        alg = p_alg;
        seq = p_seq;
    }

    public void clear()
    {
        pass = null;
        seed = null;
        alg = Otp.MD5;
        seq = 0;
    }
}
