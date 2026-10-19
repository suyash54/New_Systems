package InMemCache;

public class CacheEntry<V> {
   final V value;
   final long expiryTime;

   public CacheEntry(V value, long expiryTime){
      this.value = value;
      this.expiryTime = expiryTime;
   }

   boolean isExpired(){
      return this.expiryTime < System.currentTimeMillis();
   }
}
