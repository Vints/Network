/* LockDList.java */

package list;

/**
 *  A LockDList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list. A node in a LockDList cannot be removed once added to the list.
 */

public class LockDList extends DList {

    protected LockDListNode newNode(Object item, LockDListNode prev, LockDListNode next) {
        return new LockDListNode(item, prev, next);
    }

    public void lockNode(DListNode node) {
        LockDListNode target = (LockDListNode) node;
        target.isLocked = true;
    } 

    public void remove(DListNode node) {
        LockDListNode target = (LockDListNode) node;
        if (target == null || target.isLocked) {
            return;
        }
        super.remove(node);
    }
}