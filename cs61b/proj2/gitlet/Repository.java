package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import static gitlet.Utils.*;

// TODO: any imports you need here

/**
 * Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 * @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /**
     * The current working directory.
     */
    private static final File CWD = new File(System.getProperty("user.dir"));

    /**
     * The .gitlet directory.
     */
    private static final File GITLET_DIR = join(CWD, ".gitlet");

    private static final File BRANCHES_DIR = join(GITLET_DIR, "branches");

    private static final File OBJECT_DIR = join(GITLET_DIR, "objects");

    private static final File HEAD = join(GITLET_DIR, "HEAD");

    private static final File INDEX = join(GITLET_DIR, "index");

    private static final File CURRENT_BRANCH = join(GITLET_DIR, "CURRENT_BRANCH");

    /**
     * Init gitlet directory.
     */
    public static void initGitletDirectory() throws GitletException {
        if (gitletDirectoryExists()) {
            throw error("A Gitlet version-control system already exists in the current directory.");
        }

        try {
            // Create gitlet directory.
            Files.createDirectory(GITLET_DIR.toPath());
            Files.createDirectory(BRANCHES_DIR.toPath());
            Files.createDirectory(OBJECT_DIR.toPath());
            Files.createFile(HEAD.toPath());
            Files.createFile(INDEX.toPath());
            Files.createFile(CURRENT_BRANCH.toPath());

            // Create initial commit and save it to objects file.
            Commit initialCommit = new Commit("initial commit", new Date(0), null, null, null);
            String hashedCommit = sha1(initialCommit);
            File initialCommitFile = join(OBJECT_DIR, hashedCommit);
            Files.createFile(initialCommitFile.toPath());
            writeObject(initialCommitFile, initialCommit);

            // Set head.
            writeObject(HEAD, hashedCommit);

            // Create master branch.
            createBranch("master");

            // Set current branch to master.
            writeObject(CURRENT_BRANCH, "master");
        } catch (IOException e) {
            throw error(e.getMessage());
        }
    }

    public static boolean gitletDirectoryExists() {
        return GITLET_DIR.exists();
    }

    private static void createBranch(String branchName) throws IOException {
        File newBranch = join(BRANCHES_DIR, branchName);
        if (newBranch.exists()) {
            // TODO: Should change error message.
            throw error("A branch with that name already exists.");
        }

        String headRef = readContentsAsString(HEAD);
        File head = join(newBranch, "HEAD");
        writeObject(head, headRef);

        Files.createFile(head.toPath());
        Files.createFile(join(newBranch, "index").toPath());
    }

}
