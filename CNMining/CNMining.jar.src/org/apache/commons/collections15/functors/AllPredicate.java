/*     */ package org.apache.commons.collections15.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import org.apache.commons.collections15.Predicate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AllPredicate<T>
/*     */   implements Predicate<T>, PredicateDecorator<T>, Serializable
/*     */ {
/*     */   static final long serialVersionUID = -3094696765038308799L;
/*     */   private final Predicate<? super T>[] iPredicates;
/*     */   
/*     */   public static <T> Predicate<T> getInstance(Predicate<? super T>[] predicates)
/*     */   {
/*  53 */     FunctorUtils.validateMin2(predicates);
/*  54 */     predicates = FunctorUtils.copy(predicates);
/*  55 */     return new AllPredicate(predicates);
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
/*     */   public static <T> Predicate<T> getInstance(Collection<Predicate<? super T>> predicates)
/*     */   {
/*  68 */     Predicate[] preds = FunctorUtils.validate(predicates);
/*  69 */     return new AllPredicate(preds);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AllPredicate(Predicate<? super T>[] predicates)
/*     */   {
/*  80 */     this.iPredicates = predicates;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean evaluate(T object)
/*     */   {
/*  90 */     for (int i = 0; i < this.iPredicates.length; i++) {
/*  91 */       if (!this.iPredicates[i].evaluate(object)) {
/*  92 */         return false;
/*     */       }
/*     */     }
/*  95 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Predicate<? super T>[] getPredicates()
/*     */   {
/* 105 */     return this.iPredicates;
/*     */   }
/*     */ }


/* Location:              /home/menick/Scrivania/ProcessMining/CNMining/CNMining.jar!/org/apache/commons/collections15/functors/AllPredicate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */