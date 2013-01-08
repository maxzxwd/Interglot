package org.spigotmc.interglot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

public class App {

    private static final Pattern versioned = Pattern.compile("^.*/v[\\d_]+(?=/)");
    private static final Pattern v = Pattern.compile("v(\\d_)?");
    private static final Set<String> repackaged = new HashSet<String>();

    static {
        repackaged.add("net/minecraft/server/");
        repackaged.add("org/bukkit/craftbukkit/");
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame gui = new Gui();

        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.requestFocus();
    }

    public static void process(File inFile, String outFile, final String version, final Logger logger) {
        Remapper remapper = new Remapper() {
            @Override
            public String map(String typeName) {
                for (String pre : repackaged) {
                    if (typeName.startsWith(pre)) {
                        Matcher matcher = versioned.matcher(typeName);
                        if (matcher.matches()) {
                            // Jar was compiled for older version, lets update the name
                            pre = typeName.substring(0, matcher.start());
                        }

                        String post = typeName.substring(pre.length(), typeName.length());
                        String newName = pre + version + "/" + post;
                        // logger.info(typeName + " -> " + newName);
                        return newName;
                    }
                }
                return typeName;
            }

            @Override
            public Object mapValue(Object value) {
                if (value instanceof String) {
                    if (v.matcher((String) value).matches()) {
                        logger.info("Mapped \"" + value + "\" -> \"Interglot\"");
                        value = "Interglot";
                    }
                }
                return super.mapValue(value);
            }
        };

        try {
            logger.info("Reading " + inFile);
            JarInputStream inJar = new JarInputStream(new FileInputStream(inFile));
            JarOutputStream out = new JarOutputStream(new FileOutputStream(outFile));
            int processed = 0;
            JarEntry entry;
            while ((entry = inJar.getNextJarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();

                ByteArrayOutputStream b = new ByteArrayOutputStream();
                byte[] buf = new byte[0x1000];
                int r;
                while ((r = inJar.read(buf)) != -1) {
                    b.write(buf, 0, r);
                }
                byte[] entryBytes = b.toByteArray();
                // logger.info("Read " + name + " from input jar.");

                if (name.endsWith(".class")) {
                    ClassReader cr = new ClassReader(entryBytes);
                    ClassWriter cw = new ClassWriter(cr, 0);
                    cr.accept(new RemappingClassAdapter(cw, remapper), ClassReader.EXPAND_FRAMES);
                    entryBytes = cw.toByteArray();
                    processed++;
                    // logger.info("Processed class file " + name);
                }

                out.putNextEntry(new JarEntry(name));
                out.write(entryBytes);
                // logger.info("Added " + name + " to output jar.");
            }

            inJar.close();
            out.close();

            logger.info("Done! Processed " + processed + " files. The new jar is at " + outFile + ". Remember this jar is may not work correctly at all and could cause harm to your server. Test first!\n");
        } catch (Exception ex) {
            logger.severe("Exception, check console for details!");
            ex.printStackTrace();
        }
    }
}
