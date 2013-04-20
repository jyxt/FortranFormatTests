/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fortranformattests;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;


import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Example to watch a directory (or tree) for changes to files.
 */
public class WatchDir {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean recursive;
    private boolean trace = true;
    SimpleUI UI;
    private Model model;
    int BASTimes;
    
    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Register the given directory with the WatchService
     */
    
    public void updateUI(String s) {UI.updateProgress("\n"+s);}
           
    
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDir(Path dir, boolean recursive, SimpleUI UI) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.recursive = recursive;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            UI.updateProgress("Scanning "+dir);
            registerAll(dir);
            System.out.println("Done.");
            UI.updateProgress("Done");
        } else {
            register(dir);
            UI.updateProgress(dir+" registered");
        }

        // enable trace after initial registration
        this.trace = true;
        this.UI=UI;
        
        BASTimes=0;
    }

    /**
     * Process all events for keys queued to the watcher
     */
     void processEvents() {
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);

                
                
                if (child.getFileName().toString().split("\\.")[child.getFileName().toString().split("\\.").length-1].equals("BAS"))
                {
                    BASTimes++;
                }
                    
                if (BASTimes==2) {
                    BASTimes = 0;
                    System.out.println("BAS changed"); 
            //        UI.updateProgress("BAS changed");
                    
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(WatchDir.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    getModel().readBAS(model.getBAS(), false);
                    
                    UI.updateProgress(getModel().iboundStats());
                    
                    final JFrame frame;
                    frame = new JFrame("IBOUND");
                    frame.setLocationRelativeTo(null);
                    int w = 1000;
                    int h = 1000;
                    frame.setSize(w + 100, 200 + h);
                    ModelPanel panel = new ModelPanel(model);                    
                    panel.setLayer(1);
                    
                    frame.getContentPane().add(panel);
                    frame.setVisible(true);
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            frame.setVisible(false);
                            frame.removeAll();
                            frame.dispose();
                        }
                    });
                    
                    
                    /*
                    diff_match_patch diff = new diff_match_patch();
                    try {
                        try (BufferedReader in2 = new BufferedReader(new FileReader(child.toString())); BufferedReader in = new BufferedReader(new FileReader(child.toString()+"_Original"))) {
                            String strRead;
                            while ((strRead = in.readLine()) != null) {
                                LinkedList<diff_match_patch.Diff> Diffs = diff.diff_main(strRead, in2.readLine());
                                for (diff_match_patch.Diff d : Diffs) {
                                    if (d.operation != diff_match_patch.Operation.EQUAL) {
                                        System.out.println(d);
                                        UI.updateProgress(d.toString());
                                    }
                                }
                            }
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println("File not found !" + e.getMessage());
                    } catch (IOException ioe) {
                        System.out.println("IO Exception!");
                    }
                    */ 
                }


                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

    public static void main(String[] args) {
        // parse arguments
        /*
         if (args.length == 0 || args.length > 2)
         usage();
         boolean recursive = false;
         int dirArg = 0;
         if (args[0].equals("-r")) {
         if (args.length < 2)
         usage();
         recursive = true;
         dirArg++;
         }
         */

        // register directory and process its events
//        Path dir;
//        dir = Paths.get("C:\\0-Modeling projects\\22-PathBud");
//        WatchDir wd;
//        try {
//            wd = new WatchDir(dir, true);
//            wd.processEvents();
//        } catch (IOException ex) {
//            Logger.getLogger(WatchDir.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }
}
