package com.yishenxiao.commons.utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import javazoom.jl.player.Player;

public class RecordingUtils {

    public static void play(String position) {
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(position));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int getDuration(String position) {

        int length = 0;
        try {
            MP3File mp3File = (MP3File) AudioFileIO.read(new File(position));
            MP3AudioHeader audioHeader = (MP3AudioHeader) mp3File.getAudioHeader();

            // 单位为秒
            length = audioHeader.getTrackLength();

            return length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }
    
   /* public static void main(String[] args) {
        String position = "d:/171222-085256.mp3";
        System.out.println(getDuration(position));
        //play(position);
    }*/
}