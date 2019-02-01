import java.io.*;
import java.util.Scanner;

public class NameReplacer {

        // return file extension
    private static String getFileExtension(String fullName) {

        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

        // return validated user's input
    private static String getValidatedInputFilepath() {

        File currentFile;
        boolean isFilePathExists = false;
        Scanner scanner = new Scanner(System.in);
        String inputString = "";

        while (!isFilePathExists) {

            inputString = scanner.nextLine();
            currentFile = new File(inputString);

            if (currentFile.exists()) {

                if (!getFileExtension(inputString).equals("txt")) {
                    System.out.println("Current file has no txt extension. Please, choose another file");
                } else if (currentFile.length() == 0) {
                    System.out.println("Current file is empty. Please, choose another file");
                } else {
                    isFilePathExists = true;
                }
            } else {
                System.out.println("Current file path: " + inputString + " doesn't exist");
            }
        }

        return inputString;
    }
           //creates outputFile
    private  static String getOutputFilepath(String filepath) {

        File outputFile = new File(filepath);
        File outputFileDirectory = outputFile.getParentFile();
        String outputFilePath = outputFile.getAbsolutePath();
        String outputFileName = outputFile.getName();
        int fileNumber = 0;

        try {

            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            //generates unique file name
            else {
                boolean stopLoop = false;
                boolean isFound = false;
                String[] filesInDirectory = outputFileDirectory.list();

                while (!stopLoop) {
                    isFound = false;
                    for (String fileInDir : filesInDirectory) {
                        if (fileInDir.equals(outputFileName)) {
                            isFound = true;
                            fileNumber++;
                            break;
                        }
                    }

                    outputFileName = "output" + fileNumber + ".txt";
                    if (!isFound) stopLoop = true;
                }
            }
            outputFilePath = outputFileDirectory + "\\" + outputFileName;
            File outFile = new File(outputFilePath);
            outFile.createNewFile();
        } catch (IOException a) {
            System.out.println(a.getMessage());
        }

        return outputFilePath;
    }
    // copy input file into output file
    private  static void copyFile(String fileSource, String dest) {

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(fileSource);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException a) {
            System.out.println(a.getMessage());
            System.out.println("Error occurred during copying file");
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException a) {
                System.out.println(a.getMessage());
                System.out.println("Error occurred during closing Input/Output streams in copying file");
            }
        }
    }
    //modifies output file
    private  static void modifyFile(String filepath, String oldString, String newString) {

        File fileToBeModified = new File(filepath);
        String oldContent = "";
        BufferedReader reader = null;
        FileWriter writer = null;

        try {
            reader = new BufferedReader(new FileReader(fileToBeModified));
            String line = reader.readLine();

            while (line != null) {
                oldContent = oldContent + line + System.lineSeparator();
                line = reader.readLine();
            }

            String newContent = oldContent.replaceAll(oldString, newString);
            writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error occurred during file modify");
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error occurred during closing streams");
            }
        }
    }

    public static void appLauncher(){
        String inputFilepath = getValidatedInputFilepath();
        File fileDest = new File(getOutputFilepath(inputFilepath));
        String outputDest = fileDest.getAbsolutePath();
        copyFile(inputFilepath, outputDest);
        modifyFile(fileDest.getAbsolutePath(),"Вася","Витя");
    }

    public static void main(String[] args)  {
        appLauncher();
    }
}