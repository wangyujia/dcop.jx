package JX;

/*
 * ¥ÌŒÛ¬Î∂®“Â errno definitions
 */
public enum Errno
{
    SUCCESS(0),

    FAILURE(-1);


    private int nCode;


    private Errno(int nCode)
    {
        this.nCode = nCode;
    }

    @Override
    public String toString()
    {
        return String.valueOf(this.nCode);
    }

}

