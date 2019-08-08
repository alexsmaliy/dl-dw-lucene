package solutions.bloaty.misc.dwlucene.persistence;

import com.google.common.collect.ImmutableList;
import io.dropwizard.lifecycle.Managed;
import solutions.bloaty.tuts.dw.deepsearch.api.appconfig.DropwizardLuceneDeepSearchConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Set;

public class TotalDirectoryManager implements Managed {
    private final List<Managed> subjects;

    public TotalDirectoryManager(DropwizardLuceneDeepSearchConfig configuration) {
        subjects = ImmutableList.of(
            new Managed() {
                @Override
                public void start() {
                    makeLuceneIndexDir(configuration);
                }
                @Override
                public void stop() {}
            }
        );
    }

    @Override
    public void start() {
        subjects.forEach(s -> {
            try {
                s.start();
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize a directory!", e);
            }
        });
    }

    @Override
    public void stop() {
        subjects.forEach(s -> {
            try {
                s.start();
            } catch (Exception e) {
                throw new RuntimeException("Failed to clean up a directory!", e);
            }
        });
    }

    private static void makeLuceneIndexDir(DropwizardLuceneDeepSearchConfig configuration) {
        Set<PosixFilePermission> perms =
            PosixFilePermissions.fromString("rwxrwxrwx");
        FileAttribute<Set<PosixFilePermission>> fileAttributes =
            PosixFilePermissions.asFileAttribute(perms);
        Path indexDir = configuration.lucene().indexDir();
        if (!Files.exists(indexDir, LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(indexDir, fileAttributes);
            } catch (IOException e) {
                throw new RuntimeException("Unable to create directory for Lucene indexes!", e);
            }
        }
        if (!Files.isDirectory(indexDir, LinkOption.NOFOLLOW_LINKS) || !Files.isWritable(indexDir)) {
            throw new RuntimeException("Something already exists at the path configured for Lucene indexes, but is not  writable directory!");
        }
    }
}
