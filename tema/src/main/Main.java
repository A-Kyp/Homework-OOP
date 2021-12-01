package main;

import actions.recommendation.Recommendation;
import checker.Checkstyle;
import checker.Checker;
import actions.commands.Grader;
import actions.commands.UserCommands;
import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.UserInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import actions.queries.Query;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        Grader grd = new Grader();
        UserCommands comm = new UserCommands();
        Query query = new Query();
        Recommendation rec = new Recommendation();

        /*Iterate through the action list*/
        for (ActionInputData aid : input.getCommands()) {
            /*Check if the command is user related*/
            if (aid.getActionType().equals("command")) {
                for (UserInputData u : input.getUsers()) {
                    /*If the user exist execute the specified command*/
                    if (u.getUsername().equals(aid.getUsername())) {
                        arrayResult.add(fileWriter.writeFile(aid.getActionId(), "",
                                            comm.execute(input, aid, u, grd)));
                    }
                }
            } else if (aid.getActionType().equals("query")) { /*Check if ot it is a query*/
                arrayResult.add(fileWriter.writeFile(aid.getActionId(), "",
                                    query.execute(input, aid, grd)));
            } else {
                arrayResult.add(fileWriter.writeFile(aid.getActionId(), "",
                                    rec.execute(input, aid, comm, grd)));
            }
        }

        fileWriter.closeJSON(arrayResult);
    }
}
