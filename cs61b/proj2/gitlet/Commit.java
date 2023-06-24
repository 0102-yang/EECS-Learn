package gitlet;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Commit implements Serializable {

    /**
     * The message of this Commit.
     */
    private final String message;

    private final Date timestamp;

    /**
     * The hash id of parent.
     */
    private final String parentRef;

    /**
     * The hash id of second parent(for merge).
     */
    private final String secondParentRef;

    private final String[] filenameBlobRefs;

    /* TODO: fill in the rest of this class. */

    public Commit(String message, Date timestamp, String parentRef, String secondParentRef, String[] filenameBlobRefs) {
        this.message = message;
        this.timestamp = timestamp;
        this.parentRef = parentRef;
        this.secondParentRef = secondParentRef;
        this.filenameBlobRefs = filenameBlobRefs;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getParentRef() {
        return this.parentRef;
    }

    public String getSecondParentRef() {
        return this.secondParentRef;
    }

    public String[] getFilenameBlobRefs() {
        return this.filenameBlobRefs;
    }

}
