package org.spigotmc.interglot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

public final class App {
    private static final Pattern VERSIONED = Pattern.compile("/v[\\d_]+.*?(?=/)");
    private static final Set<String> REPACKAGED = new HashSet<>(3);

    private App() {
    }

    static {
        REPACKAGED.add("net/minecraft/");
        REPACKAGED.add("net/minecraft/server/");
        REPACKAGED.add("org/bukkit/craftbukkit/");
    }

    public static void main(String... args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame gui = new Gui();

        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.requestFocus();
    }

    public static void process(File inFile, String outFile, String version, Logger logger) {
        Remapper remapper = new Remapper() {
            @Override
            public String map(String typeName) {
                for (String pre : REPACKAGED) {
                    if (typeName.startsWith(pre)) {

                        String newName = typeName.substring(0, pre.length() - 1) +
                            VERSIONED.matcher(typeName.substring(pre.length() - 1))
                            .replaceFirst('/' + version);

                         logger.info(typeName + " -> " + newName);
                        return newName;
                    }
                }
                return typeName;
            }
        };

        try {
            logger.info("Reading " + inFile);
            int processed = 0;
            JarInputStream inJar = new JarInputStream(new FileInputStream(inFile));
            try (JarOutputStream out = new JarOutputStream(new FileOutputStream(outFile))) {
                JarEntry entry;
                while ((entry = inJar.getNextJarEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }
                    String name = entry.getName();

                    byte[] entryBytes = stream2Byte(inJar);
                    // logger.info("Read " + name + " from input jar.");

                    if (name.endsWith(".class")) {
                        ClassReader cr = new ClassReader(entryBytes);
                        ClassWriter cw = new ClassWriter(cr, 0);
                        cr.accept(new RemappingClassAdapter(cw, remapper), ClassReader.EXPAND_FRAMES);
                        entryBytes = cw.toByteArray();

                        // Unfortunately for us, Heroes implements an additonal version checker, so even though our bytecode is valid, it still won't run
                        String heroes = "com/herocraftonline/heroes/util/VersionChecker.class";
                        if (Objects.equals(name, heroes)) {
                            InputStream in = App.class.getClassLoader().getResourceAsStream(name);
                            entryBytes = stream2Byte(in);
                            in.close();
                        }

                        processed++;
                        // logger.info("Processed class file " + name);
                    }

                    out.putNextEntry(new JarEntry(name));
                    out.write(entryBytes);
                    // logger.info("Added " + name + " to output jar.");
                }
            }

            inJar.close();

            logger.info("Done! Processed " + processed + " files. The new jar is at " + outFile
                + ". Remember this jar is may not work correctly at all and could cause harm to your server. Test first!\n");
        } catch (Exception ex) {
            logger.severe("Exception, check console for details!");
            ex.printStackTrace();
        }
    }

    private static byte[] stream2Byte(InputStream in) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        byte[] buf = new byte[0x1000];
        int r;
        while ((r = in.read(buf)) != -1) {
            b.write(buf, 0, r);
        }
        return b.toByteArray();
    }
}
