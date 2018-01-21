package org.nsu.dcis.amv.core.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.nsu.dcis.amv.core.exception.AmvException;
import org.nsu.dcis.amv.core.exception.FileException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FileUtil {
    public final static String ERROR_DIRECTORY = "ERROR";
    private static final String END_OF_LINE_CHARACTER = "\n";
    public static final String ALL_PERMISSIONS_EXCEPT_OTHER_WRITE = "775";
    private Logger log = Logger.getLogger(getClass().getName());
    private String[] namesOfFilesInTemporaryDirectory;

    public String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public List<FileLine> getFileLines(String fullFileName) {

        FileInputStream fis;
        BufferedReader reader;

        List<FileLine> fileLines = new ArrayList<>();
        try {
            fis = new FileInputStream(fullFileName);
            reader = new BufferedReader(new InputStreamReader(fis));
            String line = reader.readLine();
            int lineNumber = 0;
            while(line != null){
                FileLine fileLine = new FileLine(line);
                fileLine.setLineNumber(++lineNumber);
                fileLines.add(fileLine);
                line = reader.readLine();
            }
        } catch (Exception e) {
            throw new AmvException("Unable to read lines from '" + fullFileName + "'", e);
        }
        return fileLines;
    }

    public void setFilePermission(String filePermission, String fileName) {

        if (OperatingSystemCheck.isUnix()) {
            log.info("Running on Unix, OS was: " + OperatingSystemCheck.getOs() +
                    "' file permission on file '" + fileName + "' will be set to '" + filePermission + "'");

            StringBuilder commandBuilder = new StringBuilder("chmod ");
            commandBuilder.append(filePermission);
            commandBuilder.append(" ");
            commandBuilder.append(fileName);
            String setFilePermissionCommand = commandBuilder.toString();

            log.info("Set file permission with the following command: " + setFilePermissionCommand);
            CommandResult commandResult;
            try {
                commandResult = getCommandResult(setFilePermissionCommand);
            } catch (Exception e) {
                throw new AmvException("An error occurred when setting file permission '"
                        + filePermission + "for file '" + fileName +  "'", e);
            }

            if (!commandResult.succeeded()) {
                throw new AmvException("An error occurred when setting file permission '" +
                        filePermission + "' on file '" + fileName + ". The error was: " + commandResult.getStdError());
            }
            log.info("File permission '" + filePermission + "' has been set for file '" + fileName);
        } else {
            log.info("Not running on Unix, OS was: " + OperatingSystemCheck.getOs() +
                    "' file permission on file '" + fileName + "' will not be changed to '" + filePermission + "'");
        }
    }

    private CommandResult getCommandResult(String command) throws IOException, InterruptedException {
        CommandResult result;
        Process p = Runtime.getRuntime().exec(command);
        int exitCode = p.waitFor();
        String stdOut = getStreamAsString(p.getInputStream());
        String errOut = getStreamAsString(p.getErrorStream());
        result = new CommandResult(command, stdOut, errOut, exitCode);
        return result;
    }

    private String getStreamAsString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer buffer = new StringBuffer();
        String result;
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }
            result = buffer.toString();
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }

    public void moveFile(String fileName, String fromDirectory, String toDirectory) {
        String from;
        String to;
        StringBuilder commandBuilder;

        // Construct command when running locally on Windows
        if (OperatingSystemCheck.isWindows()) {
            commandBuilder = new StringBuilder("cmd.exe /c move ");
            from = fromDirectory + "\\" + fileName;
            to = toDirectory + "\\" + fileName;

            // Construct command when running on Linux
        } else {
            commandBuilder = new StringBuilder("mv ");
            from = fromDirectory + "/" + fileName;
            to = toDirectory + "/" + fileName;
        }

        // Append the last pieces to the move command
        commandBuilder.append(from)
                .append(" ")
                .append(to);

        String moveCommand = commandBuilder.toString();

        log.info("Move file with the following command: " + moveCommand);
        CommandResult result;
        try {
            result = getCommandResult(moveCommand);
        } catch (Exception e) {
            throw new AmvException("An error occurred when moving file " +
                    "with the following command '" + moveCommand + "'", e);
        }

        if (!result.succeeded()) {
            throw new AmvException("An error occurred when moving file " +
                    "with the following command '" + moveCommand + "'. The error was: " + result.getStdError());
        }
        log.info("File has been moved with the following command '" + moveCommand + "'");
    }

    public LinkedList getNamesOfFilesInTemporaryDirectory(File directoryLocation) {
        return (LinkedList) FileUtils.listFiles(directoryLocation, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    }

    public String readFileAsString(String absoluteFilePath) {
        String fileAsString;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(absoluteFilePath));
            fileAsString = IOUtils.toString(fileInputStream, "UTF-8");
        } catch (Exception e) {
            throw new AmvException("Unable to read file as string, file: '" +
                    absoluteFilePath + "'", e.getCause());
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                    fileInputStream = null;
                }
            } catch (IOException e) {
                log.warn("An error occurred when trying to close file :" + absoluteFilePath);
            }
        }
        return fileAsString;
    }

    public static void writeStringToFile(File fullFileName, String fileContent) {
        try {
            FileUtils.writeStringToFile(fullFileName, fileContent);
        } catch (IOException e) {
            throw new AmvException(
                    "An error occurred when writing file to the file system '" +
                            fullFileName + "'", e.getCause());
        }
    }

    public void archiveFile(String fileName, String fromDirectory, String toDirectory, String notificationName) {
        String from;
        String to;
        StringBuilder commandBuilder;

        // Construct command when running locally on Windows
        if (OperatingSystemCheck.isWindows()) {
            commandBuilder = new StringBuilder("cmd.exe /c move ");
            from = fromDirectory + "\\" + fileName;
            to = toDirectory + "\\" + notificationName;

            if (!(new File(to).exists())) {
                createDirectory(to);
            }
            to += "\\" + fileName;

            // Construct command when running on Linux
        } else {
            commandBuilder = new StringBuilder("mv ");
            from = fromDirectory + "/" + fileName;
            to = toDirectory + "/" + notificationName;

            if (!(new File(to).exists())) {
                createDirectory(to);
            }
            to += "/" + fileName;
        }

        // Append the last pieces to the move command
        commandBuilder.append(from)
                .append(" ")
                .append(to);

        String moveCommand = commandBuilder.toString();

        log.info("Move file with the following command: " + moveCommand);
        CommandResult result;
        try {
            result = getCommandResult(moveCommand);
        } catch (Exception e) {
            throw new AmvException("An error occurred when moving file " +
                    "with the following command '" + moveCommand + "'", e);
        }

        if (!result.succeeded()) {
            throw new AmvException("An error occurred when moving file '" +
                    "with the following command '" + moveCommand + "'. The error was: " + result.getStdError());
        }
        log.info("File has been moved with the following command '" + moveCommand + "'");
    }

    public void createDirectory(String directory) {
        if (!directoryExists(directory)) {
            File dir = new File(directory);
            if (dir.mkdir()) {
                log.info("Directory '" + directory + "' was created.");
                setFilePermission(ALL_PERMISSIONS_EXCEPT_OTHER_WRITE, directory);
            } else {
                throw new AmvException("Unable to create directory '" + directory + "'");
            }
        } else {
            log.info("Directory '" + directory + "' already exists and was not created.");
        }
    }

    public boolean directoryExists(String directory) {
        return (new File(directory).exists());
    }

    public Scanner getScanner(File file) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            log.info("The following file was not found: '" + file + "'");
            e.printStackTrace();
        }
        return scanner;
    }

    public boolean fileExists(String fileName) {
        return (new File(fileName).exists());
    }

    public Set<File> getArchiveDirectories(String rootDir) {
        Set<File> archiveDirectories = new HashSet<File>();

        File root = new File(rootDir);
        File[] list = root.listFiles();

        if (list == null) {
            return archiveDirectories;
        }

        for ( File file : list ) {
            if ( file.isDirectory() ) {
                archiveDirectories.add(file);
            }
        }

        return archiveDirectories;
    }

    public Set<File> getFilesInDirectory(String directoryString) {
        File directory = new File(directoryString);
        Set<File> filesInDirectory = new HashSet<File>();
        if (directory.isDirectory()) {
            File[] list = directory.listFiles();
            for ( File file : list ) {
                if ( file.isFile() ) {
                    filesInDirectory.add(file);
                }
            }
        } else {
            throw new AmvException("Not a directory '" + directoryString + "'");
        }
        return filesInDirectory;
    }

    public Set<File> getFilesInDirectory(String directoryString, String extension) {
        File directory = new File(directoryString);
        Set<File> filesInDirectory = new HashSet<File>();
        if (directory.isDirectory()) {
            File[] list = directory.listFiles();
            for ( File file : list ) {
                if ( file.isFile() ) {
                    boolean containsIgnoreCase = StringUtils.containsIgnoreCase(file.getName(), "." + extension);
                    if (containsIgnoreCase) {
                        filesInDirectory.add(file);
                    }
                }
            }
        } else {
            throw new AmvException("Not a directory '" + directoryString + "'");
        }
        return filesInDirectory;
    }

    public void deleteFile(File fileToDelete) {
        if (fileToDelete.isFile()) {
            boolean deleted = fileToDelete.delete();
            if (!deleted) {
                throw new AmvException("Unable to delete file '" + fileToDelete.getAbsolutePath() + "'");
            }
        } else {
            throw new AmvException("The file '" + fileToDelete.getAbsolutePath() +
                    "' can not be deleted because it does not exist.");
        }
    }

    public String getFullFileName(String filePath) {
        String fileName = new SimpleDateFormat("yyyy_MMdd_HHmmss.SSS").format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(filePath)
                .append("/")
                .append(fileName)
                .append(".xml");
        return stringBuilder.toString();
    }

    public void writeLineToFile(String fileName, String lineToWrite) {
        try {
            FileUtils.writeLines(new File(fileName), Arrays.asList(lineToWrite));
        } catch (IOException e) {
            throw new AmvException("Unable to write line to file, File Name: '" +
                    fileName + "', Line To Write: '" + lineToWrite + "'", e);
        }
    }

    public void appendLineToFile(String fileName, String lineToWrite) {
        log.info("Append to file '" + fileName + "' the line: " + lineToWrite);
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            fileWriter.append(lineToWrite + END_OF_LINE_CHARACTER);
            fileWriter.flush();
            fileWriter.close();
            fileWriter = null;
        } catch (IOException e) {
            throw new AmvException("Unable to append line to file, File Name: '" +
                    fileName + "', Line To Append: '" + lineToWrite + "'");
        }
    }

    public List<String> webServiceFileLineReader(String fullFileName, String searchEnvelopeId) {
        List<String> webServiceFileLines = new ArrayList<String>();
        try {
            File f = new File(fullFileName);
            List<String> lines = FileUtils.readLines(f, "UTF-8");
            for (String line : lines) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
                if (stringTokenizer.hasMoreTokens()) {
                    String envelopeId = stringTokenizer.nextToken().trim();
                    if (searchEnvelopeId.equals(envelopeId)) {
                        webServiceFileLines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new AmvException("An error occurred when reading lines from file '" + fullFileName + "'", e);
        }
        return webServiceFileLines;
    }

    public List<String> readLinesFromFile(String fullFileName) {
        List<String> lines;
        try {
            File f = new File(fullFileName);
            lines = FileUtils.readLines(f, "UTF-8");
        } catch (IOException e) {
            throw new AmvException("An error occurred when reading lines from file '" + fullFileName + "'", e);
        }
        return lines;
    }

    public void deleteAllFilesInDirectory(String filePath) {
        Set<File> filesInDirectory = getFilesInDirectory(filePath);
        for (File file :filesInDirectory) {
            deleteFile(file);
        }
    }

    public void deleteAllFilesInDirectoryWithExtension(String filePath, String extension) {
        Set<File> filesInDirectory = getFilesInDirectory(filePath);
        for (File file : filesInDirectory) {
            boolean containsIgnoreCase = StringUtils.containsIgnoreCase(file.getName(), "." + extension);
            if (containsIgnoreCase) {
                deleteFile(file);
            }
        }
    }

    public void writeSubscriberFileHeader(String subscriberFileName, String fileHeader) {
        log.info("Write to the subscription file '" + subscriberFileName + "' the file header: " + fileHeader);
        writeLineToFile(subscriberFileName, fileHeader);
        setFilePermission(ALL_PERMISSIONS_EXCEPT_OTHER_WRITE, subscriberFileName);
    }

    public static void copyFile(String fullSourceFileName, String fullDestinationFileName) {
        File sourceFile = new File(fullSourceFileName);
        File destinationFile = new File(fullDestinationFileName);
        try {
            FileUtils.copyFile(sourceFile, destinationFile);
        } catch (IOException e) {
            throw new AmvException("An error occurred when copying file" +
                    " from '" + sourceFile.getAbsolutePath() +
                    "' to '" + destinationFile.getAbsolutePath() + "'", e);
        }
    }

    public BufferedReader openFileForReadingLines(String fullPathToFile) {
        File file = new File(fullPathToFile);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new FileException("An error occurred when attempting to open the file for reading lines ", e);
        }

        return  new BufferedReader(new InputStreamReader(fis));
    }

    public BufferedWriter openFileForWritingLines(String execution_trace_log_file) {
        File fout = new File("out.txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(execution_trace_log_file);
        } catch (FileNotFoundException e) {
            throw new FileException("An error occurred when attempting to open the file for writing lines", e);
        }

        return new BufferedWriter(new OutputStreamWriter(fos));
    }

    public void closeFileForReadingLines(BufferedReader bufferedReader) {
        try {
            bufferedReader.close();
        } catch (IOException e) {
            throw new FileException("An error occurred when attempting to close file for reading lines", e);
        }
    }

    public void closeFileWritingLines(BufferedWriter bufferedWriter) {
        try {
            bufferedWriter.close();
        } catch (IOException e) {
            throw new FileException("An error occurred when attempting to close file for writing lines", e);
        }
    }

    public String readLineFromFile(BufferedReader bufferedReader) {
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            throw new FileException("An error occurred when attempting to read a line from file ", e);
        }
        return line;
    }

    public void writeLineToFile(BufferedWriter bufferedWriter, String lineToWrite) {
        try {
            bufferedWriter.write(lineToWrite);
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new FileException("An error occurred when attempting to write line to file ", e);
        }
    }
}
