package no.uib.ii.deliverandum.persistence.exceptions;

import org.springframework.core.NestedRuntimeException;

/**
 * Generic DAO exception used for checked exceptions and as a superclass for
 * other DAO exceptions
 *
 * Created at: 31.mai.2008 23:28:39
 *
 * @author Karianne Berg <karianne@ii.uib.no>
 * @author haakon
 */
public class DaoException extends NestedRuntimeException {
    
    private static final long serialVersionUID = -3126965724147259756L;

    public DaoException(Exception e) {
        this(e.getMessage(), e);
    }

    public DaoException(String message) {
        super(message);
    }
    
    public DaoException(String message, Exception e) {
        super(message, e);
    }    
    
}
