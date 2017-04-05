package com.mycompany.drawingboard.light.coherence;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;

import com.mycompany.drawingboard.light.Drawing;
import com.mycompany.drawingboard.light.DataProvider.DrawingsCacheEventListener;

/**
 * CacheService provides service to access caches
 * @author mbraeuer
 */
public class CacheService {
	
	private static CachingProvider cachingProvider = Caching.getCachingProvider();
	private static CacheManager cacheManager = null;
	
	private static Cache<Integer, Drawing> drawingCache = null;
	private static Cache<Integer, Integer> idCache = null;
	
	public static Cache<Integer, Drawing> getDrawingsCache() {
		
		if (drawingCache != null) {
			return drawingCache;
			
		}  else {
		
			MutableConfiguration<Integer, Drawing> config = new MutableConfiguration<Integer, Drawing>();
		
			if (cacheManager == null) {
				try {
					cacheManager = cachingProvider.getCacheManager();
				} catch (Exception ex) {
					System.out.println("Can't get CacheManager. Exiting ...");
					ex.printStackTrace();
					System.exit(1);
				}
			}
			
			try {
				drawingCache = cacheManager.createCache("drawings-cache", config);
		
			} catch (Exception ex) {
				System.out.println("Can't get drawings-cache. Exiting ...");
				ex.printStackTrace();
				System.exit(1);
			}
				
			return drawingCache;
		}
	}
	
	public static Cache<Integer, Integer> getIdCache() {
		
		if (idCache != null) {
			return idCache;
			
		}  else {
		
			MutableConfiguration<Integer, Integer> config = new MutableConfiguration<Integer, Integer>();
		
			if (cacheManager == null) {
				try {
					cacheManager = cachingProvider.getCacheManager();
				} catch (Exception ex) {
					System.out.println("Can't get CacheManager. Exiting ...");
					ex.printStackTrace();
					System.exit(1);
				}
			}
			
			try {
				idCache = cacheManager.createCache("id-cache", config);
		
			} catch (Exception ex) {
				System.out.println("Can't get id-cache. Exiting ...");
				ex.printStackTrace();
				System.exit(1);
			}
				
			return idCache;
		}
	}
	
	public static void initializeAll() throws Exception {
		Cache<Integer, Integer> idCache = CacheService.getIdCache();
		idCache.putIfAbsent(new Integer(-1), new Integer(0));
		
		DrawingsCacheEventListener listener = new DrawingsCacheEventListener();
		MutableCacheEntryListenerConfiguration<Integer, Drawing> cacheEntryListenerConfiguration 
			= new MutableCacheEntryListenerConfiguration<Integer, Drawing>
					(FactoryBuilder.factoryOf(listener), null, true, false);
		
		Cache<Integer, Drawing> drawingsCache = CacheService.getDrawingsCache();
		drawingsCache.registerCacheEntryListener(cacheEntryListenerConfiguration);
	}
		
	public static void close() {
		
		if (drawingCache != null) {
			try {
				drawingCache.close();
			} catch (Exception ex) {
				// close on shutdown. do nothing.
			}
		}
		
		if (cacheManager != null) {
			try {
				cacheManager.close();
			} catch (Exception ex) {
				// close on shutdown. do nothing.
			}
		}
		
		if (cachingProvider != null) {
			try {
				cachingProvider.close();
			} catch (Exception ex) {
				// close on shutdown. do nothing.
			}
			
		}
	}
}
