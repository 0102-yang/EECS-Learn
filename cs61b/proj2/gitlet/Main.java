package gitlet;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        try {
            int length = args.length;
            if (length == 0) {
                throw Utils.error("Please enter a command.");
            }

            String firstArg = args[0];
            switch (firstArg) {
                case "init":
                    // TODO: handle the `init` command
                    checkArgumentsLength(length, 1);
                    Repository.initGitletDirectory();
                    break;
                case "add":
                    // TODO: handle the `add [filename]` command
                    break;
                case "commit":
                    break;
                case "rm":
                    break;
                case "log":
                    break;
                case "global-log":
                    break;
                case "find":
                    break;
                case "status":
                    break;
                case "checkout":
                    break;
                case "branch":
                    break;
                case "rm-branch":
                    break;
                case "reset":
                    break;
                case "merge":
                    break;
                default:
                    throw new GitletException("No command with that name exists.");
            }
        } catch (GitletException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static void checkArgumentsLength(int actualLength, int... expectedLength) {
        boolean flag = false;
        for (int l : expectedLength) {
            if (actualLength == l) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            throw Utils.error("Incorrect operands.");
        }
    }

    private static void isInGitletDirectory() {
        if (!Repository.gitletDirectoryExists()) {
            throw Utils.error("Not in an initialized Gitlet directory.");
        }
    }

}
