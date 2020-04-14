package dev.m00nl1ght.ftl.autosaves;

import java.io.File;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * based on https://stackoverflow.com/a/27737069
 */
@SuppressWarnings({"CallToThreadYield", "resource"})
public class FileWatcher extends Thread {

    private final File file;
    private final Runnable onChange;
    private AtomicBoolean stop = new AtomicBoolean(false);

    public FileWatcher(File file, Runnable onChange) {
        this.file = file;
        this.onChange = onChange;
        setDaemon(true);
    }

    public boolean isStopped() { return stop.get(); }
    public void stopThread() { stop.set(true); }

    @Override
    public void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path path = file.toPath().getParent();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
            while (!isStopped()) {
                WatchKey key;
                try { key = watcher.poll(25, TimeUnit.MILLISECONDS); }
                catch (InterruptedException e) { return; }
                if (key == null) { Thread.yield(); continue; }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        Thread.yield();
                        continue;
                    } else if (filename.toString().equals(file.getName())) {
                        onChange.run();
                    }

                    boolean valid = key.reset();
                    if (!valid) { break; }
                }

                Thread.yield();
            }
        } catch (Throwable e) {
            throw new RuntimeException("Exception in FileWatcher", e);
        }
    }
}
