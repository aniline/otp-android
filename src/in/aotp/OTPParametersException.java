package in.aotp;
/**
 * Bad OTP Parameters.
 */
class OTPParametersException extends Exception {
    static final long serialVersionUID = 1; 
    public String message;
    public int m_id;
        
    public OTPParametersException(String m)
    {
        message = new String(m);
        m_id = 0;
    }

    public OTPParametersException(int m)
    {
        m_id = m;
        message = null;
    }
        
    public int messageId() {
        return m_id;
    }
        
    public String toString () {
        return message;
    }
}
