package com.company;

import java.io.*;
import java.nio.file.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

        public static void main(String[] args) throws IOException {
	    FileService file = new FileService();
        file.getFile();
    }

}
