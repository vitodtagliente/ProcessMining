/*     */ package com.carrotsearch.hppc;
/*     */ 
/*     */ import com.carrotsearch.hppc.cursors.IntCursor;
/*     */ import com.carrotsearch.hppc.predicates.IntPredicate;
/*     */ import com.carrotsearch.hppc.procedures.IntProcedure;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IntArrayList
/*     */   extends AbstractIntCollection
/*     */   implements IntIndexedContainer, Cloneable
/*     */ {
/*     */   public static final int DEFAULT_CAPACITY = 5;
/*  31 */   private static final Object EMPTY = new int[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] buffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int elementsCount;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ArraySizingStrategy resizer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntArrayList()
/*     */   {
/*  60 */     this(5);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntArrayList(int initialCapacity)
/*     */   {
/*  70 */     this(initialCapacity, new BoundedProportionalArraySizingStrategy());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntArrayList(int initialCapacity, ArraySizingStrategy resizer)
/*     */   {
/*  78 */     assert (initialCapacity >= 0) : ("initialCapacity must be >= 0: " + initialCapacity);
/*  79 */     assert (resizer != null);
/*     */     
/*  81 */     this.resizer = resizer;
/*  82 */     ensureBufferSpace(resizer.round(initialCapacity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntArrayList(IntContainer container)
/*     */   {
/*  90 */     this(container.size());
/*  91 */     addAll(container);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(int e1)
/*     */   {
/* 100 */     ensureBufferSpace(1);
/* 101 */     this.buffer[(this.elementsCount++)] = e1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(int e1, int e2)
/*     */   {
/* 111 */     ensureBufferSpace(2);
/* 112 */     this.buffer[(this.elementsCount++)] = e1;
/* 113 */     this.buffer[(this.elementsCount++)] = e2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(int[] elements, int start, int length)
/*     */   {
/* 121 */     assert (length >= 0) : "Length must be >= 0";
/*     */     
/* 123 */     ensureBufferSpace(length);
/* 124 */     System.arraycopy(elements, start, this.buffer, this.elementsCount, length);
/* 125 */     this.elementsCount += length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(int... elements)
/*     */   {
/* 135 */     add(elements, 0, elements.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int addAll(IntContainer container)
/*     */   {
/* 143 */     int size = container.size();
/* 144 */     ensureBufferSpace(size);
/*     */     
/* 146 */     for (IntCursor cursor : container)
/*     */     {
/* 148 */       add(cursor.value);
/*     */     }
/*     */     
/* 151 */     return size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int addAll(Iterable<? extends IntCursor> iterable)
/*     */   {
/* 159 */     int size = 0;
/* 160 */     for (IntCursor cursor : iterable)
/*     */     {
/* 162 */       add(cursor.value);
/* 163 */       size++;
/*     */     }
/* 165 */     return size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void insert(int index, int e1)
/*     */   {
/* 175 */     assert ((index >= 0) && (index <= size())) : ("Index " + index + " out of bounds [" + 0 + ", " + size() + "].");
/*     */     
/* 177 */     ensureBufferSpace(1);
/* 178 */     System.arraycopy(this.buffer, index, this.buffer, index + 1, this.elementsCount - index);
/* 179 */     this.buffer[index] = e1;
/* 180 */     this.elementsCount += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int get(int index)
/*     */   {
/* 190 */     assert ((index >= 0) && (index < size())) : ("Index " + index + " out of bounds [" + 0 + ", " + size() + ").");
/*     */     
/* 192 */     return this.buffer[index];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int set(int index, int e1)
/*     */   {
/* 202 */     assert ((index >= 0) && (index < size())) : ("Index " + index + " out of bounds [" + 0 + ", " + size() + ").");
/*     */     
/* 204 */     int v = this.buffer[index];
/* 205 */     this.buffer[index] = e1;
/* 206 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int remove(int index)
/*     */   {
/* 216 */     assert ((index >= 0) && (index < size())) : ("Index " + index + " out of bounds [" + 0 + ", " + size() + ").");
/*     */     
/* 218 */     int v = this.buffer[index];
/* 219 */     if (index + 1 < this.elementsCount)
/* 220 */       System.arraycopy(this.buffer, index + 1, this.buffer, index, this.elementsCount - index - 1);
/* 221 */     this.elementsCount -= 1;
/* 222 */     this.buffer[this.elementsCount] = 0;
/* 223 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeRange(int fromIndex, int toIndex)
/*     */   {
/* 233 */     assert ((fromIndex >= 0) && (fromIndex <= size())) : ("Index " + fromIndex + " out of bounds [" + 0 + ", " + size() + ").");
/*     */     
/*     */ 
/* 236 */     assert ((toIndex >= 0) && (toIndex <= size())) : ("Index " + toIndex + " out of bounds [" + 0 + ", " + size() + "].");
/*     */     
/* 238 */     assert (fromIndex <= toIndex) : ("fromIndex must be <= toIndex: " + fromIndex + ", " + toIndex);
/*     */     
/*     */ 
/* 241 */     System.arraycopy(this.buffer, toIndex, this.buffer, fromIndex, this.elementsCount - toIndex);
/*     */     
/* 243 */     int count = toIndex - fromIndex;
/* 244 */     this.elementsCount -= count;
/* 245 */     Arrays.fill(this.buffer, this.elementsCount, this.elementsCount + count, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int removeFirstOccurrence(int e1)
/*     */   {
/* 255 */     int index = indexOf(e1);
/* 256 */     if (index >= 0) remove(index);
/* 257 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int removeLastOccurrence(int e1)
/*     */   {
/* 266 */     int index = lastIndexOf(e1);
/* 267 */     if (index >= 0) remove(index);
/* 268 */     return index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int removeAllOccurrences(int e1)
/*     */   {
/* 277 */     int to = 0;
/* 278 */     for (int from = 0; from < this.elementsCount; from++)
/*     */     {
/* 280 */       if (e1 == this.buffer[from])
/*     */       {
/* 282 */         this.buffer[from] = 0;
/*     */       }
/*     */       else
/*     */       {
/* 286 */         if (to != from)
/*     */         {
/* 288 */           this.buffer[to] = this.buffer[from];
/* 289 */           this.buffer[from] = 0;
/*     */         }
/* 291 */         to++;
/*     */       }
/*     */     }
/* 294 */     int deleted = this.elementsCount - to;
/* 295 */     this.elementsCount = to;
/* 296 */     return deleted;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean contains(int e1)
/*     */   {
/* 305 */     return indexOf(e1) >= 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int indexOf(int e1)
/*     */   {
/* 314 */     for (int i = 0; i < this.elementsCount; i++) {
/* 315 */       if (e1 == this.buffer[i])
/* 316 */         return i;
/*     */     }
/* 318 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int lastIndexOf(int e1)
/*     */   {
/* 327 */     for (int i = this.elementsCount - 1; i >= 0; i--) {
/* 328 */       if (e1 == this.buffer[i])
/* 329 */         return i;
/*     */     }
/* 331 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 340 */     return this.elementsCount == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void ensureCapacity(int minCapacity)
/*     */   {
/* 350 */     if (minCapacity > this.buffer.length) {
/* 351 */       ensureBufferSpace(minCapacity - size());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void ensureBufferSpace(int expectedAdditions)
/*     */   {
/* 360 */     int bufferLen = this.buffer == null ? 0 : this.buffer.length;
/* 361 */     if (this.elementsCount >= bufferLen - expectedAdditions)
/*     */     {
/* 363 */       int newSize = this.resizer.grow(bufferLen, this.elementsCount, expectedAdditions);
/* 364 */       assert (newSize >= this.elementsCount + expectedAdditions) : ("Resizer failed to return sensible new size: " + newSize + " <= " + (this.elementsCount + expectedAdditions));
/*     */       
/*     */ 
/*     */ 
/* 368 */       int[] newBuffer = new int[newSize];
/* 369 */       if (bufferLen > 0)
/*     */       {
/* 371 */         System.arraycopy(this.buffer, 0, newBuffer, 0, this.buffer.length);
/*     */       }
/* 373 */       this.buffer = newBuffer;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resize(int newSize)
/*     */   {
/* 386 */     if (newSize <= this.buffer.length)
/*     */     {
/* 388 */       if (newSize < this.elementsCount)
/*     */       {
/* 390 */         Arrays.fill(this.buffer, newSize, this.elementsCount, 0);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 395 */         Arrays.fill(this.buffer, this.elementsCount, newSize, 0);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else {
/* 401 */       ensureCapacity(newSize);
/*     */     }
/* 403 */     this.elementsCount = newSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 412 */     return this.elementsCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void trimToSize()
/*     */   {
/* 421 */     if (size() != this.buffer.length) {
/* 422 */       this.buffer = ((int[])toArray());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 433 */     Arrays.fill(this.buffer, 0, this.elementsCount, 0);
/* 434 */     this.elementsCount = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void release()
/*     */   {
/* 443 */     this.buffer = ((int[])EMPTY);
/* 444 */     this.elementsCount = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] toArray()
/*     */   {
/* 457 */     return Arrays.copyOf(this.buffer, this.elementsCount);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IntArrayList clone()
/*     */   {
/*     */     try
/*     */     {
/* 470 */       IntArrayList cloned = (IntArrayList)super.clone();
/* 471 */       cloned.buffer = ((int[])this.buffer.clone());
/* 472 */       return cloned;
/*     */     }
/*     */     catch (CloneNotSupportedException e)
/*     */     {
/* 476 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 486 */     int h = 1;int max = this.elementsCount;
/* 487 */     for (int i = 0; i < max; i++)
/*     */     {
/* 489 */       h = 31 * h + Internals.rehash(this.buffer[i]);
/*     */     }
/* 491 */     return h;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 501 */     if (obj != null)
/*     */     {
/* 503 */       if ((obj instanceof IntArrayList))
/*     */       {
/* 505 */         IntArrayList other = (IntArrayList)obj;
/* 506 */         return (other.size() == size()) && (rangeEquals(other.buffer, this.buffer, size()));
/*     */       }
/*     */       
/* 509 */       if ((obj instanceof IntIndexedContainer))
/*     */       {
/* 511 */         IntIndexedContainer other = (IntIndexedContainer)obj;
/* 512 */         return (other.size() == size()) && (allIndexesEqual(this, other, size()));
/*     */       }
/*     */     }
/*     */     
/* 516 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean rangeEquals(int[] b1, int[] b2, int length)
/*     */   {
/* 525 */     for (int i = 0; i < length; i++)
/*     */     {
/* 527 */       if (b1[i] != b2[i])
/*     */       {
/* 529 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 533 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean allIndexesEqual(IntIndexedContainer b1, IntIndexedContainer b2, int length)
/*     */   {
/* 543 */     for (int i = 0; i < length; i++)
/*     */     {
/* 545 */       int o1 = b1.get(i);
/* 546 */       int o2 = b2.get(i);
/*     */       
/* 548 */       if (o1 != o2)
/*     */       {
/* 550 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 554 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   static final class ValueIterator
/*     */     extends AbstractIterator<IntCursor>
/*     */   {
/*     */     private final IntCursor cursor;
/*     */     
/*     */     private final int[] buffer;
/*     */     
/*     */     private final int size;
/*     */     
/*     */     public ValueIterator(int[] buffer, int size)
/*     */     {
/* 569 */       this.cursor = new IntCursor();
/* 570 */       this.cursor.index = -1;
/* 571 */       this.size = size;
/* 572 */       this.buffer = buffer;
/*     */     }
/*     */     
/*     */ 
/*     */     protected IntCursor fetch()
/*     */     {
/* 578 */       if (this.cursor.index + 1 == this.size) {
/* 579 */         return (IntCursor)done();
/*     */       }
/* 581 */       this.cursor.value = this.buffer[(++this.cursor.index)];
/* 582 */       return this.cursor;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<IntCursor> iterator()
/*     */   {
/* 592 */     return new ValueIterator(this.buffer, size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends IntProcedure> T forEach(T procedure)
/*     */   {
/* 601 */     return forEach(procedure, 0, size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends IntProcedure> T forEach(T procedure, int fromIndex, int toIndex)
/*     */   {
/* 613 */     assert ((fromIndex >= 0) && (fromIndex <= size())) : ("Index " + fromIndex + " out of bounds [" + 0 + ", " + size() + ").");
/*     */     
/*     */ 
/* 616 */     assert ((toIndex >= 0) && (toIndex <= size())) : ("Index " + toIndex + " out of bounds [" + 0 + ", " + size() + "].");
/*     */     
/* 618 */     assert (fromIndex <= toIndex) : ("fromIndex must be <= toIndex: " + fromIndex + ", " + toIndex);
/*     */     
/*     */ 
/* 621 */     int[] buffer = this.buffer;
/* 622 */     for (int i = fromIndex; i < toIndex; i++)
/*     */     {
/* 624 */       procedure.apply(buffer[i]);
/*     */     }
/*     */     
/* 627 */     return procedure;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int removeAll(IntPredicate predicate)
/*     */   {
/* 636 */     int elementsCount = this.elementsCount;
/* 637 */     int to = 0;
/* 638 */     int from = 0;
/*     */     try
/*     */     {
/* 641 */       for (; from < elementsCount; from++)
/*     */       {
/* 643 */         if (predicate.apply(this.buffer[from]))
/*     */         {
/* 645 */           this.buffer[from] = 0;
/*     */         }
/*     */         else
/*     */         {
/* 649 */           if (to != from)
/*     */           {
/* 651 */             this.buffer[to] = this.buffer[from];
/* 652 */             this.buffer[from] = 0;
/*     */           }
/* 654 */           to++;
/*     */         }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 660 */       for (; from < elementsCount; from++)
/*     */       {
/* 662 */         if (to != from)
/*     */         {
/* 664 */           this.buffer[to] = this.buffer[from];
/* 665 */           this.buffer[from] = 0;
/*     */         }
/* 667 */         to++;
/*     */       }
/*     */       
/* 670 */       this.elementsCount = to;
/*     */     }
/*     */     
/* 673 */     return elementsCount - to;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends IntPredicate> T forEach(T predicate)
/*     */   {
/* 682 */     return forEach(predicate, 0, size());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends IntPredicate> T forEach(T predicate, int fromIndex, int toIndex)
/*     */   {
/* 694 */     assert ((fromIndex >= 0) && (fromIndex <= size())) : ("Index " + fromIndex + " out of bounds [" + 0 + ", " + size() + ").");
/*     */     
/*     */ 
/* 697 */     assert ((toIndex >= 0) && (toIndex <= size())) : ("Index " + toIndex + " out of bounds [" + 0 + ", " + size() + "].");
/*     */     
/* 699 */     assert (fromIndex <= toIndex) : ("fromIndex must be <= toIndex: " + fromIndex + ", " + toIndex);
/*     */     
/*     */ 
/* 702 */     int[] buffer = this.buffer;
/* 703 */     for (int i = fromIndex; i < toIndex; i++)
/*     */     {
/* 705 */       if (!predicate.apply(buffer[i])) {
/*     */         break;
/*     */       }
/*     */     }
/* 709 */     return predicate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntArrayList newInstance()
/*     */   {
/* 719 */     return new IntArrayList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntArrayList newInstanceWithCapacity(int initialCapacity)
/*     */   {
/* 729 */     return new IntArrayList(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntArrayList from(int... elements)
/*     */   {
/* 739 */     IntArrayList list = new IntArrayList(elements.length);
/* 740 */     list.add(elements);
/* 741 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IntArrayList from(IntContainer container)
/*     */   {
/* 750 */     return new IntArrayList(container);
/*     */   }
/*     */ }


/* Location:              /home/menick/Scrivania/ProcessMining/CNMining/CNMining.jar!/com/carrotsearch/hppc/IntArrayList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */