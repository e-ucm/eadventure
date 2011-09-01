package java.util;

public class QueueImpl<E> extends ArrayList<E> implements Queue<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public E pop() {
        if (size() == 0)
            return null;

        return remove(0);
    }

    public void push(E item) {
        add(item);
    }

    public E first() {
        if (size() == 0)
            return null;

        return get(0);
    }

    public E last() {
        if (size() == 0)
            return null;

        return get(size() - 1);
    }

	@Override
	public E element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean offer(E arg0) {
		// TODO Auto-generated method stub
		return false;
	}

    public E peek() {
        return first();
    }

    public E poll() {
        return pop();
    }

	@Override
	public E remove() {
		// TODO Auto-generated method stub
		return null;
	}
}
