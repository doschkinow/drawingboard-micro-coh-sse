package com.mycompany.drawingboard.light;

import com.mycompany.drawingboard.light.coherence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.cache.Cache;
import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryRemovedListener;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;

import com.tangosol.net.NamedCache;
import com.tangosol.util.ValueManipulator;
import com.tangosol.util.processor.NumberIncrementor;

/**
 * Simple in-memory data storage for the application.
 */
public class DataProvider {

    /**
     * Broadcaster for server-sent events.
     */
    private static SseBroadcaster sseBroadcaster = new SseBroadcaster();

    /**
     * Retrieves a drawing by ID.
     *
     * @param drawingId ID of the drawing to be retrieved.
     * @return Drawing with the corresponding ID.
     */
    public static synchronized Drawing getDrawing(int drawingId) {
        Cache<Integer, Drawing> drawingCache = CacheService.getDrawingsCache();
        return drawingCache.get(new Integer(drawingId));
    }

    /**
     * Retrieves all existing drawings.
     *
     * @return List of all drawings.
     */
    public static synchronized List<Drawing> getAllDrawings() {
        Cache<Integer, Drawing> drawingCache = CacheService.getDrawingsCache();
        // we use unwrap otherwise we would have to iterate through all cache entries
        NamedCache nc = drawingCache.unwrap(NamedCache.class);
        List<Drawing> list = new ArrayList<>(nc.values());
        Collections.sort(list);
        return list;
    }

    /**
     * Creates a new drawing based on the supplied drawing object.
     *
     * @param drawing Drawing object containing property values for the new
     * drawing.
     * @return ID of the newly created drawing.
     */
    public static synchronized int createDrawing(Drawing drawing) {
        Cache<Integer, Integer> idCache = CacheService.getIdCache();
        //alternatively we could write custom entry processor
        NamedCache idCacheNC = idCache.unwrap(NamedCache.class);
        Integer lastIdInteger = (Integer) idCacheNC.invoke(-1, new NumberIncrementor((ValueManipulator) null, 1, false));

        Drawing result = new Drawing();
        result.setId(lastIdInteger);
        result.setName(drawing.getName());

        Cache<Integer, Drawing> drawingsCache = CacheService.getDrawingsCache();
        drawingsCache.put(result.getId(), result);
        return result.getId();
    }

    /**
     * Delete a drawing with a given ID.
     *
     * @param drawingId ID of the drawing to be deleted.
     * @return {@code true} if the drawing was deleted, {@code false} if there
     * was no such drawing.
     */
    public static synchronized boolean deleteDrawing(int drawingId) {
        Cache<Integer, Drawing> drawingsCache = CacheService.getDrawingsCache();
        return drawingsCache.remove(new Integer(drawingId));
    }

    /**
     * Registers a new channel for sending events. An event channel corresponds
     * to a client (browser) event source connection.
     *
     * @param ec Event channel to be registered for sending events.
     */
    public static void addEventOutput(EventOutput eo) {
        sseBroadcaster.add(eo);
    }

    /**
     * DrawingsCacheEventListener to propagate events back to clients
     *
     * @author mbraeuer
     */
    public static class DrawingsCacheEventListener implements CacheEntryCreatedListener<Integer, Drawing>, CacheEntryRemovedListener<Integer, Drawing>, Serializable {

        @Override
        public void onRemoved(Iterable<CacheEntryEvent<? extends Integer, ? extends Drawing>> events)
                throws CacheEntryListenerException {

            for (CacheEntryEvent<? extends Integer, ? extends Drawing> event : events) {
                sseBroadcaster.broadcast(new OutboundEvent.Builder()
                        .name("delete")
                        .data(String.class, String.valueOf(((Drawing) event.getOldValue()).getId()))
                        .build());
                System.out.println("drawing removed");
            }

        }

        @Override
        public void onCreated(Iterable<CacheEntryEvent<? extends Integer, ? extends Drawing>> events)
                throws CacheEntryListenerException {

            for (CacheEntryEvent<? extends Integer, ? extends Drawing> event : events) {
                sseBroadcaster.broadcast(new OutboundEvent.Builder()
                        .name("create")
                        .data(Drawing.class, event.getValue())
                        .mediaType(MediaType.APPLICATION_JSON_TYPE)
                        .build());
                System.out.println("drawing added");
            }
        }
    }

}
