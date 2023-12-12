package edu.project3;

public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        NginxLogAnalyzer nginxLogAnalyzer = new NginxLogAnalyzer();
        nginxLogAnalyzer.analyzeLogs(args);
    }
}
