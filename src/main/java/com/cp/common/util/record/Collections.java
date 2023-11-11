/*
 * Collections.java (c) 4 November 2003
 *
 * Code from Sun Microsystems Java 2 Platform SDK Standard Edition v1.4.1.
 *
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * @author Josh Bloch
 * @author John J. Blum
 * @version 1.63, 05/12/02
 * @deprecated
 */

package com.cp.common.util.record;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

final class Collections {

  /**
   * Returns a synchronized (thread-safe) collection backed by the specified
   * collection.  In order to guarantee serial access, it is critical that
   * <strong>all</strong> access to the backing collection is accomplished
   * through the returned collection.<p>
   *
   * It is imperative that the user manually synchronize on the returned
   * collection when iterating over it:
   * <pre>
   *  Collection c = Collections.synchronizedCollection(myCollection);
   *     ...
   *  synchronized(c) {
   *      Iterator i = c.iterator(); // Must be in the synchronized block
   *      while (i.hasNext())
   *         foo(i.next());
   *  }
   * </pre>
   * Failure to follow this advice may result in non-deterministic behavior.
   *
   * <p>The returned collection does <i>not</i> pass the <tt>hashCode</tt>
   * and <tt>equals</tt> operations through to the backing collection, but
   * relies on <tt>Object</tt>'s equals and hashCode methods.  This is
   * necessary to preserve the contracts of these operations in the case
   * that the backing collection is a set or a list.<p>
   *
   * The returned collection will be serializable if the specified collection
   * is serializable.
   *
   * @param  c the collection to be "wrapped" in a synchronized collection.
   * @return a synchronized view of the specified collection.
   */
  public static Collection synchronizedCollection(Collection c) {
    return new SynchronizedCollection(c);
  }

  static Collection synchronizedCollection(Collection c, Object mutex) {
    return new SynchronizedCollection(c, mutex);
  }

  /**
   * @serial include
   */
  private static class SynchronizedCollection implements Collection, Serializable {
    // use serialVersionUID from JDK 1.2.2 for interoperability
    private static final long serialVersionUID = 3053995032091335093L;

    Collection c;	   // Backing Collection
    Object mutex;  // Object on which to synchronize

    SynchronizedCollection(Collection c) {
      if (c == null)
        throw new NullPointerException();
      this.c = c;
      mutex = this;
    }

    SynchronizedCollection(Collection c, Object mutex) {
      this.c = c;
      this.mutex = mutex;
    }

    public int size() {
      synchronized (mutex) {
        return c.size();
      }
    }

    public boolean isEmpty() {
      synchronized (mutex) {
        return c.isEmpty();
      }
    }

    public boolean contains(Object o) {
      synchronized (mutex) {
        return c.contains(o);
      }
    }

    public Object[] toArray() {
      synchronized (mutex) {
        return c.toArray();
      }
    }

    public Object[] toArray(Object[] a) {
      synchronized (mutex) {
        return c.toArray(a);
      }
    }

    public Iterator iterator() {
      return c.iterator(); // Must be manually synched by user!
    }

    public boolean add(Object o) {
      synchronized (mutex) {
        return c.add(o);
      }
    }

    public boolean remove(Object o) {
      synchronized (mutex) {
        return c.remove(o);
      }
    }

    public boolean containsAll(Collection coll) {
      synchronized (mutex) {
        return c.containsAll(coll);
      }
    }

    public boolean addAll(Collection coll) {
      synchronized (mutex) {
        return c.addAll(coll);
      }
    }

    public boolean removeAll(Collection coll) {
      synchronized (mutex) {
        return c.removeAll(coll);
      }
    }

    public boolean retainAll(Collection coll) {
      synchronized (mutex) {
        return c.retainAll(coll);
      }
    }

    public void clear() {
      synchronized (mutex) {
        c.clear();
      }
    }

    public String toString() {
      synchronized (mutex) {
        return c.toString();
      }
    }
  }

  /**
   * Returns a synchronized (thread-safe) set backed by the specified
   * set.  In order to guarantee serial access, it is critical that
   * <strong>all</strong> access to the backing set is accomplished
   * through the returned set.<p>
   *
   * It is imperative that the user manually synchronize on the returned
   * set when iterating over it:
   * <pre>
   *  Set s = Collections.synchronizedSet(new HashSet());
   *      ...
   *  synchronized(s) {
   *      Iterator i = s.iterator(); // Must be in the synchronized block
   *      while (i.hasNext())
   *          foo(i.next());
   *  }
   * </pre>
   * Failure to follow this advice may result in non-deterministic behavior.
   *
   * <p>The returned set will be serializable if the specified set is
   * serializable.
   *
   * @param  s the set to be "wrapped" in a synchronized set.
   * @return a synchronized view of the specified set.
   */
  public static Set synchronizedSet(Set s) {
    return new SynchronizedSet(s);
  }

  static Set synchronizedSet(Set s, Object mutex) {
    return new SynchronizedSet(s, mutex);
  }

  /**
   * @serial include
   */
  private static class SynchronizedSet extends SynchronizedCollection
      implements Set {
    SynchronizedSet(Set s) {
      super(s);
    }

    SynchronizedSet(Set s, Object mutex) {
      super(s, mutex);
    }

    public boolean equals(Object o) {
      synchronized (mutex) {
        return c.equals(o);
      }
    }

    public int hashCode() {
      synchronized (mutex) {
        return c.hashCode();
      }
    }
  }

}
