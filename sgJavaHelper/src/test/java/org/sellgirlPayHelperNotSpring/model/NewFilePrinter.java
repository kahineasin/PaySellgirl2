package org.sellgirlPayHelperNotSpring.model;

import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import java.io.*;
 
public class NewFilePrinter {
    private final WatchService watcher;
    private final Path pathToWatch;
 
    public NewFilePrinter(Path path) throws IOException {
        this.pathToWatch = path;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.pathToWatch.register(watcher, ENTRY_CREATE);
    }
 
    public void start() throws IOException, InterruptedException {
        while (true) {
            WatchKey key = watcher.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
 
                if (kind == OVERFLOW) {
                    continue;
                }
 
                if (kind == ENTRY_CREATE) {
                    Path file = ((Path) event.context());
                    System.out.println("New file created: " + file.toString());
                    // 可以在这里添加额外的处理文件的代码
                }
            }
 
            key.reset();
            Thread.sleep(1000);
        }
    }
 
    public static void main(String[] args) throws IOException, InterruptedException {
//        Path path = Paths.get("path/to/watch").toAbsolutePath();
        Path path = Paths.get("D:\\cache\\1").toAbsolutePath();
        NewFilePrinter printer = new NewFilePrinter(path);
        printer.start();
    }
}