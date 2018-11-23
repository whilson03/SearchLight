/**
 * author : Adeyoriju Olabode Wilson
 * Department of Computer Science and Enginneering,
 * Obafemi Awolowo University
 * Date created; 26 - 10 - 2018.
 * Date last modified; 7 - 11 - 2018.
 * purpose:
 * searches for specified keywords that match a word inside file or
 * a filename or a folder name.inspired by spotlight.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchLight {
    private String keyWord;
    private File directoryToWork;
    private ArrayList<File> pendingFilesList = new ArrayList<>();
    private ArrayList<File> otherFilesList = new ArrayList<>();


    public SearchLight() {

    }

    public SearchLight(File directory) throws IOException {
        if (directory.isDirectory() && directory.exists()) {
            this.directoryToWork = directory;
            getKeyWord();
            addFilesToList();
        } else {
            throw new IOException("enter a valid directory");
        }
    }

    public void setDirectoryToWork(File directoryToWork) throws IOException {
        if (directoryToWork.isDirectory()) {
            this.directoryToWork = directoryToWork;
        } else {
            throw new IOException("enter a valid directory");
        }
    }


    /**
     * get keyword to search for from user
     *
     * @return: String of the keyword.
     */
    private String getKeyWord() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a keyword to search :".toUpperCase());
        keyWord = input.nextLine();
        return keyWord;

    }

    /**
     * sort specified files to be processed in the pending files list for processing.
     * @return
     */
    private void addFilesToList() {
        try {
            // create array of all files in the working directory.
            File[] arrayOfFilesInDirectory = directoryToWork.listFiles();
            assert arrayOfFilesInDirectory != null;
            for (File file : arrayOfFilesInDirectory) {
                // restricted files that are not to be read.
                boolean restrictedFiles = file.getName().endsWith(".pptx") ||
                        file.getName().endsWith(".png") ||
                        file.getName().endsWith(".docx") ||
                        file.getName().endsWith(".gif") ||
                        file.getName().endsWith(".mkv") ||
                        file.getName().endsWith(".mp4") ||
                        file.getName().endsWith(".mp3");
                // conditions of files i want to work on only.
                if (!file.isHidden() && file.canRead() &&
                        file.canWrite() && !restrictedFiles) {
                    // add valid files to array list of files the search operations will be operated on.
                    pendingFilesList.add(file);
                } else {
                    otherFilesList.add(file);
                }
            }
            // System.out.println(pendingFilesList);
        } catch (NullPointerException e) {
            System.out.println("** There are no files in directory to operate **");
            //e.printStackTrace();
            System.exit(0);
        }
    }


    /**
     * this is the main function that performs the search operation.
     * it check each file one after the other and also line by line for substrings that match
     * the keyword
     */
    public void searchInFile() {
        // iterate over each file for reading.
        for (File filesToCheck : pendingFilesList) {
            if (!filesToCheck.isDirectory()) {
                System.out.println(filesToCheck.getName());
                System.out.println("+======================+");
                try {
                    Scanner fileReader = new Scanner(filesToCheck);
                    int lineNUmber = 1; // line  no where keyword is found.
                    int noOfOccurrenceInFile = 0; // total no of occurrences per file.
                    while (fileReader.hasNext()) {
                        String currentLine = fileReader.nextLine();
                        // check each line for keyword.
                        if (currentLine.contains(keyWord)) {
                            noOfOccurrenceInFile += 1;

                            if (noOfOccurrenceInFile == 6) System.out.println("...");

                            if (noOfOccurrenceInFile >= 6) {
                                continue;
                            } else {
                                System.out.println("  match found in line:  " + lineNUmber);
                            }
                        }
                        lineNUmber++;
                    }
                    System.out.println("no of occurrences in " + filesToCheck.getName() +
                            " Is " + "( " + noOfOccurrenceInFile + " )"+"\n");
                } catch (Exception e) {
                    System.out.println("ERROR SOMETHING WENT WRONG WHILE SEARCHING");
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * check for possible key word matches in the folder name
     */
    public void folderSearch() {
        System.out.println("** DIRECTORIES **\n");
        int noOfFolderMatches = 0;
        for (File folders : pendingFilesList) {
            if (folders.isDirectory()) {
                if (folders.getName().contains(keyWord)) {
                    System.out.println("  " + folders.getName());
                    noOfFolderMatches++;
                }
            }
        }
        System.out.println("\n+++ Folder matches Found " + noOfFolderMatches + " +++\n");
    }


    /**
     * get other file matches that are not folders or readable files.
     */
    public void displayOtherFiles() {
        System.out.println("** other files **\n");
        int noOfMatches = 0;
        for (File file : otherFilesList) {
            if (!file.isDirectory() && !file.isHidden()) {
                if (file.getName().contains(keyWord)) {
                    System.out.println("  " + file.getName());
                    noOfMatches++;
                }
            }
        }
        System.out.println("\n+++ other matches Found " + noOfMatches + " +++\n");
    }


    /**
     * perfrom general search for all filenames and in file txt
     */
    public void generalSearch(){
        folderSearch();
        searchInFile();
        displayOtherFiles();
    }
}
