/* LockDListNode.java */

package list;

/**
 *  A LockDListNode is a node in a LockDList (doubly-linked list with nodes that cannot be removed).
 */

public class LockDListNode extends DListNode {

    protected boolean isLocked = false;

    LockDListNode(Object i, DListNode p, DListNode n) {
        super(i, p , n);
    }
}