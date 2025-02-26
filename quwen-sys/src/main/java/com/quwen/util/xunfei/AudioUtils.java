package com.quwen.util.xunfei;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;

@Slf4j
public class AudioUtils {

    private static boolean execFfmpeg(String[] commands) {
        boolean result = false;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();
            if (process.exitValue() == 0) {
                result =true;
            }
            process.destroy();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String pcmToAudio(String filePath,String fileType) {

        File pcmFile = new File(filePath);
        if(!pcmFile.exists()){
            pcmFile.delete();
            return null;
        }

        String inputFile = pcmFile.getAbsolutePath();
        String outputFile = inputFile.substring(0, inputFile.length() - 3) + fileType;

        String[] commands = new String[]{"ffmpeg", "-y", "-f", "s16le", "-acodec", "pcm_s16le", "-ac", "1", "-ar", "16000",  "-i", inputFile,outputFile};
        if (execFfmpeg(commands) && new File(outputFile).exists()) {
            pcmFile.delete();
            return outputFile;
        }

        log.error("ffmpeg pcm转audio 失败:{}",outputFile);
        pcmFile.delete();
        return null;
    }

    public static String audioToPcm(String filePath) {
        File file = new File(filePath);
        String inputFile = file.getAbsolutePath();
        String outputFile = inputFile.substring(0, inputFile.length() - 3) + "pcm";

        String[] commands = new String[]{"ffmpeg", "-y", "-i", inputFile, "-acodec", "pcm_s16le", "-f", "s16le", "-ac", "1", "-ar", "16000", outputFile};
        if (execFfmpeg(commands) && new File(outputFile).exists()) {
            return outputFile;
        }
        return null;
    }

    public static String getTmpFilePath(String type) {
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempFolder = new File(tempDir + File.separator + "quwenFolder");
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }
        return tempFolder.getAbsolutePath() + File.separator + System.currentTimeMillis() + (int) ((Math.random() * 8 + 1) * 100) + "." + type;
    }

    public static String saveAudioFile(FileInputStream inputStream,String fileType) throws IOException {
        String inputFilePath = AudioUtils.getTmpFilePath(fileType);
        FileOutputStream fileOutputStream = new FileOutputStream(inputFilePath);
        // 缓冲流，提高写入性能
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            bufferedOutputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        bufferedOutputStream.close();

        return inputFilePath;
    }
}
